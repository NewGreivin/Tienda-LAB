package App;

import Clientes.Cliente;
import Clientes.MetodoPago;
import Clientes.RepositorioClientes;
import Clientes.ServicioClientes;
import Clientes.TipoMetodoPago;
import Facturacion.*;
import Facturacion.ItemFactura;
import Facturacion.RepositorioFacturas;
import Facturacion.ServicioFacturacion;
import Notificaciones.CanalNotificacion;
import Notificaciones.ServicioNotificaciones;
import Catalogo.Categoria;
import Catalogo.Producto;
import Catalogo.RepositorioCategorias;
import Catalogo.RepositorioProductos;
import Catalogo.ServicioCatalogo;
import Facades.TiendaFacade;
import java.io.UnsupportedEncodingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final CommandInvoker invoker = new CommandInvoker();

    private static final String OK  = "✅ ";
    private static final String ERR = "❌ ";

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        
        RepositorioCategorias repoCate = new RepositorioCategorias();
        RepositorioProductos repoProd = new RepositorioProductos();
        RepositorioClientes repoClie  = new RepositorioClientes();
        RepositorioFacturas repoFact  = RepositorioFacturas.getInstancia();

    ServicioCatalogo servCata = new ServicioCatalogo(repoCate, repoProd);
    ServicioClientes servClie = new ServicioClientes(repoClie);
    ServicioNotificaciones servNoti = new ServicioNotificaciones();
    ServicioFacturacion servFact = new ServicioFacturacion(repoFact, servNoti);

    // Crear fachada que agrupa los servicios
    TiendaFacade tienda = new TiendaFacade(servCata, servClie, servFact, servNoti);

    seedDatos(tienda);

        while (true){
            System.out.println("=== TIENDA ===");
            System.out.println("1-Categorías");
            System.out.println("2-Productos");
            System.out.println("3-Clientes");
            System.out.println("4-Facturación");
            System.out.println("5-Listado de facturas");
            System.out.println("6-Historial de notificaciones");
            System.out.println("0-Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();
            switch (op){
                case "1" -> menuCategorias(tienda);
                case "2" -> menuProductos(tienda);
                case "3" -> menuClientes(tienda);
                case "4" -> menuFacturacion(tienda);
                case "5" -> menuListados(tienda);
                case "6" -> mostrarHistorial(tienda);
                case "0" -> { System.out.println("¡Adiós!"); return; }
                default -> System.out.println(ERR + "Opción inválida.");
            }
        }
    }

    private static void seedDatos(TiendaFacade tienda){
        ServicioCatalogo catalogo = tienda.getServicioCatalogo();
        ServicioClientes clientes = tienda.getServicioClientes();

        var c1 = new Categoria(1,"Alimentos","Comestibles", true);
        var c2 = new Categoria(2,"Tecnología","Electrónica", true);
        catalogo.crearCategoria(c1); catalogo.crearCategoria(c2);

        catalogo.crearProducto(new Producto("A100","Arroz 1kg", 1150, 50, c1));
        catalogo.crearProducto(new Producto("A200","Frijol 1kg", 1450, 40, c1));
        catalogo.crearProducto(new Producto("T900","Audífonos BT", 15990, 10, c2));

        var cli = new Cliente("1","Ana Pérez","ana@correo.com","7000-0000");
        cli.addMetodoPago(new MetodoPago(1, TipoMetodoPago.TARJETA, "****-1234"));
        clientes.crearCliente(cli);
    }

    // =================== MENÚS ===================

    private static void menuCategorias(TiendaFacade tienda){
        ServicioCatalogo catalogo = tienda.getServicioCatalogo();
        System.out.println("-- Categorías --");
        catalogo.listarCategorias().forEach(System.out::println);
        System.out.println("a-Crear");
        System.out.println("b-Activar/Desactivar");
        System.out.println("Otra tecla para volver");
        String op=sc.nextLine().trim();

        if("a".equalsIgnoreCase(op)){
            try {
                System.out.print("Id: "); int id=Integer.parseInt(sc.nextLine());
                System.out.print("Nombre: "); String n=sc.nextLine();
                System.out.print("Descripción: "); String d=sc.nextLine();
                catalogo.crearCategoria(new Categoria(id,n,d,true));
                System.out.println(OK + "Categoría creada.");
            } catch (NumberFormatException e){
                System.out.println(ERR + "Id inválido.");
            }
        } else if("b".equalsIgnoreCase(op)){
            try {
                System.out.print("Id: "); int id=Integer.parseInt(sc.nextLine());
                Optional<Categoria> opt = catalogo.listarCategorias().stream()
                        .filter(c->c.getId()==id).findFirst();

                if (opt.isPresent()){
                    Categoria c = opt.get();
                    c.setActiva(!c.isActiva());
                    System.out.println(OK + "Categoría " + c.getNombre() +
                            (c.isActiva() ? " activada." : " desactivada."));
                } else {
                    System.out.println(ERR + "Categoría no encontrada.");
                }
            } catch (NumberFormatException e){
                System.out.println(ERR + "Id inválido.");
            }
        }
    }

    private static void menuProductos(TiendaFacade tienda){
        ServicioCatalogo catalogo = tienda.getServicioCatalogo();
        System.out.println("-- Productos --");
        catalogo.listarProductos().forEach(System.out::println);
        System.out.println("a-Crear");
        System.out.println("e-Editar");
        System.out.println("d-Eliminar");
        System.out.println("s-Buscar (código/nombre)");
        System.out.println("f-Filtro por categoría");
        System.out.println("Otra para volver");
        String op=sc.nextLine().trim();

        switch (op.toLowerCase()){
            case "a" -> {
                System.out.print("Código: "); String codigo=sc.nextLine();
                System.out.print("Nombre: "); String nombre=sc.nextLine();
                try {
                    System.out.print("Precio: "); double precio=Double.parseDouble(sc.nextLine());
                    System.out.print("Stock: "); int stock=Integer.parseInt(sc.nextLine());
                    Categoria cat = seleccionarCategoria(catalogo);
                    if (cat == null){ System.out.println(ERR + "Categoría no existe."); break; }
                    if (catalogo.buscarPorCodigo(codigo).isPresent()){
                        System.out.println(ERR + "Ya existe un producto con ese código.");
                        break;
                    }
                    catalogo.crearProducto(new Producto(codigo,nombre,precio,stock,cat));
                    System.out.println(OK + "Producto creado.");
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Precio/Stock inválidos.");
                }
            }
            case "e" -> {
                System.out.print("Código producto: "); String codigo=sc.nextLine();
                catalogo.buscarPorCodigo(codigo).ifPresentOrElse(p -> {
                    System.out.print("Nuevo nombre: "); String nombre=sc.nextLine();
                    try {
                        System.out.print("Nuevo precio: "); double precio=Double.parseDouble(sc.nextLine());
                        System.out.print("Nuevo stock: "); int stock=Integer.parseInt(sc.nextLine());
                        Categoria cat = seleccionarCategoria(catalogo);
                        if (cat == null){ System.out.println(ERR + "Categoría no existe."); return; }
                        catalogo.editarProducto(codigo, nombre, precio, stock, cat);
                        System.out.println(OK + "Producto actualizado.");
                    } catch (NumberFormatException e){
                        System.out.println(ERR + "Precio/Stock inválios.");
                    }
                }, () -> System.out.println(ERR + "Producto no encontrado."));
            }
            case "d" -> {
                System.out.print("Código producto: "); String codigo=sc.nextLine();
     
                catalogo.buscarPorCodigo(codigo).ifPresentOrElse(p -> {
                    if (!confirmar("¿Eliminar producto " + codigo + "?")) {
                        System.out.println(OK + "Operación cancelada.");
                        return;
                    }
                    catalogo.eliminarProducto(codigo);
                    System.out.println(OK + "Producto eliminado.");
                }, () -> System.out.println(ERR + "Producto no encontrado."));
            }
            case "s" -> {
                System.out.print("Buscar por (c)ódigo o (n)ombre?: ");
                String t=sc.nextLine();
                if("c".equalsIgnoreCase(t)){
                    System.out.print("Código: ");
                    catalogo.buscarPorCodigo(sc.nextLine()).ifPresentOrElse(
                        p -> System.out.println(OK + p),
                        () -> System.out.println(ERR + "Producto no encontrado.")
                    );
                } else {
                    System.out.print("Nombre contiene: ");
                    var results = catalogo.buscarPorNombre(sc.nextLine());
                    if (results.isEmpty()) System.out.println(ERR + "Sin resultados.");
                    else results.forEach(System.out::println);
                }
            }
            case "f" -> {
                Categoria cat = seleccionarCategoria(catalogo);
                if (cat == null){ System.out.println(ERR + "Categoría no existe."); break; }
                var lista = catalogo.filtrarPorCategoria(cat);
                if (lista.isEmpty()) System.out.println(ERR + "Sin productos en esa categoría.");
                else lista.forEach(System.out::println);
            }
            default -> { }
        }
    }

    private static void menuClientes(TiendaFacade tienda){
        ServicioClientes clientes = tienda.getServicioClientes();
        System.out.println("-- Clientes --");
        clientes.listarClientes().forEach(System.out::println);
        System.out.println("a-Crear");
        System.out.println("e-Editar");
        System.out.println("d-Eliminar");
        System.out.println("m-Añadir método pago");
        System.out.println("r-Remover método pago");
        System.out.println("Otra para volver");
        String op=sc.nextLine().trim();

        switch (op.toLowerCase()){
            case "a" -> {
                System.out.print("Id: "); String id=sc.nextLine();
                System.out.print("Nombre: "); String n=sc.nextLine();
                System.out.print("Email: "); String em=sc.nextLine();
                System.out.print("Teléfono: "); String t=sc.nextLine();
                boolean existe = clientes.listarClientes().stream().anyMatch(c -> c.getId().equals(id));
                if (existe){ System.out.println(ERR + "Ya existe un cliente con ese Id."); break; }
                clientes.crearCliente(new Cliente(id,n,em,t));
                System.out.println(OK + "Cliente creado.");
            }
            case "e" -> {
                System.out.print("Id: "); String id=sc.nextLine();
                var opt = clientes.listarClientes().stream().filter(c->c.getId().equals(id)).findFirst();
                if (opt.isPresent()){
                    System.out.print("Nombre: "); String n=sc.nextLine();
                    System.out.print("Email: "); String em=sc.nextLine();
                    System.out.print("Teléfono: "); String t=sc.nextLine();
                    clientes.editarCliente(id,n,em,t); // servicio void
                    System.out.println(OK + "Cliente actualizado.");
                } else {
                    System.out.println(ERR + "Cliente no encontrado.");
                }
            }
            case "d" -> {
                System.out.print("Id: "); String id=sc.nextLine();
                var opt = clientes.listarClientes().stream().filter(c->c.getId().equals(id)).findFirst();
                if (opt.isPresent()){
                    if (!confirmar("¿Eliminar cliente " + id + "?")) {
                        System.out.println(OK + "Operación cancelada.");
                        break;
                    }
                    clientes.eliminarCliente(id);
                    System.out.println(OK + "Cliente eliminado.");
                } else {
                    System.out.println(ERR + "Cliente no encontrado.");
                }
            }
            case "m" -> {
                System.out.print("Id Cliente: "); String id=sc.nextLine();
                var opt = clientes.listarClientes().stream().filter(c->c.getId().equals(id)).findFirst();
                if (opt.isPresent()){
                    try {
                        System.out.print("Id método: "); int idm=Integer.parseInt(sc.nextLine());
                        System.out.print("Tipo (TARJETA/TRANSFERENCIA/EFECTIVO): ");
                        var tipo = TipoMetodoPago.valueOf(sc.nextLine().toUpperCase());
                        System.out.print("Detalles: "); String det=sc.nextLine();
                        clientes.agregarMetodoPago(id, new MetodoPago(idm, tipo, det));
                        System.out.println(OK + "Método agregado.");
                    } catch (NumberFormatException e){
                        System.out.println(ERR + "Datos inválidos o tipo no reconocido.");
                    }
                } else {
                    System.out.println(ERR + "Cliente no encontrado.");
                }
            }
            case "r" -> {
                System.out.print("Id Cliente: "); String id=sc.nextLine();
                var opt = clientes.listarClientes().stream().filter(c->c.getId().equals(id)).findFirst();
                if (opt.isPresent()){
                    try {
                        System.out.print("Id método: "); int idm=Integer.parseInt(sc.nextLine());
                        clientes.eliminarMetodoPago(id, idm);
                        System.out.println(OK + "Método eliminado (si existía).");
                    } catch (NumberFormatException e){
                        System.out.println(ERR + "Id de método inválido.");
                    }
                } else {
                    System.out.println(ERR + "Cliente no encontrado.");
                }
            }
            default -> {}
        }
    }

    private static void menuFacturacion(TiendaFacade tienda){
        ServicioFacturacion fact = tienda.getServicioFacturacion();
        ServicioCatalogo catalogo = tienda.getServicioCatalogo();
        ServicioClientes clientes = tienda.getServicioClientes();
        System.out.println("-- Facturación --");
        System.out.println("a-Crear factura");
        System.out.println("i-Agregar ítem");
        System.out.println("e-Emitir (notifica)");
        System.out.println("p-Pagar");
        System.out.println("x-Anular");
        System.out.println("v-Ver factura");
        System.out.println("h-Ver historial de comandos");
        System.out.println("Otra para volver");
        String op=sc.nextLine().trim();

        switch (op.toLowerCase()){
            case "a" -> {
                try {
                    System.out.print("Número: "); int num=Integer.parseInt(sc.nextLine());
                    System.out.print("Id cliente: "); String idc=sc.nextLine();
                    var cliOpt = clientes.listarClientes().stream().filter(c->c.getId().equals(idc)).findFirst();
                    if(cliOpt.isPresent()){
                        Command cmd = new CrearFacturaCommand(fact, num, cliOpt.get());
                        invoker.executeCommand(cmd);
                        System.out.println(OK + "Factura creada.");
                    } else {
                        System.out.println(ERR + "Cliente no existe.");
                    }
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Número inválido.");
                }
            }
            case "i" -> {
                try {
                    System.out.print("N° factura: "); int num=Integer.parseInt(sc.nextLine());
                    Optional<?> fOpt = fact.obtenerFactura(num);
                    if (fOpt.isEmpty()){ System.out.println(ERR + "Factura no existe."); break; }

                    System.out.print("Código producto: "); String cod=sc.nextLine();
                    var prodOpt = catalogo.buscarPorCodigo(cod);
                    if (prodOpt.isEmpty()){ System.out.println(ERR + "Producto no existe."); break; }

                    var p = prodOpt.get();
                    System.out.print("Cantidad: "); int cant=Integer.parseInt(sc.nextLine());
                    if (cant <= 0){ System.out.println(ERR + "Cantidad inválida."); break; }

                    var item = new ItemFactura(p, cant);
                    Command cmd = new AgregarItemFacturaCommand(fact, num, item);
                    invoker.executeCommand(cmd);
                    System.out.println(OK + "Ítem agregado: " + item);
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Datos numéricos inválidos.");
                }
            }
            case "e" -> {
                try {
                    System.out.print("N° factura: "); int num=Integer.parseInt(sc.nextLine());
                    if (fact.obtenerFactura(num).isEmpty()){ System.out.println(ERR + "Factura no existe."); break; }
                    var canales = List.of(CanalNotificacion.EMAIL, CanalNotificacion.SMS, CanalNotificacion.PANTALLA);
                    Command cmd = new EmitirFacturaCommand(fact, num, canales);
                    invoker.executeCommand(cmd);
                    System.out.println(OK + "Factura emitida.");
                    fact.obtenerFactura(num).ifPresent(System.out::println);
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Número inválido.");
                }
            }
            case "p" -> {
                try {
                    System.out.print("N° factura: "); int n=Integer.parseInt(sc.nextLine());
                    if (fact.obtenerFactura(n).isEmpty()){ System.out.println(ERR + "Factura no existe."); break; }
                    Command cmd = new PagarFacturaCommand(fact, n);
                    invoker.executeCommand(cmd);
                    System.out.println(OK + "Factura pagada.");
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Número inválido.");
                }
            }
            case "x" -> {
                try {
                    System.out.print("N° factura: "); int n=Integer.parseInt(sc.nextLine());
                    if (fact.obtenerFactura(n).isEmpty()){ System.out.println(ERR + "Factura no existe."); break; }
                    Command cmd = new AnularFacturaCommand(fact, n);
                    invoker.executeCommand(cmd);
                    System.out.println(OK + "Factura anulada.");
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Número inválido.");
                }
            }
            case "v" -> {
                try {
                    System.out.print("N° factura: "); int n=Integer.parseInt(sc.nextLine());
                    fact.obtenerFactura(n).ifPresentOrElse(f -> {
                        System.out.println(f);
                        f.getItems().forEach(System.out::println);
                        System.out.printf("Sub: %.2f  Imp: %.2f  Total: %.2f%n",
                                f.getSubtotal(), f.getImpuesto(), f.getTotal());
                    }, () -> System.out.println(ERR + "No existe."));
                } catch (NumberFormatException e){
                    System.out.println(ERR + "Número inválido.");
                }
            }
            case "h" -> {
                System.out.println("Historial de comandos ejecutados:");
                invoker.getCommandHistory().forEach(cmd -> 
                    System.out.println("- " + cmd.getClass().getSimpleName())
                );
            }
            default -> {  }
        }
    }

    private static void menuListados(TiendaFacade tienda){
        ServicioFacturacion fact = tienda.getServicioFacturacion();
        System.out.println("-- Listado de facturas --");
        System.out.println("1-Todas");
        System.out.println("2-Por estado");
        System.out.println("3-Por rango de fecha");
        String op=sc.nextLine().trim();
        switch (op){
            case "1" -> fact.listar().forEach(System.out::println);
            case "2" -> {
                try {
                    System.out.print("Estado (EMITIDA/PAGADA/ANULADA/PENDIENTE): ");
                    var e = EstadoFactura.valueOf(sc.nextLine().toUpperCase());
                    var lista = fact.filtrarPorEstado(e);
                    if (lista.isEmpty()) System.out.println(ERR + "Sin resultados.");
                    else lista.forEach(System.out::println);
                } catch (IllegalArgumentException ex){
                    System.out.println(ERR + "Estado inválido.");
                }
            }
            case "3" -> {
                try {
                    System.out.print("Desde (YYYY-MM-DD): "); LocalDate d=LocalDate.parse(sc.nextLine());
                    System.out.print("Hasta (YYYY-MM-DD): "); LocalDate h=LocalDate.parse(sc.nextLine());
                    var lista = fact.filtrarPorFecha(d,h);
                    if (lista.isEmpty()) System.out.println(ERR + "Sin resultados.");
                    else lista.forEach(System.out::println);
                } catch (Exception e){
                    System.out.println(ERR + "Fechas inválidas.");
                }
            }
            default -> {}
        }
    }

    private static void mostrarHistorial(TiendaFacade tienda){
        ServicioNotificaciones n = tienda.getServicioNotificaciones();
        System.out.println("-- Historial de notificaciones --");
        n.getHistorial().forEach(System.out::println);
    }

    // ============== Utilidades ==============

    private static Categoria seleccionarCategoria(ServicioCatalogo catalogo){
        System.out.println("Categorías:");
        catalogo.listarCategorias().forEach(System.out::println);
        System.out.print("Id categoría: ");
        try {
            int idc = Integer.parseInt(sc.nextLine());
            return catalogo.listarCategorias().stream()
                    .filter(c->c.getId()==idc).findFirst()
                    .orElse(null);
        } catch (NumberFormatException e){
            System.out.println(ERR + "Id inválido.");
            return null;
        }
    }

    private static boolean confirmar(String msg){
        System.out.print(msg + " (s/n): ");
        return sc.nextLine().trim().equalsIgnoreCase("s");
    }
}

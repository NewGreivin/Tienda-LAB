/**
 *
 * @author Greivin
 */
package Facades;

import Catalogo.Categoria;
import Catalogo.Producto;
import Catalogo.ServicioCatalogo;
import Clientes.Cliente;
import Clientes.ServicioClientes;
import Facturacion.EstadoFactura;
import Facturacion.Factura;
import Facturacion.ItemFactura;
import Facturacion.ServicioFacturacion;
import Notificaciones.CanalNotificacion;
import Notificaciones.Notificacion;
import Notificaciones.ServicioNotificaciones;
import java.util.Arrays;
import java.util.List;

public class TiendaFacade {
    private final ServicioCatalogo servicioCatalogo;
    private final ServicioClientes servicioClientes;
    private final ServicioFacturacion servicioFacturacion;
    private final ServicioNotificaciones servicioNotificaciones;

    public ServicioCatalogo getServicioCatalogo() { return servicioCatalogo; }
    public ServicioClientes getServicioClientes() { return servicioClientes; }
    public ServicioFacturacion getServicioFacturacion() { return servicioFacturacion; }
    public ServicioNotificaciones getServicioNotificaciones() { return servicioNotificaciones; }
    
    public TiendaFacade(ServicioCatalogo servicioCatalogo,ServicioClientes servicioClientes,
            ServicioFacturacion servicioFacturacion,ServicioNotificaciones servicioNotificaciones) {
        this.servicioCatalogo = servicioCatalogo;
        this.servicioClientes = servicioClientes;
        this.servicioFacturacion = servicioFacturacion;
        this.servicioNotificaciones = servicioNotificaciones;
    }
 
    	// ---------------- Venta ----------------
	public Factura crearNuevaVenta(int numeroFactura, String idCliente) {
		Cliente encontrado = null;
		for (Cliente c : servicioClientes.listarClientes()) {
			if (c.getId().equals(idCliente)) { encontrado = c; break; }
		}
		if (encontrado == null) throw new IllegalArgumentException("Cliente no encontrado");
		return servicioFacturacion.crearFactura(numeroFactura, encontrado);
	}

	public void agregarProductoAVenta(int numeroFactura, String codigoProducto, int cantidad) {
		Producto encontrado = null;
		for (Producto p : servicioCatalogo.listarProductos()){
			if (p.getCodigo().equals(codigoProducto)) { encontrado = p; break; }
		}
		if (encontrado == null) throw new IllegalArgumentException("Producto no encontrado");
		ItemFactura item = new ItemFactura(encontrado, cantidad);
		servicioFacturacion.agregarItem(numeroFactura, item);
	}

	public void completarVenta(int numeroFactura) {
		List<CanalNotificacion> canales = Arrays.asList(CanalNotificacion.EMAIL,CanalNotificacion.SMS,CanalNotificacion.PANTALLA);
		servicioFacturacion.emitirFactura(numeroFactura, canales);
		servicioFacturacion.pagarFactura(numeroFactura);
	}

	// ---------------- Catálogo ----------------
	public void agregarNuevoProducto(String codigo, String nombre, double precio, int stock, int categoriaId) {
		Categoria encontrada = null;
		for (Categoria c : servicioCatalogo.listarCategorias()){
			if (c.getId() == categoriaId) { encontrada = c; break; }
		}
		if (encontrada == null) throw new IllegalArgumentException("Categoría no encontrada");
		Producto producto = new Producto(codigo, nombre, precio, stock, encontrada);
		servicioCatalogo.crearProducto(producto);
	}

	// ---------------- Consultas ----------------
	public List<Factura> obtenerVentasDelDia(){
		return servicioFacturacion.filtrarPorEstado(EstadoFactura.PAGADA);
	}

	public List<Notificacion> obtenerHistorialNotificaciones(){
		return servicioNotificaciones.getHistorial();
	}

	public List<Producto> buscarProductosPorNombre(String nombre){
		return servicioCatalogo.buscarPorNombre(nombre);
	}
}
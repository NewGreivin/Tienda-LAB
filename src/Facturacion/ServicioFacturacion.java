/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Facturacion;

import Clientes.Cliente;
import Notificaciones.CanalNotificacion;
import Notificaciones.ServicioNotificaciones;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author jprod
 */
public class ServicioFacturacion {
    private final RepositorioFacturas repo;
    private final ServicioNotificaciones notificador;

    public ServicioFacturacion(RepositorioFacturas r, ServicioNotificaciones n){
        this.repo=r; this.notificador=n;
    }

    public Factura crearFactura(int numero, Cliente cliente){
        Factura f = new Factura(numero, cliente);
        repo.guardar(f);
        return f;
    }

    public void agregarItem(int numeroFactura, ItemFactura item){
        repo.buscar(numeroFactura).ifPresent(f->{ 
            f.addItem(item);
            repo.guardar(f);
        });
    }

    public Optional<Factura> obtenerFactura(int numero){
        return repo.buscar(numero);
    }

    public void emitirFactura(int numero, List<CanalNotificacion> canales){
        var action = new AccionEmitirFactura(repo, notificador, canales);
        action.ejecutar(numero);
    }

    public void pagarFactura(int numero){
        var action = new PagarFacturaAction(repo);
        action.ejecutar(numero);
    }
    public void anularFactura(int numero){
        var action = new AccionAnularFactura(repo);
        action.ejecutar(numero);
    }

    public List<Factura> listar(){ 
        return repo.obtenerTodo();
    }
    
    public List<Factura> filtrarPorEstado(EstadoFactura e){
        return repo.filtrarPorEstado(e);
    }
    
    public List<Factura> filtrarPorFecha(LocalDate d, LocalDate h){
        return repo.filtrarPorFecha(d,h);
    }

}

package Facturacion;

import Notificaciones.CanalNotificacion;
import Notificaciones.ServicioNotificaciones;
import java.util.List;

public class AccionEmitirFactura extends AccionFactura {
    private final ServicioNotificaciones notificador;
    private final List<CanalNotificacion> canales;

    public AccionEmitirFactura(RepositorioFacturas repo, ServicioNotificaciones n, List<CanalNotificacion> canales){
        super(repo);
        this.notificador = n;
        this.canales = canales;
    }


    @Override
    protected void cambiarEstado(Factura f) {
        f.setEstado(EstadoFactura.EMITIDA);
    }

    @Override
    protected void despuesDeCambiar(Factura f) {
        for (CanalNotificacion c : canales) {
            notificador.enviar(f, c);
        }
    }
}

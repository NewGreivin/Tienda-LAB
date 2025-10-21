package Facturacion;

import Notificaciones.CanalNotificacion;
import java.util.List;

public class EmitirFacturaCommand implements Command {
    private final ServicioFacturacion servicio;
    private final int numeroFactura;
    private final List<CanalNotificacion> canales;

    public EmitirFacturaCommand(ServicioFacturacion servicio, int numeroFactura, List<CanalNotificacion> canales) {
        this.servicio = servicio;
        this.numeroFactura = numeroFactura;
        this.canales = canales;
    }

    @Override
    public void ejecutar() {
        servicio.emitirFactura(numeroFactura, canales);
    }
}
package Facturacion;

public class PagarFacturaCommand implements Command {
    private final ServicioFacturacion servicio;
    private final int numeroFactura;

    public PagarFacturaCommand(ServicioFacturacion servicio, int numeroFactura) {
        this.servicio = servicio;
        this.numeroFactura = numeroFactura;
    }

    @Override
    public void ejecutar() {
        servicio.pagarFactura(numeroFactura);
    }
}
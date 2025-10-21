package Facturacion;

public class AnularFacturaCommand implements Command {
    private final ServicioFacturacion servicio;
    private final int numeroFactura;

    public AnularFacturaCommand(ServicioFacturacion servicio, int numeroFactura) {
        this.servicio = servicio;
        this.numeroFactura = numeroFactura;
    }

    @Override
    public void ejecutar() {
        servicio.anularFactura(numeroFactura);
    }
}
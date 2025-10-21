package Facturacion;

public class AgregarItemFacturaCommand implements Command {
    private final ServicioFacturacion servicio;
    private final int numeroFactura;
    private final ItemFactura item;

    public AgregarItemFacturaCommand(ServicioFacturacion servicio, int numeroFactura, ItemFactura item) {
        this.servicio = servicio;
        this.numeroFactura = numeroFactura;
        this.item = item;
    }

    @Override
    public void ejecutar() {
        servicio.agregarItem(numeroFactura, item);
    }
}
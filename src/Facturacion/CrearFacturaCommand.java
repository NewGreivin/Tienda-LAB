package Facturacion;

import Clientes.Cliente;

public class CrearFacturaCommand implements Command {
    private final ServicioFacturacion servicio;
    private final int numeroFactura;
    private final Cliente cliente;

    public CrearFacturaCommand(ServicioFacturacion servicio, int numeroFactura, Cliente cliente) {
        this.servicio = servicio;
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
    }

    @Override
    public void ejecutar() {
        servicio.crearFactura(numeroFactura, cliente);
    }
}

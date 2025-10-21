package Facturacion;

public class PagarFacturaAction extends AccionFactura {

    public PagarFacturaAction(RepositorioFacturas repo){ super(repo); }

    @Override
    protected void cambiarEstado(Factura f) {
        f.setEstado(EstadoFactura.PAGADA);
    }
}

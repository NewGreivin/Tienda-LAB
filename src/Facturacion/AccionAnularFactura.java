package Facturacion;

public class AccionAnularFactura extends AccionFactura {

    public AccionAnularFactura(RepositorioFacturas repo){ super(repo); }

    @Override
    protected void cambiarEstado(Factura f) {
        f.setEstado(EstadoFactura.ANULADA);
    }
}

package Facturacion;


public abstract class AccionFactura {
    protected final RepositorioFacturas repositorio;

    public AccionFactura(RepositorioFacturas repositorio) {
        this.repositorio = repositorio;
    }

    public final void ejecutar(int numero) {
        repositorio.buscar(numero).ifPresent(factura -> {
            antesDeCambiar(factura);
            cambiarEstado(factura);
            repositorio.guardar(factura);
            despuesDeCambiar(factura);
        });
    }

    protected void antesDeCambiar(Factura factura) { }

    protected abstract void cambiarEstado(Factura factura);

    protected void despuesDeCambiar(Factura factura) { }
}


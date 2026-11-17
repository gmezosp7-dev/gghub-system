package modelos;

public class ItemConsumo {
    private  final Producto producto;
    private  final int cantidad;
    private  final double precioUnitario;

    public ItemConsumo(Producto producto, int cantidad) {
        // primero se valida: si la cantidad es mala, el item ni siquiera se crea
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        // se copia el precio de hoy, si mañana cambia, esta venta no se altera
        this.precioUnitario = producto.getPrecioVenta();
    }

    //Sobrecarga para cargar desde el archivo: recibe el precio que se guardo, no el de hoy
    public ItemConsumo(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
    public double subtotal(){
        return precioUnitario * cantidad;
    }

    @Override
    public String toString(){
        return producto.getNombre()  + " x" + cantidad + " = " + "$"+ subtotal();

    }
}

package modelos;

import enums.TipoVenta;
import interfaces.MetodoPago;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Venta {
    private final LocalDateTime fechaHora;
    private final TipoVenta tipo;
    private final SesionJuego sesion;
    private ArrayList <ItemConsumo> items = new ArrayList<>();
    private  boolean confirmada;
    // como se pago (solo en CONTADO); queda null en CARGO_SESION, que se paga al finalizar la sesion
    private MetodoPago metodoPago;

    public Venta(TipoVenta tipo, SesionJuego sesion) {
        // un cargo a sesion sin sesion no tiene a quien cobrarle
        if (tipo == TipoVenta.CARGO_SESION && sesion == null) {
            throw new IllegalArgumentException("Una venta con cargo a sesion necesita una sesion");
        }
        this.tipo = tipo;
        this.sesion = sesion;
        // la hora la pone la venta sola: nadie puede inventar cuando se vendio
        this.fechaHora = LocalDateTime.now();
    }

    //Sobrecarga para cargar desde el archivo: recibe la fecha y los productos que se guardaron
    //(solo se guardan ventas confirmadas, por eso queda confirmada)
    public Venta(TipoVenta tipo, SesionJuego sesion, LocalDateTime fechaHora, ArrayList<ItemConsumo> items) {
        this.tipo = tipo;
        this.sesion = sesion;
        this.fechaHora = fechaHora;
        this.items = new ArrayList<>(items);
        this.confirmada = true;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoVenta getTipo() {
        return tipo;
    }

    public SesionJuego getSesion() {
        return sesion;
    }

    public ArrayList<ItemConsumo> getItems() {
        return new ArrayList<>(items);
    }


    public boolean isConfirmada() {
        return confirmada;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }


    // unica puerta para meter productos a la venta (no hay setItems)
    //rechaza si esta confirmada o no hay stock
    public void agregarItem(Producto producto, int cantidad){
        if (confirmada){
            throw new IllegalArgumentException("No se pueden agregar productos a una venta confirmada");
        }
        // aviso temprano: el stock todavia NO se descuenta, eso pasa en confirmar()
        if (!producto.hayStock(cantidad)){
            throw new IllegalArgumentException("Stock insuficiente de " + producto.getNombre()
                    + ": quedan " + producto.getStock());
        }
        // ItemConsumo valida que la cantidad sea mayor a cero y copia el precio de hoy
        items.add(new ItemConsumo(producto, cantidad));
    }

    public double calcularTotal(){
        double suma = 0.0;
        for (ItemConsumo e : items){
            suma = e.subtotal() + suma;
        }
        return suma;

    }
    // metodo: como paga el cliente si es CONTADO; en CARGO_SESION va null (se paga con la sesion)
    // REGLA DEL PROYECTO: no llamar directo; se confirma con GestorVentas.confirmar(...), que ademas
    // guarda la venta en su lista. Si se llama directo, el dinero no queda registrado y no sale en el
    // cierre de caja (RF-12). Java no deja esconder este metodo porque Venta y GestorVentas estan en
    // paquetes distintos, por eso es una regla (igual que cambiarEstado solo desde los gestores).
    public void confirmar(MetodoPago metodo){
        if(confirmada){
            throw new IllegalArgumentException("La venta ya fue confirmada");
        }
        if (items.isEmpty()){
            throw new IllegalArgumentException("no se puede confirmar una venta sin productos");
        }
        if (tipo == TipoVenta.CONTADO && metodo == null){
            throw new IllegalArgumentException("Una venta de contado necesita un metodo de pago");
        }
        if (tipo == TipoVenta.CARGO_SESION && metodo != null){
            throw new IllegalArgumentException("Un cargo a sesion no se paga ahora: se paga al finalizar la sesion");
        }
        // primero se valida (todo antes de tocar inventario)
        // no se le carga nada a una cuenta que ya se cerro
        if (tipo == TipoVenta.CARGO_SESION && !sesion.estaActiva()){
            throw new IllegalArgumentException("La sesion del puesto " + sesion.getPuesto().getId()
                    + " ya fue finalizada: no se le pueden cargar consumos");
        }
        // el stock pudo cambiar desde que se armo la venta (otra venta se llevo las ultimas)
        for (ItemConsumo e : items){
            if (!e.getProducto().hayStock(e.getCantidad())){
                throw new IllegalArgumentException("Stock insuficiente de " + e.getProducto().getNombre()
                        + ": quedan " + e.getProducto().getStock());
            }
        }
        // se cobra ANTES de descontar: si el pago falla (efectivo insuficiente...), el inventario no se toca.
        // La venta no sabe si es Efectivo, Tarjeta o PagoDigital: solo dice "pagate". Polimorfismo.
        if (tipo == TipoVenta.CONTADO){
            metodo.pagar(calcularTotal());
            metodoPago = metodo;
        }
        for(ItemConsumo e: items){
            e.getProducto().descontarStock(e.getCantidad());
            if(tipo == TipoVenta.CARGO_SESION){
                sesion.agregarConsumo(e);
            }
        }
        confirmada = true;
    }

    @Override
    public String toString(){
        // solo se pide el puesto si es cargo a sesion: en contado la sesion es null
        String destino;
        if (tipo == TipoVenta.CARGO_SESION) {
            destino = " a la sesion del puesto " + sesion.getPuesto().getId();
        } else {
            destino = "";
        }
        String estado;
        if (confirmada) {
            estado = " | confirmada";
        } else {
            estado = " | sin confirmar";
        }

        return "[" + tipo + "]" + destino + " | " + items.size() + " productos | total: $" + calcularTotal() + estado;
    }
}

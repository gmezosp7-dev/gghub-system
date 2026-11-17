package servicios;

import enums.TipoVenta;
import interfaces.MetodoPago;
import modelos.SesionJuego;
import modelos.Venta;
import modelos.Producto;
import java.util.ArrayList;

public class GestorVentas {
    private ArrayList<Venta> ventas = new ArrayList<>();
    private final InventarioBarra inventarioBarra;
    private final GestorSesiones gestorSesiones;

    public GestorVentas(InventarioBarra inventarioBarra, GestorSesiones gestorSesiones) {
        this.inventarioBarra = inventarioBarra;
        this.gestorSesiones = gestorSesiones;
    }
    public Venta nuevaVentaContado(){
        return new Venta(TipoVenta.CONTADO,null);
    }
    public Venta nuevaVentaCargoSesion(int id){
        //busca la sesion abierta de esa estacion
        SesionJuego sesion = gestorSesiones.buscarSesionActiva(id);
        //si no hay  no hay a quien cargarle la cuenta
        if (sesion == null) {
            throw new IllegalArgumentException("No hay una sesion activa en el puesto " + id);
        }
        //creo la venta cargada a esa sesion
        return new Venta(TipoVenta.CARGO_SESION, sesion);
    }
    //Busca el producto; si no existe, error
    public void agregarProducto(Venta venta, String codigo, int cantidad){
        Producto producto = inventarioBarra.buscarProducto(codigo);
        if(producto == null){
            throw new IllegalArgumentException("No existe producto con codigo : " + codigo);
        }
        // le agrega a la venta un renglon con este producto y esta cantidad
        venta.agregarItem(producto,cantidad);


    }
    // venta de CONTADO: se confirma y se paga en el mismo momento.
    // Recibe un MetodoPago: puede ser Efectivo, TarjetaCredito o PagoDigital (polimorfismo).
    public void confirmar(Venta venta, MetodoPago metodo){
        venta.confirmar(metodo);
        // solo se guarda si se confirmo y pago bien
        ventas.add(venta);
    }
    // venta con CARGO_SESION: no se paga ahora, se paga al finalizar la sesion.
    // Sobrecarga: mismo nombre, distintos parametros; Java elige cual usar.
    public void confirmar(Venta venta){
        confirmar(venta, null);
    }
    public ArrayList<Venta> listar(){
        return new ArrayList<>(ventas);
    }
    public int cantidad(){
        return ventas.size();
    }


}

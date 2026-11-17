package modelos;

import interfaces.MetodoPago;

import java.time.LocalDateTime;

public class Inscripcion {
    //todos final porque nunca se va a cambiar una vez inscritos
    private final Cliente cliente;
    private final LocalDateTime fecha;
    //Se utiliza montoPagado en vez de costoInscripcion por si el costo del torneo cambia,
    //solo se realiza el pago del dia en el que fue inscrito, lo pagado ya no cambia
    private final double montoPagado;
    //final: con que se pago ya es un hecho que ya ocurrio
    private final MetodoPago metodoPago;

    public Inscripcion(Cliente cliente, double montoPagado, MetodoPago metodoPago) {
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
    }

    //Sobrecarga para cargar desde el archivo: recibe la fecha que se guardo.
    //El metodo de pago no se guarda en el archivo, por eso queda null
    public Inscripcion(Cliente cliente, double montoPagado, LocalDateTime fecha) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.montoPagado = montoPagado;
        this.metodoPago = null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    @Override
    public String toString() {
        //getNombre() para no imprimir el cliente completo (documento, telefono, correo)
        String texto = "[INSCRIPCION] | CLIENTE: " + cliente.getNombre() + " | Fecha: " + fecha + " | Pagado: " + montoPagado;
        //las cargadas del archivo no tienen metodo de pago, asi no se cae con null
        if (metodoPago != null) {
            texto = texto + " | " + metodoPago.obtenerComprobante();
        }
        return texto;
    }
}

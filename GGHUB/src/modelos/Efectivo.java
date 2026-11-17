package modelos;

import interfaces.MetodoPago;
import servicios.ValidadorDatos;

public class Efectivo implements MetodoPago {
    private final double montoRecibido;
    private double montoAPagar;
    private double cambio;
    // un pago se usa una sola vez: si se reutilizara, se borraria el comprobante del primero
    private boolean usado;

    public Efectivo (double montoRecibido){
        if (!ValidadorDatos.precioValido(montoRecibido)){
            throw  new IllegalArgumentException("el efectivo debe ser mayor a 0");
        }
        this.montoRecibido = montoRecibido;
    }

    public double getCambio(){
        return cambio;
    }
    @Override
    public void pagar(double monto){
        if (usado) {
            throw new IllegalArgumentException("Este pago en efectivo ya fue utilizado");
        }
        // un monto negativo daria un "cambio" mayor a lo que entrego el cliente
        if (ValidadorDatos.validadorNegativo(monto)) {
            throw new IllegalArgumentException("El monto a pagar no puede ser negativo");
        }
        if(montoRecibido < monto){
            double faltante = monto-montoRecibido;
            throw  new IllegalArgumentException("efectivo insuficiente falta: $" + faltante  );
        }
        montoAPagar = monto;
        cambio = montoRecibido-monto;
        // se marca al final: si algo fallo arriba, el pago se puede intentar de nuevo
        usado = true;


    }
    @Override
    public String obtenerComprobante(){
        return "Efectivo | " + "pagado: $" + montoAPagar + " | "+ "recibido: $" + montoRecibido + " | cambio: $" + cambio;
    }
}

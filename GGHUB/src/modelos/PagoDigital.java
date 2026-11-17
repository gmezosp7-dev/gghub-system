package modelos;

import interfaces.MetodoPago;
import servicios.ValidadorDatos;

// Pago por billetera digital (Nequi, Daviplata...): mismo contrato MetodoPago,
// otra forma de pagar. Polimorfismo.
public class PagoDigital implements MetodoPago {
    private final String plataforma;
    private final String celular;
    private double montoPagado;
    // un pago se usa una sola vez: si se reutilizara, se borraria el comprobante del primero
    private boolean usado;

    public PagoDigital(String plataforma, String celular) {
        if (!ValidadorDatos.textoValido(plataforma)) {
            throw new IllegalArgumentException("La plataforma de pago es obligatoria ");
        }
        // celular colombiano: exactamente 10 digitos
        if (celular == null || !celular.matches("\\d{10}")) {
            throw new IllegalArgumentException("El celular debe tener exactamente 10 digitos");
        }
        this.plataforma = plataforma;
        this.celular = celular;
    }

    @Override
    public void pagar(double monto) {
        if (usado) {
            throw new IllegalArgumentException("Este pago digital ya fue utilizado");
        }
        // 0 se acepta: una sesion de menos de un minuto sin consumos cuesta $0
        if (ValidadorDatos.validadorNegativo(monto)) {
            throw new IllegalArgumentException("El monto a pagar no puede ser negativo");
        }
        montoPagado = monto;
        usado = true;
    }

    @Override
    public String obtenerComprobante() {
        // se ocultan los digitos del medio del celular en el comprobante
        String celularOculto = celular.substring(0, 3) + "****" + celular.substring(7);
        return "Pago digital (" + plataforma + ") " + celularOculto + " | pagado: $" + montoPagado;
    }
}

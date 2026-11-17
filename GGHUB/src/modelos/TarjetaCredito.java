package modelos;

import interfaces.MetodoPago;
import servicios.ValidadorDatos;


public class TarjetaCredito implements MetodoPago {
    // por seguridad solo se guardan los ultimos 4 digitos, nunca el numero completo
    private final String ultimosDigitos;
    private final int cuotas;
    private double montoPagado;

    public TarjetaCredito(String ultimosDigitos, int cuotas) {
        // exactamente 4 digitos (como PATRON_DOCUMENTO, pero con {4})
        if (ultimosDigitos == null || !ultimosDigitos.matches("\\d{4}")) {
            throw new IllegalArgumentException("La tarjeta debe tener exactamente 4 digitos finales");
        }
        if (cuotas < 1 || cuotas > 36) {
            throw new IllegalArgumentException("Las cuotas deben estar entre 1 y 36");
        }
        this.ultimosDigitos = ultimosDigitos;
        this.cuotas = cuotas;
    }

    @Override
    public void pagar(double monto) {
        // 0 se acepta: una sesion de menos de un minuto sin consumos cuesta $0
        if (ValidadorDatos.validadorNegativo(monto)) {
            throw new IllegalArgumentException("El monto a pagar no puede ser negativo");
        }
        montoPagado = monto;
    }

    @Override
    public String obtenerComprobante() {
        return "Tarjeta de credito ****" + ultimosDigitos + " | pagado: $" + montoPagado + " | " + cuotas + " cuotas";
    }
}

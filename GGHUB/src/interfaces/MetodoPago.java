package interfaces;

public interface MetodoPago {
    void pagar(double monto);
    String obtenerComprobante();
}

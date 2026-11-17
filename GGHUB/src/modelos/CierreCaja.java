package modelos;

import servicios.ValidadorDatos;

import java.time.LocalDate;


// Solo GUARDA y MUESTRA los totales ya calculados quien los calcula es el gestor de caja.
public class CierreCaja {
    // todos final: un cierre es el registro de algo que ya paso, nadie puede retocar los numeros
    private final LocalDate fecha;
    private final String operador;
    private final double totalJuego;
    private final double totalControles;
    private final double totalBarra;
    private final double totalTorneos;
    private final int cantidadSesiones;
    private final int cantidadVentas;
    private final int cantidadInscripciones;


    public CierreCaja(String operador, double totalJuego, double totalControles, double totalBarra,
                      double totalTorneos, int cantidadSesiones, int cantidadVentas, int cantidadInscripciones) {
        if (!ValidadorDatos.textoValido(operador)){
            throw new IllegalArgumentException("El operador es obligatorio");
        }
        this.fecha = LocalDate.now();
        this.operador = operador;
        this.totalJuego = totalJuego;
        this.totalControles = totalControles;
        this.totalBarra = totalBarra;
        this.totalTorneos = totalTorneos;
        this.cantidadSesiones = cantidadSesiones;
        this.cantidadVentas = cantidadVentas;
        this.cantidadInscripciones = cantidadInscripciones;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getOperador() {
        return operador;
    }

    public double getTotalJuego() {
        return totalJuego;
    }

    public double getTotalControles() {
        return totalControles;
    }

    public double getTotalBarra() {
        return totalBarra;
    }

    public double getTotalTorneos() {
        return totalTorneos;
    }

    public int getCantidadSesiones() {
        return cantidadSesiones;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public int getCantidadInscripciones() {
        return cantidadInscripciones;
    }

    // la suma de los 4 rubros del RF-12; se escribe una sola vez y la usa generarResumen()
    public double calcularTotal(){
        return totalJuego + totalControles + totalBarra + totalTorneos;
    }

    public String generarResumen(){
        return "====== Cierre de caja " + fecha + " | Operador: " + operador + " ======\n" +
                "Tiempo de juego: $" + totalJuego + "\n" +
                "Controles extra: $" + totalControles + "\n" +
                "Ventas de barra: $" + totalBarra + "\n" +
                "Torneos:         $" + totalTorneos + "\n" +
                "TOTAL RECAUDADO: $" + calcularTotal() + "\n" +
                "Sesiones: " + cantidadSesiones + " | Ventas de contado: " + cantidadVentas
                + " | Inscripciones: " + cantidadInscripciones;
    }

    // asi el cierre se puede imprimir directo con println
    @Override
    public String toString(){
        return generarResumen();
    }
}

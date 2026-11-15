package modelos;

import java.time.LocalDateTime;
import servicios.ValidadorDatos;

public class Torneo {
    private final String nombre;        //final porque es la identificación del torneo, no cambia
    private String videojuego;
    private LocalDateTime fecha;
    private int cupoMaximo;
    private double costoInscripcion;
    private ModalidadTorneo modalidad;  //obtiene la modadlidad de la clase padre que proviene de cualquiera de las dos hijas
    private boolean inscripcionesAbiertas;

    // instancia cada uno de los atributos del cliente
    public Torneo(String nombre, String videojuego, LocalDateTime fecha, int cupoMaximo, double costoInscripcion, ModalidadTorneo modalidad) {
        this.nombre = nombre;
        this.videojuego = videojuego;
        this.fecha = fecha;
        this.cupoMaximo = cupoMaximo;
        this.costoInscripcion = costoInscripcion;
        this.modalidad = modalidad;
        this.inscripcionesAbiertas = true;
    }

    public String getNombre() {
        return nombre;
    }

    public String getVideojuego() {
        return videojuego;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public double getCostoInscripcion() {
        return costoInscripcion;
    }

    public ModalidadTorneo getModalidad() {
        return modalidad;
    }

    public boolean isInscripcionesAbiertas() {
        return inscripcionesAbiertas;
    }

    @Override
    public String toString() {
        return "Torneo{" +
                "nombre='" + nombre + '\'' +
                ", videojuego='" + videojuego + '\'' +
                ", fecha=" + fecha +
                ", cupoMaximo=" + cupoMaximo +
                ", costoInscripcion=" + costoInscripcion +
                ", modalidad=" + modalidad.getNombre() +
                ", inscripcionesAbiertas=" + inscripcionesAbiertas +
                '}';
    }

    //se aplica encapsulamiento para comprobar si está bien armado el torneo y no sea responsabilidad de otra clase

    public void validar() {
        //final no impide que el nombre llegue null o vacio; primero null para no hacer null.isBlank()
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El torneo debe tener un nombre");
        }
        if (modalidad == null) {
            throw new IllegalArgumentException("El torneo debe tener una modalidad");
        }
        if (fecha == null || fecha.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del torneo es obligatoria y no puede ser anterior a la actual");
        }
        if (cupoMaximo < 2) {
            throw new IllegalArgumentException("El cupo maximo debe ser de al menos 2 participantes, se recibio: " + cupoMaximo);
        }
        if (!ValidadorDatos.precioValido(costoInscripcion)) {
            throw new IllegalArgumentException("Costo de Inscripcion invalido");
        }
    }
}


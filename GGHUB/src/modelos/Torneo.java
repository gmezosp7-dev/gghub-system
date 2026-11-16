package modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;

import servicios.ValidadorDatos;

public class Torneo {
    private final String nombre;        //final porque es la identificación del torneo, no cambia
    private String videojuego;
    private LocalDateTime fecha;
    private int cupoMaximo;
    private double costoInscripcion;
    private ModalidadTorneo modalidad;  //tipo de la clase padre para poder guardar cualquiera de las dos modalidades (polimorfismo)
    private boolean inscripcionesAbiertas;
    private ArrayList<Inscripcion> inscripciones = new ArrayList<>();

    //inicializa los atributos del torneo, empieza con inscripciones abiertas y sin inscritos
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

    //Compara el documento con los que ya están para evitar repetidos
    public boolean estaInscrito(String documento){
        for (Inscripcion i : inscripciones){
            if (documento.equals(i.getCliente().getDocumento())){
                return true;
            }
        }
        return false;
    }

    public int cantidadInscritos(){
        return inscripciones.size();
    }

    public boolean estaLleno(){
        if(inscripciones.size() >= cupoMaximo){
            return true;
        }
        return false;
    }

    //Antes de realizar una inscripcion revisa si está abierto, si hay cupos y si hay algún error con el documento;
    //si pasa los if, se inscribe el cliente
    public Inscripcion inscribir(Cliente cliente){
        if(!inscripcionesAbiertas){
            throw new IllegalArgumentException("Las inscripciones del torneo " + getNombre() + " estan cerradas");
        }
        if (estaInscrito(cliente.getDocumento())){
            throw new IllegalArgumentException("El cliente con el documento: " + cliente.getDocumento() + " ya esta inscrito en: " + getNombre());
        }
        if (estaLleno()){
            throw new IllegalArgumentException("El torneo " + getNombre() + " esta lleno, el cupo maximo es de " + getCupoMaximo());
        }
        Inscripcion nuevaInscripcion = new Inscripcion(cliente, costoInscripcion);
        inscripciones.add(nuevaInscripcion);
        return nuevaInscripcion;
    }

    public ArrayList<Inscripcion> getInscripciones() {
        return new ArrayList<>(inscripciones);
    }
}

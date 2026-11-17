package modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;

import servicios.ValidadorDatos;

//Polimorfismo: Torneo no le pide nada a modalidad, le dice lo mismo a las dos y cada una reacciona como debe
public class Torneo {
    private final String nombre;        //final porque es la identificación del torneo, no cambia
    private String videojuego;
    private LocalDateTime fecha;
    private int cupoMaximo;
    private double costoInscripcion;
    private ModalidadTorneo modalidad;  //tipo de la clase padre para poder guardar cualquiera de las dos modalidades (polimorfismo)
    private boolean inscripcionesAbiertas;  //sin setter: solo se cierra con cerrarInscripciones()
    private ArrayList<Inscripcion> inscripciones = new ArrayList<>();   //cada torneo tiene sus propios inscritos
    private ArrayList<Partida> partidas = new ArrayList<>();            //vacia hasta que se generan los cruces

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
    public boolean estaInscrito(String documento) {
        for (Inscripcion i : inscripciones) {
            if (documento.equals(i.getCliente().getDocumento())) {
                return true;
            }
        }
        return false;
    }

    public int cantidadInscritos() {
        return inscripciones.size();
    }

    public boolean estaLleno() {
        if (inscripciones.size() >= cupoMaximo) {
            return true;
        }
        return false;
    }

    //valida todo primero (abierto, no repetido, hay cupo) y solo al final crea, agrega y devuelve,
    //para no dejar la lista a medias
    public Inscripcion inscribir(Cliente cliente) {
        if (!inscripcionesAbiertas) {
            throw new IllegalArgumentException("Las inscripciones del torneo " + getNombre() + " estan cerradas");
        }
        if (estaInscrito(cliente.getDocumento())) {
            throw new IllegalArgumentException("El cliente con el documento: " + cliente.getDocumento() + " ya esta inscrito en: " + getNombre());
        }
        if (estaLleno()) {
            throw new IllegalArgumentException("El torneo " + getNombre() + " esta lleno, el cupo maximo es de " + getCupoMaximo());
        }
        Inscripcion nuevaInscripcion = new Inscripcion(cliente, costoInscripcion);
        inscripciones.add(nuevaInscripcion);
        return nuevaInscripcion;
    }

    public ArrayList<Inscripcion> getInscripciones() {
        return new ArrayList<>(inscripciones);
    }

    //Encapsulamiento: en vez de usar un set, se usa un metodo que no expone el dato, sino una acción con reglas
    //por lo que solo permite cerrar, nunca volver a abrir
    public void cerrarInscripciones() {
        if (!inscripcionesAbiertas) {
            throw new IllegalArgumentException("Las inscripciones del torneo: " + getNombre() + " ya estan cerradas");
        }
        if (cantidadInscritos() < 2) {
            throw new IllegalArgumentException("se necesitan al menos 2 inscritos para cerrar el torneo " + nombre + ", hay: " + cantidadInscritos());
        }
        inscripcionesAbiertas = false;
    }

    //solo se puede generar con inscripciones cerradas y una sola vez (si no, se duplicarian las partidas)
    public ArrayList<Partida> generarCruces() {
        if (inscripcionesAbiertas) {
            throw new IllegalArgumentException("Primero se deben cerrar las inscripciones del torneo: " + getNombre());
        }
        if (!partidas.isEmpty()) {
            throw new IllegalArgumentException("Los cruces del torneo: " + getNombre() + " ya fueron generados");
        }
        //polimorfismo: sin preguntar que modalidad es, cada una genera los cruces a su manera
        partidas = modalidad.generarCruces(inscripciones);
        return getPartidas();
    }

    public ArrayList<Partida> getPartidas() {
        return new ArrayList<>(partidas);
    }

    public void registrarResultado(int numeroPartida, String documentoGanador, String marcador){
        if (partidas.isEmpty()){
            throw new IllegalArgumentException("Todavia no se generaron los cruces del torneo: " + nombre);
        }
        if (numeroPartida < 1 || numeroPartida > partidas.size()){
            throw new IllegalArgumentException("No existe la partida numero: " + numeroPartida + ", el torneo tiene: " + partidas.size());
        }
        Partida partida = partidas.get(numeroPartida - 1);    //-1 porque el arbitro cuenta desde 1 y la lista desde 0
        partida.registrarResultado(documentoGanador, marcador);
        //Polimorfismo: addAll agrega todos los elementos de una lista a otra. En eliminación directa pueden ser las
        //partidas de la ronda siguiente, y en grupos siempre es una lista vacía, así que no agrega nada.
        partidas.addAll(modalidad.procesarResultado(partidas));
    }

    public String obtenerPosiciones(){
        return modalidad.obtenerPosiciones();
    }

}

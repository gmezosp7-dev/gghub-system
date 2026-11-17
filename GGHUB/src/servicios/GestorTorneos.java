package servicios;

import interfaces.MetodoPago;
import modelos.Cliente;
import modelos.Inscripcion;
import modelos.Torneo;
import modelos.Partida;

import java.util.ArrayList;

public class GestorTorneos {
    private ArrayList<Torneo> torneos = new ArrayList<>();
    //No cambia ya que al reemplazarlo, el gestor de torneos dejaría de ver los clientes
    private final GestorClientes gestorClientes;

    //recibe el mismo GestorClientes del Main; si se creara con new estaria vacio y no encontraria a nadie
    public GestorTorneos(GestorClientes gestorClientes) {
        this.gestorClientes = gestorClientes;
    }

    //recorre la lista de torneos buscando uno con ese nombre
    public Torneo buscarPorNombre(String nombre) {
        //t toma, uno por uno, cada torneo de la lista
        for (Torneo t : torneos) {
            //se compara ignorando mayusculas para que "Copa FIFA" y "copa fifa" cuenten como el mismo torneo
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        //si se recorrio toda la lista sin encontrarlo
        return null;
    }

    //agrega un torneo a la lista
    public void registrar(Torneo torneo) {
        torneo.validar();
        //comprueba que el torneo no exista en la lista
        if (buscarPorNombre(torneo.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un torneo con el nombre: " + torneo.getNombre());
        }
        torneos.add(torneo);
    }

    //devuelve una copia para que nadie pueda modificar la lista original
    public ArrayList<Torneo> listar() {
        return new ArrayList<>(torneos);
    }

    public int cantidad() {
        return torneos.size();
    }

    //Revisa que el Torneo y el documento del cliente existan y deja que el "Torneo" valide antes de inscribirlo
    public Inscripcion inscribirCliente(String nombreTorneo, String documento, MetodoPago metodoPago) {
        Torneo torneo = buscarTorneoExistente(nombreTorneo);
        Cliente cliente = gestorClientes.buscarPorDocumento(documento);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el documento: " + documento);
        }
        return torneo.inscribir(cliente, metodoPago);
    }

    //private porque solo se usa por otros metodos del gestor que buscan saber si el torneo existe
    private Torneo buscarTorneoExistente(String nombreTorneo) {
        Torneo torneo = buscarPorNombre(nombreTorneo);
        if (torneo == null) {
            throw new IllegalArgumentException("No existe un torneo con el nombre: " + nombreTorneo);
        }
        return torneo;
    }

    //cerrarInscripciones, generarCruces, registrarResultado y obtenerPosiciones: buscan el torneo
    //y le delegan la accion; las reglas las valida el Torneo
    public void cerrarInscripciones(String nombreTorneo) {
        Torneo torneo = buscarTorneoExistente(nombreTorneo);
        torneo.cerrarInscripciones();
    }

    public ArrayList<Partida> generarCruces(String nombreTorneo) {
        Torneo torneo = buscarTorneoExistente(nombreTorneo);
        return torneo.generarCruces();
    }

    //se encadena sin guardar el torneo en una variable porque solo se usa una vez
    public void registrarResultado(String nombreTorneo, int numeroPartida, String documentoGanador, String marcador){
        buscarTorneoExistente(nombreTorneo).registrarResultado(numeroPartida, documentoGanador, marcador);
    }

    public String obtenerPosiciones(String nombreTorneo){
         return buscarTorneoExistente(nombreTorneo).obtenerPosiciones();
    }

}

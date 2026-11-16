package servicios;

import modelos.Cliente;
import modelos.Inscripcion;
import modelos.Torneo;

import java.util.ArrayList;

public class GestorTorneos {
    private ArrayList<Torneo> torneos = new ArrayList<>();
    private GestorClientes gestorClientes;

    public GestorTorneos(GestorClientes gestorClientes) {
        this.gestorClientes = gestorClientes;
    }

    //recorre la lista de torneos buscando uno con ese nombre
    public Torneo buscarPorNombre(String nombre){
        //t toma, uno por uno, cada torneo de la lista
        for (Torneo t : torneos) {
            //se compara el nombre de ESE torneo con el nombre recibido (String -> .equals)
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        //si se recorrio toda la lista sin encontrarlo
        return null;
    }

    //agrega un torneo a la lista
    public void registrar(Torneo torneo){
        torneo.validar();
        //comprueba que el torneo no exista en la lista
        if (buscarPorNombre(torneo.getNombre()) != null){
            throw new IllegalArgumentException("Ya existe un torneo con el nombre: " + torneo.getNombre());
        }
        torneos.add(torneo);
    }

    //devuelve una copia para que nadie pueda modificar la lista original
    public ArrayList<Torneo> listar(){
        return new ArrayList<>(torneos);
    }

    public int cantidad(){
        return torneos.size();
    }

    //Revisa que el Torneo y el documento del cliente existan y deja que el "Torneo" valide antes de inscribirlo
    public Inscripcion inscribirCliente(String nombreTorneo, String documento){
        Torneo torneo = buscarPorNombre(nombreTorneo);
        if (torneo == null){
            throw new IllegalArgumentException("No existe un torneo con el nombre: " + nombreTorneo);
        }
        Cliente cliente = gestorClientes.buscarPorDocumento(documento);
        if (cliente == null){
            throw new IllegalArgumentException("No existe un cliente con el documento: " + documento);
        }
        return torneo.inscribir(cliente);
    }
}

package servicios;

import modelos.Torneo;

import java.util.ArrayList;

public class GestorTorneos {
    private ArrayList<Torneo> torneos = new ArrayList<>();


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
}

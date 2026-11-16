package servicios;

import enums.EstadoPuesto;
import modelos.PuestoJuego;

import java.util.ArrayList;

public class GestorPuestos {
    //guardado dentro de array puestos de juego pc y consola
    private ArrayList<PuestoJuego> puestos = new ArrayList<>();
    // busqueda por id
    public PuestoJuego buscarPorId(int id){
        //recorrido lista puestos para encontrar si existen ids iguales
        for(PuestoJuego e: puestos){
            if(e.getId() == (id)){
                return e;

            }

        }
        // si no se encuentra la similitud se retorna null
        return null;
    }
    //registrar puestos de juego recibe estacionpc o estacionconsola
    public void registrar(PuestoJuego puestoJuego){
        //si se introduce un numero invalido:
        puestoJuego.validar();
        //pide el id y recorre la lista buscando el id
        if(buscarPorId(puestoJuego.getId()) != null ){
            //si ya existe un puesto con ese id
            throw new IllegalArgumentException("id ya repetido");

        }

        puestos.add(puestoJuego);
    }

    //retorno el tamaño de elementos de la lista
    public int cantidad(){
        return puestos.size();
    }
    //otorga una copia de la lista original para mostrar objetos creados
    public ArrayList<PuestoJuego> listar(){
        return new ArrayList<>(puestos);
    }

    // Regla: EN_USO y DISPONIBLE los ponen las sesiones (GestorSesiones);
    // el mantenimiento solo entra por estos dos metodos, que revisan antes de cambiar.
    public void ponerEnMantenimiento(int id){
        PuestoJuego puesto = buscarPorId(id);
        if (puesto == null){
            throw new IllegalArgumentException("No existe un puesto de juego con el id: " + id);
        }
        // no se le quita la estacion a alguien que esta jugando
        if (!puesto.estaDisponible()){
            throw new IllegalArgumentException("Solo se puede poner en mantenimiento una estacion disponible; el puesto "
                    + id + " esta en: " + puesto.getEstado());
        }
        puesto.cambiarEstado(EstadoPuesto.EN_MANTENIMIENTO);
    }

    public void quitarMantenimiento(int id){
        PuestoJuego puesto = buscarPorId(id);
        if (puesto == null){
            throw new IllegalArgumentException("No existe un puesto de juego con el id: " + id);
        }
        if (puesto.getEstado() != EstadoPuesto.EN_MANTENIMIENTO){
            throw new IllegalArgumentException("El puesto " + id + " no esta en mantenimiento");
        }
        puesto.cambiarEstado(EstadoPuesto.DISPONIBLE);
    }
}





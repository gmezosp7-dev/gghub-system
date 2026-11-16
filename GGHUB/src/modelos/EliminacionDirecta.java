package modelos;

import java.util.ArrayList;

//Polimorfismo: implementa a su manera los metodos abstractos de ModalidadTorneo
public class EliminacionDirecta extends ModalidadTorneo{
    @Override
    public String getNombre() {
        return "Eliminacion Directa";
    }


    @Override
    public ArrayList<Partida> generarCruces(ArrayList<Inscripcion> inscripciones){
        ArrayList<Partida> partidas = new ArrayList<>();
        for (int i = 0; i < inscripciones.size(); i+=2) {   //Avanza de a dos porque cada partida tiene dos jugadores
            Cliente jugador1 = inscripciones.get(i) .getCliente();   //Se saca el primer jugador de la lista
            Cliente jugador2 = null;
            if (i + 1 < inscripciones.size()){
                jugador2 = inscripciones.get(i + 1).getCliente();
            }
            partidas.add(new Partida(1, jugador1, jugador2)); //Se agrega la partida; si el jugador2 es null es pase directo
        }
        return partidas;
    }


}

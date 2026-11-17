package modelos;

import java.util.ArrayList;

//Polimorfismo: implementa a su manera los metodos abstractos de ModalidadTorneo
public class EliminacionDirecta extends ModalidadTorneo{
    private Cliente campeon = null;     //se llena cuando termina la final

    @Override
    public String getNombre() {
        return "Eliminacion Directa";
    }


    @Override
    public ArrayList<Partida> generarCruces(ArrayList<Inscripcion> inscripciones){
        //se sacan los clientes de las inscripciones para poder usar emparejar
        ArrayList<Cliente> clientes = new ArrayList<>();
        for (Inscripcion ins : inscripciones){      //a cada inscripcion se pide el cliente y se agrega a la lista
            clientes.add(ins.getCliente());
        }
        return emparejar(clientes, 1);
    }

    //private: ayudante interno. Lo usan generarCruces (ronda 1) y procesarResultado (rondas siguientes),
    //asi el codigo de las parejas existe una sola vez
    private ArrayList<Partida> emparejar(ArrayList<Cliente> jugadores, int ronda){
        ArrayList<Partida> partidas = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i+=2) {   //Avanza de a dos porque cada partida tiene dos jugadores
            Cliente jugador1 = jugadores.get(i);   //Se saca el primer jugador de la lista
            Cliente jugador2 = null;
            if (i + 1 < jugadores.size()){
                jugador2 = jugadores.get(i + 1);
            }
            partidas.add(new Partida(ronda, jugador1, jugador2)); //Se agrega la partida; si el jugador2 es null es pase directo
        }
        return partidas;
    }

    //cuando toda la ronda tiene ganador, los ganadores avanzan a la siguiente ronda; si queda uno, es el campeon
    @Override
    public ArrayList<Partida> procesarResultado(ArrayList<Partida> partidas) {
        //sin partidas no hay ronda que revisar
        if (partidas.isEmpty()){
            return new ArrayList<>();
        }
        //la ronda que se esta jugando es la de la ultima partida (las rondas nuevas se agregan al final)
        int rondaJugada = partidas.get(partidas.size() - 1).getRonda();
        ArrayList<Cliente> ganadores = new ArrayList<>();
        for (Partida p: partidas){
            if (rondaJugada == p.getRonda()){
                //si falta alguna partida de esa ronda, todavia no se arma la siguiente
                if (!p.estaJugada()){
                    return new ArrayList<>();
                } else{
                    //los pases directos ya tienen ganador desde el constructor, entran solos
                    ganadores.add(p.getGanador());
                }
            }
        }
        //queda uno solo, ya es ganador y no hay mas rondas
        if (ganadores.size() == 1){
            campeon = ganadores.get(0);
            return new ArrayList<>();
            //sino, los ganadores se enfrentan en la siguiente ronda
        } else {
            return emparejar(ganadores, rondaJugada +1);
        }
    }

    //en eliminacion directa no hay tabla: solo importa si ya hay campeon
    @Override
    public String obtenerPosiciones() {
        if (campeon == null){
            return "Eliminacion directa en curso";
        } else {
            return "El campeon es: " + campeon.getNombre();
        }
    }
}

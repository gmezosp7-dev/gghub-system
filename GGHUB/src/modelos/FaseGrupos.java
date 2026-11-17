package modelos;

import java.util.ArrayList;

//Polimorfismo: implementa a su manera los metodos abstractos de ModalidadTorneo
public class FaseGrupos extends ModalidadTorneo{
    private ArrayList<PosicionGrupo> tabla = new ArrayList<>();    //tabla de posiciones; se recalcula en procesarResultado

    @Override
    public String getNombre(){
        return "Fase de Grupos";
    }

    @Override
    public ArrayList<Partida> generarCruces(ArrayList<Inscripcion> inscripciones){
        ArrayList<Partida> partidas = new ArrayList<>();
        for (int i = 0; i < inscripciones.size(); i++) {
            for (int j = i + 1; j <inscripciones.size() ; j++) {        //empieza después del i para no enfrentar jugadores consigo mismos ni repetir parejas
                partidas.add(new Partida(1,inscripciones.get(i).getCliente(), inscripciones.get(j).getCliente()));
            }
        }
        return partidas;
    }

    //busca la fila de ese cliente, si no tiene se la crea
    private PosicionGrupo buscarPosicion(Cliente cliente){
        for (PosicionGrupo pos : tabla) {
            //se compara por documento porque es la identificacion
            if (pos.getCliente().getDocumento().equals(cliente.getDocumento())){
                return pos;
            }
        }
        PosicionGrupo nueva = new PosicionGrupo(cliente);
        tabla.add(nueva);
        return nueva;
    }

    //recalcula la tabla desde cero con todas las partidas, asi nunca suma dos veces el mismo resultado
    @Override
    public ArrayList<Partida> procesarResultado(ArrayList<Partida> partidas){
        tabla = new ArrayList<>();
        for (Partida p : partidas) {
            //se llama para que los dos jugadores tengan la fila, aunque no hayan ganado
            buscarPosicion(p.getJugador1());
            buscarPosicion(p.getJugador2());
            if (p.estaJugada()){
                buscarPosicion(p.getGanador()).sumarVictorias();
            }
        }
        //en fase de grupos nunca hay partidas nuevas: todas se crearon al generar los cruces
        return new ArrayList<>();
    }

    @Override
    public String obtenerPosiciones() {
        if (tabla.isEmpty()){
            return "Fase de grupos sin resultado";
        }
        //ordena de mayor a menor en puntos; si b tiene mas puntos que a, b va primero
        tabla.sort((a, b) -> b.getPuntos() - a.getPuntos());
        String texto = "";
        for (int i = 0; i < tabla.size(); i++) {
            //i +1 porque las posiciones en la tabla empiezan en 1 no en 0
            texto = texto + (i + 1) + ". " + tabla.get(i) + "\n";
        }
        return texto;
    }



}

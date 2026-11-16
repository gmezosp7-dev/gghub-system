package modelos;

import java.util.ArrayList;

//Polimorfismo: implementa a su manera los metodos abstractos de ModalidadTorneo
public class FaseGrupos extends ModalidadTorneo{
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
}

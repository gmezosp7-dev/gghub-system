package modelos;

import java.util.ArrayList;

//abstracta: no existe "una modalidad" a secas, siempre es eliminacion directa o fase de grupos
public abstract class ModalidadTorneo {

    //abstracto: cada hija devuelve su propio nombre
    public abstract String getNombre();

    //abstracto: obliga a cada hija a generar los cruces con sus propias reglas (sin if ni switch)
    public abstract ArrayList<Partida> generarCruces(ArrayList<Inscripcion> inscripciones);

    //abstracto: procesa todas las partidas del torneo y devuelve las partidas que hay que agregar, si no hay ninguna devuelve la lista vacia
    public abstract ArrayList<Partida> procesarResultado(ArrayList<Partida> partidas);

    //abstracto: para ver como va el torneo de cada modalidad
    public abstract String obtenerPosiciones();

}

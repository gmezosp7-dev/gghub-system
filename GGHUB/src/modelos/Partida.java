package modelos;
//Quien contra quien, en que ronda y quien ganó

public class Partida {
    private final int ronda;        //la ronda en la que se juega no cambia
    private final Cliente jugador1; //Quienes se enfrentan no cambia
    private final Cliente jugador2; //Puede ser null porque el total de jugadores puede ser impar por lo que el ultimo no tendrá enfrentamiento
    private Cliente ganador;        //Se llena cuando se registra el resultado en RF-11
    private String marcador;        //null hasta que se juega; es texto porque cada juego cuenta distinto (goles, rondas...)

    //Inicializa la ronda con sus jugadores y si no hay jugador2, gana directamente el jugador1
    public Partida(int ronda, Cliente jugador1, Cliente jugador2) {
        this.ronda = ronda;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        if (jugador2 == null){
            this.ganador = jugador1;
        }else{
            this.ganador = null;
        }
    }

    public int getRonda() {
        return ronda;
    }

    public Cliente getJugador1() {
        return jugador1;
    }

    public Cliente getJugador2() {
        return jugador2;
    }

    public Cliente getGanador() {
        return ganador;
    }

    public String getMarcador() {
        return marcador;
    }

    public boolean esPaseDirecto(){
        if (jugador2 == null){
            return true;
        }
        return false;
    }

    //Verifica si ya hay ganador
    public boolean estaJugada(){
        if (this.ganador != null){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        //si no hay rival no se puede hacer jugador2.getNombre() (jugador2 es null)
        if (esPaseDirecto()) {
            return "[PARTIDA] | Ronda " + ronda + " | " + jugador1.getNombre() + " pasa directo";
        }
        //se usa getNombre() para no imprimir el cliente completo (documento, telefono, correo)
        String texto = "[PARTIDA] | Ronda " + ronda + " | " + jugador1.getNombre() + " vs " + jugador2.getNombre();
        //si ya se jugo, se le agrega el resultado al final
        if (estaJugada()) {
            texto = texto + " | " + marcador + " | Gana: " + ganador.getNombre();
        }
        return texto;
    }

    //Lo registra la partida porque es quien sabe quienes juegan. Valida todo primero y al final
    //asigna ganador y marcador; no hay empates
    public void registrarResultado(String documentoGanador, String marcador){
        //variable local: el atributo no se toca hasta saber que el ganador es uno de los dos jugadores
        Cliente elGanador = null;
        if(esPaseDirecto()){
            throw new IllegalArgumentException("La partida es un pase directo, no se juega");
        }
        //no se puede cambiar un resultado: el ganador ya pudo haber avanzado de ronda
        if (estaJugada()){
            throw new IllegalArgumentException("La partida ya tiene resultado");
        }
        if (marcador == null || marcador.isBlank()){
            throw new IllegalArgumentException("El marcador esta en blanco");
        }
        if (jugador1.getDocumento().equals(documentoGanador)){
            elGanador = jugador1;
        } else if (jugador2.getDocumento().equals(documentoGanador)) {
            elGanador = jugador2;
        }else{
            throw new IllegalArgumentException("El cliente con el documento: " + documentoGanador + " no juega en esta partida");
        }
        this.marcador = marcador;
        this.ganador = elGanador;
    }
}

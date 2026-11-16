package modelos;
//Quien contra quien, en que ronda y quien ganó

public class Partida {
    private final int ronda;        //la ronda en la que se juega no cambia
    private final Cliente jugador1; //Quienes se enfrentan no cambia
    private final Cliente jugador2; //Puede ser null porque el total de jugadores puede ser impar por lo que el ultimo no tendrá enfrentamiento
    private Cliente ganador;        //Se llena cuando se registra el resultado en RF-11

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
        return "[PARTIDA] | Ronda " + ronda + " | " + jugador1.getNombre() + " vs " + jugador2.getNombre();
    }
}

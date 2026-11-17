package modelos;

//Una fila de la tabla de posiciones: un jugador con sus victorias.
//Reemplaza los arreglos participantes[] y puntos[] del UML: asi el cliente y sus victorias viajan juntos
public class PosicionGrupo {
    private final Cliente cliente;      //final: la fila siempre es del mismo jugador
    private int victorias;              //sin final: va subiendo con cada partida ganada
    //static final: regla fija del torneo, una sola copia para todas las filas; si cambia, se cambia aqui
    private static final int PUNTOS_POR_VICTORIA = 3;

    //recibe solo el cliente: toda fila nueva arranca sin victorias
    public PosicionGrupo(Cliente cliente) {
        this.cliente = cliente;
        this.victorias = 0;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public int getVictorias() {
        return victorias;
    }

    @Override
    public String toString() {
        //getNombre() para no imprimir el cliente completo; los puntos se calculan, no se guardan
        return cliente.getNombre() + " | " + victorias + " victorias | " + getPuntos() + " puntos";
    }

    //Encapsulamiento: en vez de un setVictorias(), solo se pueden sumar de a una (nunca negativas ni inventadas)
    public void sumarVictorias(){
        victorias++;
    }

    //los puntos no se guardan, se calculan: asi siempre cuadran con las victorias
    public int getPuntos(){
        return (victorias* PUNTOS_POR_VICTORIA);
    }
}

package modelos;


import enums.EstadoPuesto;
import servicios.ValidadorDatos;

public abstract class PuestoJuego {
    //se usa final para en un futuro no se pueda cambiar mediante un [ setid() ]
    private final int  id;
    private String modelo;
    private double tarifaHora;
    //estado actual del puesto; arranca DISPONIBLE y solo puede tomar los valores del enum EstadoPuesto
    private EstadoPuesto estado;
//constructor --->
    //protected: solo lo usan las subclases con super()
    protected PuestoJuego (int id, String modelo, double tarifaHora){
        this.estado = EstadoPuesto.DISPONIBLE;
        this.id = id;
        this.modelo= modelo;
        this.tarifaHora = tarifaHora;

    }
//metodos
    public int getId() {
        return id;
    }
    public String getModelo() {
        return modelo;
    }
    public double getTarifaHora() {
        return tarifaHora;
    }
    public EstadoPuesto getEstado(){
        return estado;
    }

    @Override
    public String toString(){
        return "id: [" +getId()+ "] | " + "modelo: "+ getModelo() +" | tarifa/h: " + getTarifaHora() + " | " + " Estado: " + getEstado() +" |";
    }
    public void validar (){
        if(!ValidadorDatos.precioValido(tarifaHora)){
            throw new IllegalArgumentException(("tarifa invalida"));

        }

    }
    //estadisponible
    public boolean estaDisponible(){
        //si esta disponible devuelve si
        if(estado == EstadoPuesto.DISPONIBLE){
            return true;

        }
        //de lo contrario retorna un false
        return false;
    }
    public void cambiarEstado(EstadoPuesto nuevoEstado){
        //no se puede colocar un estado vacio o null
        if(nuevoEstado== null){
            throw new IllegalArgumentException("el estado no puede ser nulo");
        }
        //constructor desde disponible
        this.estado = nuevoEstado;

    }
}

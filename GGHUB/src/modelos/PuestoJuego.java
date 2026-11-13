package modelos;


import servicios.ValidadorDatos;

public abstract class PuestoJuego {
    //se usa final para en un futuro no se pueda cambiar mediante un [ setid() ]
    private final int  id;
    private String modelo;
    private  double tarifaHora;
//constructor --->
    //protected para que solo los que estan en el paquete lo puedan heredar
    protected PuestoJuego (int id, String modelo, double tarifaHora){
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

    @Override
    public String toString(){
        return    "id: [" +getId()+"] | " + " modelo: "+ getModelo() +" | tarifa/h: " + getTarifaHora() + " | ";
    }
    public void validar (){
        if(!ValidadorDatos.precioValido(tarifaHora)){
            throw new IllegalArgumentException(("tarifa invalida"));

        }

    }
}

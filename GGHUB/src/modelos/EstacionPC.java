package modelos;

public class EstacionPC extends PuestoJuego {
    private String especifiaciones;
    // constructor ----> se realiza mediante la herencia de clase padre (PuestoJuego) y se heredan atributos
    //al constructor mediante palabra reservada "SUPER"

    public EstacionPC (int id, String modelo, double tarifaHora, String especifiaciones){
        super(id, modelo, tarifaHora);
        this.especifiaciones = especifiaciones;
    }
    public String getEspecifiaciones(){
        return especifiaciones;
    }
    @Override
    public String toString(){
        return "[Pc] "+  super.toString() + " Especifiaciones: "+ getEspecifiaciones() +" |";
    }



}

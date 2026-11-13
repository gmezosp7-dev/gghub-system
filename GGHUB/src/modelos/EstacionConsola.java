package modelos;


import servicios.ValidadorDatos;

// Herencia clase padre PuestoJuego mediante "extends"
public class EstacionConsola extends  PuestoJuego {
    private String tipoConsola;
    private double tarifaControlExtra;
    //constructor
    public EstacionConsola(int id, String modelo, double tarifaHora,String tipoConsola, double tarifaControlExtra){
        //mediante herencia se agrega constructor de clase padre ( PuestoJuego )
        super(id, modelo, tarifaHora);
        this.tipoConsola = tipoConsola;
        this.tarifaControlExtra = tarifaControlExtra;

    }

    public String getTipoConsola(){
        return tipoConsola;

    }
    public double getTarifaControlExtra(){
        return tarifaControlExtra;
    }
    @Override
    public String toString(){

        return  "[Consola] "+super.toString()+  " Tipo de consola: " + getTipoConsola() + " | Tarifa Control Extra: " + getTarifaControlExtra()+ " |";
    }

    @Override
    public void validar() {
        super.validar();
        if(ValidadorDatos.validadorNegativo(tarifaControlExtra)){
            throw new IllegalArgumentException(("tarifa control extra invalida"));

        }
    }
}

package modelos;

import servicios.ValidadorDatos;

public class Producto {
    //codigo final para q no se pueda cambiar
    private final String codigo;
    private String nombre;
    private String categoria;
    private double costo;
    private double precioVenta;
    private int stock;

    public Producto(String codigo, String nombre, String categoria, double costo, double precioVenta, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.costo = costo;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getCosto() {
        return costo;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getStock() {
        return stock;
    }
    public void setPrecioVenta(double nuevoPrecioVenta){
        if (nuevoPrecioVenta < 0){
            throw new IllegalArgumentException("El nuevo precio no puede ser negativo");
        }
        this.precioVenta = nuevoPrecioVenta;

    }
    public void agregarStock(int adiccionStock){
        if (adiccionStock <= 0){
            throw new IllegalArgumentException("el stock a adiccionar no puede ser negativo");
        }
        stock = stock + adiccionStock;
    }
    public void descontarStock(int cantidadDescontada){
        if (!ValidadorDatos.precioValido(cantidadDescontada)) {
            throw new IllegalArgumentException("la cantidad a descontar de stock debe de ser mayor a 0");
        }
        if(!hayStock(cantidadDescontada)){
            throw new IllegalArgumentException("stock insuficiente de "+ getNombre()+ " quedan: " + getStock() );
        }
        stock = stock - cantidadDescontada;
    }
    public boolean hayStock(int stockNecesitado){
       if (stock >= stockNecesitado){
           return true;

       }
       return false;

    }
    //validacion cantidades positivas
    public void validar(){
        // el codigo es la identidad del producto: no puede ser null ni vacio
        if(!ValidadorDatos.textoValido(codigo)){
            throw new IllegalArgumentException("El codigo del producto es obligatorio");
        }
        if(ValidadorDatos.validadorNegativo(precioVenta)){
            throw new IllegalArgumentException(("el precio de venta no puede ser negativo"));

        }
        if(ValidadorDatos.validadorNegativo(costo)){
            throw  new IllegalArgumentException("El costo no puede ser negativo");

        }
        if (ValidadorDatos.validadorNegativo(stock)){
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
    @Override
    public String toString(){
        return "["+ codigo +"]" + " | " + nombre +  " | " + categoria + " | " + "precio: " + "$" + precioVenta + " | " + "stock: " + stock;
    }
}

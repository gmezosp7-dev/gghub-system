package servicios;

import modelos.Producto;
import modelos.PuestoJuego;

import java.util.ArrayList;

public class InventarioBarra {
    private ArrayList<Producto> productos = new ArrayList<>();


    public Producto buscarProducto(String codigo){
        for(Producto e : productos){
            if(e.getCodigo().equals(codigo)){
                return e;
            }

        }
        return null;

    }
    public void registrarProducto(Producto producto){
        producto.validar();
        if (buscarProducto(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el codigo " + producto.getCodigo());
        }
        productos.add(producto);
    }
    public void actualizarPrecio(String codigo, double nuevoPrecio){
        Producto producto = buscarProducto(codigo);
        if(producto == null ){
            throw new IllegalArgumentException("El producto que se busca actualizar no existe");
        }
        producto.setPrecioVenta(nuevoPrecio);
    }
    public void reabastecer(String codigo, int cantidad){
        Producto producto = buscarProducto(codigo);
        if(producto== null){
            throw new IllegalArgumentException("No existe un producto a reabastecer");


        }
        producto.agregarStock(cantidad);

    }
    public int cantidad(){
        return productos.size();
    }
    //otorga una copia de la lista original para mostrar objetos creados
    public ArrayList<Producto> listar(){
        return new ArrayList<>(productos);
    }





}

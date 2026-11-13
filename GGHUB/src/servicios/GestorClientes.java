package servicios;
//usar clase cliente que esta en otro paquete
import modelos.Cliente;
// Funcionalidad listas propia de java
import java.util.ArrayList;


public class GestorClientes {
    //creaccion de array clientes
    private ArrayList<Cliente> clientes = new ArrayList<>();

    //metodos

    //registrar clientes
    public void registrar(Cliente cliente) {
        //validacion de documento
        if(!ValidadorDatos.documentoValido(cliente.getDocumento())){
            //si el doucmento no tiene caracteres correspondientes se usa una excepcion
            throw new IllegalArgumentException("DOCUMENTO NO VALIDO,CARACTER ERRONEO");

        }
        //el documento ya esta
        if (buscarPorDocumento(cliente.getDocumento()) != null ){
            // si ya esta no se agrega nuevamente y se devuelve que ya esta
            throw new IllegalArgumentException("EL DOCUMENTO: " + cliente.getDocumento() + " ya esta registrado");
        }
        //de lo contrario se agrega el nuevo cliente y se lanza que aun no existe y que se acaba de agregar
        clientes.add(cliente);



    }
    //devuleve cantidad de clientes que existen
    public int cantidad(){
        return clientes.size();
    }
    //busqueda por documento dentro de la lista
    public Cliente buscarPorDocumento(String documento){
        for(Cliente e : clientes){
            if (e.getDocumento().equals(documento)){
                return e;

            }

        }
        //devuelve null si no encuentra similitudes
        return null;


    }//devuelve copia de lista de los clientes
    public ArrayList<Cliente> listar() {
        return new ArrayList<>(clientes);
    }
}



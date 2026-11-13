import modelos.Cliente;
import modelos.EstacionConsola;
import modelos.EstacionPC;
import modelos.PuestoJuego;
import servicios.GestorClientes;
import servicios.GestorPuestos;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        GestorClientes gestorClientes = new GestorClientes();
        Cliente cliente = new Cliente("1029","pasco","321123343","marcopollo@gmail.com");
        Cliente cliente2 = new Cliente("10295","pasco7","34534543","vascoo@gmail.com");
        Cliente cliente3 = new Cliente("1029","perroflauta","676767676","asdascoo@gmail.com");
        Cliente cliente4 = new Cliente("1029A","perroflauta","676767676","asdascoo@gmail.com");
        intentarRegistrar(gestorClientes,cliente);
        intentarRegistrar(gestorClientes,cliente2);
        intentarRegistrar(gestorClientes,cliente3);
        intentarRegistrar(gestorClientes,cliente4);
        System.out.println(gestorClientes.cantidad());
        mostrarClientes(gestorClientes);
        GestorPuestos gestorPuestos = new GestorPuestos();
        EstacionConsola ps4 = new EstacionConsola(1,"ps4",-67.0,"playstation",1.0);
        EstacionPC pc1 = new EstacionPC(15,"HP" , 12.0, "RTX");
        EstacionPC pc2 = new EstacionPC(16,"ASUS", 9.5, "GTX");
        EstacionPC pc3= new EstacionPC(15,"LENOVO", 0,"gtx");
        EstacionConsola ps4Pro = new EstacionConsola(157,"ps4",12.0,"playstation",11.0);
        intentarRegistrarPuesto(gestorPuestos, ps4Pro);
        intentarRegistrarPuesto(gestorPuestos,pc1);
        intentarRegistrarPuesto(gestorPuestos, pc2);
        mostrarPuestos(gestorPuestos);
        cantidadPuestos(gestorPuestos);




    }
    private static void intentarRegistrar(GestorClientes gestor, Cliente cliente) {
        try {
            gestor.registrar(cliente);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

        }

    }
    private static void mostrarClientes(GestorClientes gestor){
        for (Cliente c : gestor.listar()){
            System.out.println(c);
        }
    }
    private static void intentarRegistrarPuesto(GestorPuestos gestorPuestos,PuestoJuego puestoJuego){
        try{
            gestorPuestos.registrar(puestoJuego);


        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
    private static void mostrarPuestos(GestorPuestos gestorPuestos){
        for (PuestoJuego z: gestorPuestos.listar()){
            System.out.println(z);

        }

    }
    private static void cantidadPuestos(GestorPuestos gestorPuestos){
        System.out.println(gestorPuestos.cantidad());

    }

}
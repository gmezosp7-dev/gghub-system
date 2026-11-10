import modelos.Cliente;
import servicios.GestorClientes;

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



    }
    private static void intentarRegistrar(GestorClientes gestor, Cliente cliente) {
        try {
            gestor.registrar(cliente);
            System.out.println(cliente.toString());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

        }

    }
    private static void mostrarClientes(GestorClientes gestor){
        for (Cliente c : gestor.listar()){
            System.out.println(c);
        }
    }
}
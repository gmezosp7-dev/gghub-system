package ui;

import modelos.Cliente;
import servicios.GestorClientes;

import java.util.ArrayList;
import java.util.Scanner;

//Menu de clientes (RF-01), igual que el de torneos
//Solo pide los datos y se los pasa al gestor, el que valida es el GestorClientes
public class MenuClientes {

    private GestorClientes gestorClientes;
    private Scanner scanner;

    //Recibe el gestor y el Scanner del menu principal para no crear otros nuevos
    public MenuClientes(GestorClientes gestorClientes, Scanner scanner) {
        this.gestorClientes = gestorClientes;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("      GESTION DE CLIENTES     ");
            System.out.println("==============================");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Buscar por documento");
            System.out.println("3. Listar clientes");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: registrarCliente();   break;
                case 2: buscarCliente();      break;
                case 3: listarClientes();     break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void registrarCliente() {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        System.out.print("Documento (solo numeros): ");
        String documento = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        try {
            Cliente cliente = new Cliente(documento, nombre, telefono, correo);
            gestorClientes.registrar(cliente);
            System.out.println("Cliente registrado: " + cliente);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscarCliente() {
        System.out.println("\n--- BUSCAR CLIENTE ---");
        System.out.print("Documento: ");
        String documento = scanner.nextLine();
        Cliente cliente = gestorClientes.buscarPorDocumento(documento);
        if (cliente == null) {
            System.out.println("No existe un cliente con el documento: " + documento);
            return;
        }
        System.out.println(cliente);
    }

    private void listarClientes() {
        System.out.println("\n--- CLIENTES ---");
        ArrayList<Cliente> clientes = gestorClientes.listar();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    //Si escriben algo que no es un numero devuelve -1 y no se cae

    private int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

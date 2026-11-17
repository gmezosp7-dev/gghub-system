package ui;

import modelos.EstacionConsola;
import modelos.EstacionPC;
import modelos.PuestoJuego;
import servicios.GestorPuestos;

import java.util.ArrayList;
import java.util.Scanner;

//Menu de estaciones (RF-02 y RF-03), igual que el de torneos
//Solo pide los datos y se los pasa al gestor, el que valida es el GestorPuestos
//Aqui solo se pone o se quita el mantenimiento, lo de en uso y disponible lo hacen las sesiones
public class MenuPuestos {

    private GestorPuestos gestorPuestos;
    private Scanner scanner;

    //Recibe el gestor y el Scanner del menu principal para no crear otros nuevos
    public MenuPuestos(GestorPuestos gestorPuestos, Scanner scanner) {
        this.gestorPuestos = gestorPuestos;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("    GESTION DE ESTACIONES     ");
            System.out.println("==============================");
            System.out.println("1. Registrar estacion");
            System.out.println("2. Ver estado de todas las estaciones");
            System.out.println("3. Buscar estacion por id");
            System.out.println("4. Poner en mantenimiento");
            System.out.println("5. Quitar mantenimiento");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: registrarEstacion();      break;
                case 2: listarEstaciones();       break;
                case 3: buscarEstacion();         break;
                case 4: ponerEnMantenimiento();   break;
                case 5: quitarMantenimiento();    break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void registrarEstacion() {
        System.out.println("\n--- REGISTRAR ESTACION ---");
        System.out.println("Tipo: 1. PC   2. Consola");
        System.out.print("Seleccione: ");
        int tipo = leerEntero();
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo no valido. Estacion no registrada.");
            return;
        }
        System.out.print("Id (numero): ");
        int id = leerEntero();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Tarifa por hora: ");
        double tarifa = leerDouble();
        try {
            //El if es por lo que escribe el usuario, no por el tipo de un objeto (eso si seria malo)
            PuestoJuego puesto;
            if (tipo == 1) {
                System.out.print("Especificaciones (ej: RTX 4060, 32GB): ");
                puesto = new EstacionPC(id, modelo, tarifa, scanner.nextLine());
            } else {
                System.out.print("Tipo de consola (ej: PLAYSTATION): ");
                String tipoConsola = scanner.nextLine();
                System.out.print("Tarifa por control extra: ");
                puesto = new EstacionConsola(id, modelo, tarifa, tipoConsola, leerDouble());
            }
            gestorPuestos.registrar(puesto);
            System.out.println("Estacion registrada: " + puesto);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarEstaciones() {
        System.out.println("\n--- ESTACIONES ---");
        ArrayList<PuestoJuego> puestos = gestorPuestos.listar();
        if (puestos.isEmpty()) {
            System.out.println("No hay estaciones registradas.");
            return;
        }
        for (PuestoJuego p : puestos) {
            System.out.println(p);
        }
    }

    private void buscarEstacion() {
        System.out.println("\n--- BUSCAR ESTACION ---");
        System.out.print("Id: ");
        int id = leerEntero();
        PuestoJuego puesto = gestorPuestos.buscarPorId(id);
        if (puesto == null) {
            System.out.println("No existe un puesto de juego con el id: " + id);
            return;
        }
        System.out.println(puesto);
    }

    private void ponerEnMantenimiento() {
        System.out.println("\n--- PONER EN MANTENIMIENTO ---");
        System.out.print("Id de la estacion: ");
        int id = leerEntero();
        try {
            gestorPuestos.ponerEnMantenimiento(id);
            System.out.println("La estacion " + id + " quedo en mantenimiento.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void quitarMantenimiento() {
        System.out.println("\n--- QUITAR MANTENIMIENTO ---");
        System.out.print("Id de la estacion: ");
        int id = leerEntero();
        try {
            gestorPuestos.quitarMantenimiento(id);
            System.out.println("La estacion " + id + " quedo disponible.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
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

    private double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

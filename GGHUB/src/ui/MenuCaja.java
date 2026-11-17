package ui;

import modelos.CierreCaja;
import servicios.GestorCaja;

import java.util.ArrayList;
import java.util.Scanner;

//Menu del cierre de caja (RF-12), igual que el de torneos
//Solo pide el operador y se lo pasa al GestorCaja, que es el que suma todo
public class MenuCaja {

    private GestorCaja gestorCaja;
    private Scanner scanner;

    //Recibe el gestor y el Scanner del menu principal para no crear otros nuevos
    public MenuCaja(GestorCaja gestorCaja, Scanner scanner) {
        this.gestorCaja = gestorCaja;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("        CIERRE DE CAJA        ");
            System.out.println("==============================");
            System.out.println("1. Hacer cierre de caja");
            System.out.println("2. Ver cierres anteriores");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: cerrarCaja();      break;
                case 2: listarCierres();   break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void cerrarCaja() {
        System.out.println("\n--- CIERRE DE CAJA ---");
        System.out.print("Nombre del operador: ");
        String operador = scanner.nextLine();
        try {
            CierreCaja cierre = gestorCaja.cerrarCaja(operador);
            System.out.println(cierre.generarResumen());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarCierres() {
        System.out.println("\n--- CIERRES ANTERIORES ---");
        ArrayList<CierreCaja> cierres = gestorCaja.listar();
        if (cierres.isEmpty()) {
            System.out.println("Todavia no se ha hecho ningun cierre.");
            return;
        }
        for (CierreCaja c : cierres) {
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

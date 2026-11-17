package ui;

import modelos.EstacionConsola;
import modelos.EstacionPC;
import servicios.GestorCaja;
import servicios.GestorClientes;
import servicios.GestorPuestos;
import servicios.GestorSesiones;
import servicios.GestorTorneos;
import servicios.GestorVentas;
import servicios.InventarioBarra;

import java.util.Scanner;

//Menu principal: desde aqui se abren todos los demas menus
//Tiene los submenus y a todos les pasa el mismo Scanner, asi no se crean varios leyendo el teclado
public class MenuPrincipal {

    private Scanner scanner;
    private MenuClientes menuClientes;
    private MenuPuestos menuPuestos;
    private MenuSesiones menuSesiones;
    private MenuBarra menuBarra;
    private MenuTorneos menuTorneos;
    private MenuCaja menuCaja;

    //aqui se crean todos los gestores, una sola vez, y a cada submenu se le pasa el suyo
    //Los que necesitan otro gestor lo reciben por constructor (el mismo objeto, no uno nuevo)
    public MenuPrincipal() {
        GestorClientes gestorClientes = new GestorClientes();
        GestorPuestos gestorPuestos = new GestorPuestos();
        GestorSesiones gestorSesiones = new GestorSesiones(gestorClientes, gestorPuestos);
        InventarioBarra inventarioBarra = new InventarioBarra();
        GestorVentas gestorVentas = new GestorVentas(inventarioBarra, gestorSesiones);
        GestorTorneos gestorTorneos = new GestorTorneos(gestorClientes);
        GestorCaja gestorCaja = new GestorCaja(gestorSesiones, gestorVentas, gestorTorneos);
        crearSala(gestorPuestos);

        this.scanner = new Scanner(System.in);
        this.menuClientes = new MenuClientes(gestorClientes, scanner);
        this.menuPuestos = new MenuPuestos(gestorPuestos, scanner);
        this.menuSesiones = new MenuSesiones(gestorSesiones, scanner);
        this.menuBarra = new MenuBarra(inventarioBarra, gestorVentas, scanner);
        this.menuTorneos = new MenuTorneos(gestorTorneos, scanner);
        this.menuCaja = new MenuCaja(gestorCaja, scanner);
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("            GG HUB            ");
            System.out.println("==============================");
            System.out.println("1. Clientes");
            System.out.println("2. Estaciones de juego");
            System.out.println("3. Sesiones de juego");
            System.out.println("4. Barra de snacks");
            System.out.println("5. Torneos");
            System.out.println("6. Cierre de caja");
            System.out.println("7. Demo con las pruebas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: menuClientes.mostrar();   break;
                case 2: menuPuestos.mostrar();    break;
                case 3: menuSesiones.mostrar();   break;
                case 4: menuBarra.mostrar();      break;
                case 5: menuTorneos.mostrar();    break;
                case 6: menuCaja.mostrar();       break;
                case 7: Demo.ejecutarDemo();      break;
                case 0: System.out.println("Hasta luego!"); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    //Crea la sala del README: PCs del 1 al 15 y consolas del 16 al 25 (la misma que habia en el Main)
    //Cuando esten los repositorios, esto se cambia por cargar las estaciones desde el archivo
    private void crearSala(GestorPuestos gestorPuestos) {
        for (int i = 1; i < 16; i++) {
            gestorPuestos.registrar(new EstacionPC(i, "ASUS", 10.0, "GTX"));
        }
        for (int i = 16; i < 26; i++) {
            gestorPuestos.registrar(new EstacionConsola(i, "PS4", 9.5, "PLAYSTATION", 1.0));
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

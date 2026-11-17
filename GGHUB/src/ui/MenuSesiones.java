package ui;

import interfaces.MetodoPago;
import modelos.Efectivo;
import modelos.PagoDigital;
import modelos.SesionJuego;
import modelos.TarjetaCredito;
import servicios.GestorSesiones;

import java.util.ArrayList;
import java.util.Scanner;

//Menu de sesiones (RF-04 y RF-05), igual que el de torneos
//Solo pide los datos y se los pasa al gestor, el que valida y cobra es el GestorSesiones
public class MenuSesiones {

    private GestorSesiones gestorSesiones;
    private Scanner scanner;

    //Recibe el gestor y el Scanner del menu principal para no crear otros nuevos
    public MenuSesiones(GestorSesiones gestorSesiones, Scanner scanner) {
        this.gestorSesiones = gestorSesiones;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("     SESIONES DE JUEGO        ");
            System.out.println("==============================");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Ver sesion activa de una estacion");
            System.out.println("3. Finalizar sesion y cobrar");
            System.out.println("4. Listar sesiones activas");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: iniciarSesion();          break;
                case 2: verSesionActiva();        break;
                case 3: finalizarSesion();        break;
                case 4: listarSesionesActivas();  break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void iniciarSesion() {
        System.out.println("\n--- INICIAR SESION ---");
        System.out.print("Documento del cliente: ");
        String documento = scanner.nextLine();
        System.out.print("Id de la estacion: ");
        int id = leerEntero();
        System.out.print("Controles extra (0 si no pide): ");
        int controles = leerEntero();
        try {
            SesionJuego sesion = gestorSesiones.iniciarSesion(documento, id, controles);
            System.out.println("Sesion iniciada: " + sesion);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void verSesionActiva() {
        System.out.println("\n--- SESION ACTIVA ---");
        System.out.print("Id de la estacion: ");
        int id = leerEntero();
        SesionJuego sesion = gestorSesiones.buscarSesionActiva(id);
        if (sesion == null) {
            System.out.println("No hay una sesion activa en el puesto " + id);
            return;
        }
        System.out.println(sesion);
        System.out.println("Consumos de barra cargados: $" + sesion.totalConsumos());
    }

    private void finalizarSesion() {
        System.out.println("\n--- FINALIZAR SESION ---");
        System.out.print("Id de la estacion: ");
        int id = leerEntero();
        //Primero se revisa que si haya sesion, asi no le pide el pago a alguien que no tiene
        SesionJuego activa = gestorSesiones.buscarSesionActiva(id);
        if (activa == null) {
            System.out.println("No hay una sesion activa en el puesto " + id);
            return;
        }
        System.out.println(activa);
        try {
            MetodoPago metodoPago = seleccionarMetodoPago();
            if (metodoPago == null) {
                System.out.println("Metodo de pago no valido. La sesion sigue abierta.");
                return;
            }
            //La sesion calcula el total y cobra, si el pago falla sale el error y la sesion sigue abierta
            SesionJuego sesion = gestorSesiones.finalizarSesion(id, metodoPago);
            System.out.println("Sesion finalizada. Total cobrado: $" + sesion.getTotal());
            System.out.println("  Tiempo: $" + sesion.getCostoTiempo() + " | Controles: $" + sesion.getCostoControles()
                    + " | Barra: $" + sesion.totalConsumos());
            System.out.println("  " + metodoPago.obtenerComprobante());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarSesionesActivas() {
        System.out.println("\n--- SESIONES ACTIVAS ---");
        ArrayList<SesionJuego> sesiones = gestorSesiones.listar();
        int activas = 0;
        for (SesionJuego s : sesiones) {
            if (s.estaActiva()) {
                System.out.println(s);
                activas++;
            }
        }
        if (activas == 0) {
            System.out.println("No hay sesiones activas.");
        }
    }

    //Crea el pago que escoja el usuario, cada tipo de pago revisa sus propios datos
    private MetodoPago seleccionarMetodoPago() {
        System.out.println("Metodo de pago: 1. Efectivo   2. Tarjeta de credito   3. Pago digital");
        System.out.print("Seleccione: ");
        int opcion = leerEntero();
        if (opcion == 1) {
            System.out.print("Monto recibido: ");
            return new Efectivo(leerDouble());
        }
        if (opcion == 2) {
            System.out.print("Ultimos 4 digitos de la tarjeta: ");
            String digitos = scanner.nextLine();
            System.out.print("Numero de cuotas: ");
            return new TarjetaCredito(digitos, leerEntero());
        }
        if (opcion == 3) {
            System.out.print("Plataforma (ej: Nequi): ");
            String plataforma = scanner.nextLine();
            System.out.print("Celular: ");
            return new PagoDigital(plataforma, scanner.nextLine());
        }
        return null;
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

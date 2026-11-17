package ui;

import interfaces.MetodoPago;
import modelos.Efectivo;
import modelos.EliminacionDirecta;
import modelos.FaseGrupos;
import modelos.Inscripcion;
import modelos.ModalidadTorneo;
import modelos.PagoDigital;
import modelos.Partida;
import modelos.TarjetaCredito;
import modelos.Torneo;
import servicios.GestorTorneos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

//Menu de consola de torneos, con la misma forma que los menus del profe.
//No tiene reglas de negocio: solo pide los datos y llama al GestorTorneos, que es quien valida
public class MenuTorneos {

    private GestorTorneos gestorTorneos;
    private Scanner scanner;

    //formato en el que se escribe la fecha del torneo, ej: 2026-11-28 18:00
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //recibe el mismo gestor y el mismo Scanner del menu principal
    public MenuTorneos(GestorTorneos gestorTorneos, Scanner scanner) {
        this.gestorTorneos = gestorTorneos;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("       GESTION DE TORNEOS     ");
            System.out.println("==============================");
            System.out.println("1. Crear torneo");
            System.out.println("2. Listar torneos");
            System.out.println("3. Inscribir cliente");
            System.out.println("4. Cerrar inscripciones");
            System.out.println("5. Generar cruces");
            System.out.println("6. Ver partidas");
            System.out.println("7. Registrar resultado");
            System.out.println("8. Ver posiciones");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: crearTorneo();          break;
                case 2: listarTorneos();        break;
                case 3: inscribirCliente();     break;
                case 4: cerrarInscripciones();  break;
                case 5: generarCruces();        break;
                case 6: verPartidas();          break;
                case 7: registrarResultado();   break;
                case 8: verPosiciones();        break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // ─── Opciones del menu ────────────────────────────────────────────────────
    //cada opcion tiene su try/catch: si el gestor rechaza algo, se muestra el mensaje y se vuelve al menu

    private void crearTorneo() {
        System.out.println("\n--- CREAR TORNEO ---");
        System.out.print("Nombre del torneo: ");
        String nombre = scanner.nextLine();
        System.out.print("Videojuego: ");
        String videojuego = scanner.nextLine();
        System.out.print("Fecha y hora (yyyy-MM-dd HH:mm): ");
        LocalDateTime fecha = leerFecha();
        if (fecha == null) {
            System.out.println("Fecha con formato invalido. Torneo no creado.");
            return;
        }
        System.out.print("Cupo maximo: ");
        int cupo = leerEntero();
        System.out.print("Costo de inscripcion: ");
        double costo = leerDouble();
        ModalidadTorneo modalidad = seleccionarModalidad();
        if (modalidad == null) {
            System.out.println("Modalidad no valida. Torneo no creado.");
            return;
        }
        try {
            Torneo torneo = new Torneo(nombre, videojuego, fecha, cupo, costo, modalidad);
            gestorTorneos.registrar(torneo);
            System.out.println("Torneo creado: " + torneo);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarTorneos() {
        System.out.println("\n--- TORNEOS ---");
        ArrayList<Torneo> torneos = gestorTorneos.listar();
        if (torneos.isEmpty()) {
            System.out.println("No hay torneos registrados.");
            return;
        }
        for (Torneo t : torneos) {
            System.out.println(t);
        }
    }

    private void inscribirCliente() {
        System.out.println("\n--- INSCRIBIR CLIENTE ---");
        System.out.print("Nombre del torneo: ");
        String nombreTorneo = scanner.nextLine();
        System.out.print("Documento del cliente: ");
        String documento = scanner.nextLine();
        try {
            MetodoPago metodoPago = seleccionarMetodoPago();
            if (metodoPago == null) {
                System.out.println("Metodo de pago no valido. Inscripcion cancelada.");
                return;
            }
            Inscripcion inscripcion = gestorTorneos.inscribirCliente(nombreTorneo, documento, metodoPago);
            System.out.println("Inscripcion realizada: " + inscripcion);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cerrarInscripciones() {
        System.out.println("\n--- CERRAR INSCRIPCIONES ---");
        System.out.print("Nombre del torneo: ");
        String nombreTorneo = scanner.nextLine();
        try {
            gestorTorneos.cerrarInscripciones(nombreTorneo);
            System.out.println("Inscripciones cerradas.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void generarCruces() {
        System.out.println("\n--- GENERAR CRUCES ---");
        System.out.print("Nombre del torneo: ");
        String nombreTorneo = scanner.nextLine();
        try {
            ArrayList<Partida> partidas = gestorTorneos.generarCruces(nombreTorneo);
            System.out.println("Cruces generados:");
            imprimirPartidas(partidas);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void verPartidas() {
        System.out.println("\n--- PARTIDAS ---");
        System.out.print("Nombre del torneo: ");
        String nombreTorneo = scanner.nextLine();
        Torneo torneo = gestorTorneos.buscarPorNombre(nombreTorneo);
        if (torneo == null) {
            System.out.println("No existe un torneo con el nombre: " + nombreTorneo);
            return;
        }
        ArrayList<Partida> partidas = torneo.getPartidas();
        if (partidas.isEmpty()) {
            System.out.println("Todavia no se generaron los cruces.");
            return;
        }
        imprimirPartidas(partidas);
    }

    private void registrarResultado() {
        System.out.println("\n--- REGISTRAR RESULTADO ---");
        System.out.print("Nombre del torneo: ");
        String nombreTorneo = scanner.nextLine();
        System.out.print("Numero de la partida (ver opcion 6): ");
        int numero = leerEntero();
        System.out.print("Documento del ganador: ");
        String documento = scanner.nextLine();
        System.out.print("Marcador (ej: 3-1): ");
        String marcador = scanner.nextLine();
        try {
            gestorTorneos.registrarResultado(nombreTorneo, numero, documento, marcador);
            System.out.println("Resultado registrado.");
            System.out.println(gestorTorneos.obtenerPosiciones(nombreTorneo));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void verPosiciones() {
        System.out.println("\n--- POSICIONES ---");
        System.out.print("Nombre del torneo: ");
        String nombreTorneo = scanner.nextLine();
        try {
            System.out.println(gestorTorneos.obtenerPosiciones(nombreTorneo));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // ─── Ayudantes ────────────────────────────────────────────────────────────

    //muestra las partidas numeradas desde 1, que es el numero que pide "Registrar resultado"
    private void imprimirPartidas(ArrayList<Partida> partidas) {
        for (int i = 0; i < partidas.size(); i++) {
            System.out.println((i + 1) + ". " + partidas.get(i));
        }
    }

    //el usuario elige la modalidad; aqui el if es por la opcion escrita, no por el tipo de un objeto
    private ModalidadTorneo seleccionarModalidad() {
        System.out.println("Modalidad: 1. Eliminacion directa   2. Fase de grupos");
        System.out.print("Seleccione: ");
        int opcion = leerEntero();
        if (opcion == 1) {
            return new EliminacionDirecta();
        }
        if (opcion == 2) {
            return new FaseGrupos();
        }
        return null;
    }

    //crea el pago que elija el usuario; cada clase de pago valida sus propios datos
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

    //igual que el del profe: si no escriben un numero devuelve -1
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

    //devuelve null si la fecha no tiene el formato yyyy-MM-dd HH:mm
    private LocalDateTime leerFecha() {
        try {
            return LocalDateTime.parse(scanner.nextLine().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}

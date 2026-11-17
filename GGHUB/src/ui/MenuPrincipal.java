package ui;

import modelos.Cliente;
import modelos.EstacionConsola;
import modelos.EstacionPC;
import modelos.Producto;
import modelos.PuestoJuego;
import modelos.SesionJuego;
import modelos.Torneo;
import modelos.Venta;
import repositorios.ClienteRepository;
import repositorios.ProductoRepository;
import repositorios.PuestoRepository;
import repositorios.SesionRepository;
import repositorios.TorneoRepository;
import repositorios.VentaRepository;
import servicios.GestorCaja;
import servicios.GestorClientes;
import servicios.GestorPuestos;
import servicios.GestorSesiones;
import servicios.GestorTorneos;
import servicios.GestorVentas;
import servicios.InventarioBarra;

import java.util.ArrayList;
import java.util.Scanner;

//Menu principal: desde aqui se abren todos los demas menus
//Tiene los submenus y a todos les pasa el mismo Scanner, asi no se crean varios leyendo el teclado
//Tambien carga los datos de los .txt al arrancar y los guarda cada vez que se vuelve de un submenu (RNF-01)
public class MenuPrincipal {

    private Scanner scanner;
    private MenuClientes menuClientes;
    private MenuPuestos menuPuestos;
    private MenuSesiones menuSesiones;
    private MenuBarra menuBarra;
    private MenuTorneos menuTorneos;
    private MenuCaja menuCaja;

    //los gestores se guardan como atributos porque se necesitan para guardar los datos
    private GestorClientes gestorClientes;
    private GestorPuestos gestorPuestos;
    private GestorSesiones gestorSesiones;
    private InventarioBarra inventarioBarra;
    private GestorVentas gestorVentas;
    private GestorTorneos gestorTorneos;

    //un repositorio por cada cosa que se guarda en archivo
    private ClienteRepository clienteRepository;
    private PuestoRepository puestoRepository;
    private ProductoRepository productoRepository;
    private SesionRepository sesionRepository;
    private VentaRepository ventaRepository;
    private TorneoRepository torneoRepository;

    //aqui se crean todos los gestores, una sola vez, y a cada submenu se le pasa el suyo
    //Los que necesitan otro gestor lo reciben por constructor (el mismo objeto, no uno nuevo)
    public MenuPrincipal() {
        gestorClientes = new GestorClientes();
        gestorPuestos = new GestorPuestos();
        gestorSesiones = new GestorSesiones(gestorClientes, gestorPuestos);
        inventarioBarra = new InventarioBarra();
        gestorVentas = new GestorVentas(inventarioBarra, gestorSesiones);
        gestorTorneos = new GestorTorneos(gestorClientes);
        GestorCaja gestorCaja = new GestorCaja(gestorSesiones, gestorVentas, gestorTorneos);

        clienteRepository = new ClienteRepository();
        puestoRepository = new PuestoRepository();
        productoRepository = new ProductoRepository();
        sesionRepository = new SesionRepository(gestorClientes, gestorPuestos, inventarioBarra);
        ventaRepository = new VentaRepository(inventarioBarra, gestorSesiones);
        torneoRepository = new TorneoRepository(gestorClientes);
        cargarDatos();

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
            //se guarda cada vez que se vuelve de un submenu, asi no se pierde nada si cierran el programa
            guardarDatos();
        } while (opcion != 0);
    }

    //Lee los .txt y llena los gestores. El orden importa: las sesiones necesitan los clientes,
    //los puestos y los productos; las ventas necesitan las sesiones; los torneos necesitan los clientes
    private void cargarDatos() {
        for (Cliente c : clienteRepository.getDataList()) {
            gestorClientes.registrar(c);
        }
        ArrayList<PuestoJuego> puestos = puestoRepository.getDataList();
        //la primera vez no hay archivo, entonces se crea la sala del README
        if (puestos.isEmpty()) {
            crearSala();
        }
        for (PuestoJuego p : puestos) {
            gestorPuestos.registrar(p);
        }
        for (Producto p : productoRepository.getDataList()) {
            inventarioBarra.registrarProducto(p);
        }
        for (SesionJuego s : sesionRepository.getDataList()) {
            gestorSesiones.cargar(s);
        }
        for (Venta v : ventaRepository.getDataList()) {
            gestorVentas.cargar(v);
        }
        for (Torneo t : torneoRepository.getDataList()) {
            gestorTorneos.cargar(t);
        }
    }

    //Reescribe todos los .txt con lo que tienen los gestores en este momento
    private void guardarDatos() {
        clienteRepository.guardarTodos(gestorClientes.listar());
        puestoRepository.guardarTodos(gestorPuestos.listar());
        productoRepository.guardarTodos(inventarioBarra.listar());
        sesionRepository.guardarTodos(gestorSesiones.listar());
        ventaRepository.guardarTodos(gestorVentas.listar());
        torneoRepository.guardarTodos(gestorTorneos.listar());
    }

    //Crea la sala del README: PCs del 1 al 15 y consolas del 16 al 25 (la misma que habia en el Main)
    //Solo se usa la primera vez, cuando todavia no existe puestos.txt
    private void crearSala() {
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

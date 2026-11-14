import enums.EstadoPuesto;
import modelos.*;
import servicios.GestorClientes;
import servicios.GestorPuestos;
import servicios.GestorSesiones;

import java.awt.font.FontRenderContext;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        GestorClientes gestorClientes = new GestorClientes();
        Cliente cliente = new Cliente("1029", "pasco", "321123343", "marcopollo@gmail.com");
        Cliente cliente2 = new Cliente("10295", "pasco7", "34534543", "vascoo@gmail.com");
        Cliente cliente3 = new Cliente("1029", "perroflauta", "676767676", "asdascoo@gmail.com");
        Cliente cliente4 = new Cliente("1029A", "perroflauta", "676767676", "asdascoo@gmail.com");
        GestorPuestos gestor = new GestorPuestos();
        intentarRegistrar(gestorClientes, cliente);
        intentarRegistrar(gestorClientes, cliente2);
        intentarRegistrar(gestorClientes, cliente3);
        intentarRegistrar(gestorClientes, cliente4);
        System.out.println(gestorClientes.cantidad());
        mostrarClientes(gestorClientes);
        crearSala(gestor);

        cantidadPuestos(gestor);
        PuestoJuego puesto3 = gestor.buscarPorId(3);
        puesto3.cambiarEstado(EstadoPuesto.EN_MANTENIMIENTO);
        PuestoJuego puesto20 = gestor.buscarPorId(20);
        puesto20.cambiarEstado(EstadoPuesto.EN_USO);
        mostrarPuestos(gestor);
        GestorSesiones gestorSesiones = new GestorSesiones(gestorClientes,gestor);
        // Pruebas RF-04 (documento, estacion, controles extra)
        intentarIniciarSesion(gestorSesiones, "1029", 1, 0);    // 1. valida
        intentarIniciarSesion(gestorSesiones, "10295", 1, 0);   // 2. estacion 1 ya en uso
        intentarIniciarSesion(gestorSesiones,cliente2.getDocumento(),puesto3.getId(),0); // 3. estacion 3 en mantenimiento
        intentarIniciarSesion(gestorSesiones, "9999", 2, 0);    // 4. cliente no existe
        intentarIniciarSesion(gestorSesiones, "10295", 99, 0);  // 5. estacion no existe
        intentarIniciarSesion(gestorSesiones, "10295", 16, -2); // 6. controles negativos
        intentarIniciarSesion(gestorSesiones, "10295", 16, 2);  // 7. valida en consola
        mostrarPuestos(gestor);






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
    private static void crearSala(GestorPuestos gestorPuestos){
        for (int i = 1; i < 16 ; i++) {
            EstacionPC estacionPc = new EstacionPC(i,"ASUS",10.0,"GTX");
            intentarRegistrarPuesto(gestorPuestos,estacionPc);

        }
        for (int i = 16; i < 26; i++) {
            EstacionConsola estacionConsola = new EstacionConsola(i,"PS4",9.5,"PLAYSTATION",1.0);
            intentarRegistrarPuesto(gestorPuestos,estacionConsola);

        }

    }
    private static void intentarIniciarSesion(GestorSesiones gestorSesiones, String documento, int id, int controlesExtra) {
        try {
            SesionJuego sesion = gestorSesiones.iniciarSesion(documento, id, controlesExtra);
            System.out.println(sesion);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
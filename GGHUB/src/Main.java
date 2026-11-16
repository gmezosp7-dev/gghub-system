import modelos.*;
import servicios.GestorClientes;
import servicios.GestorPuestos;
import servicios.GestorSesiones;
import servicios.InventarioBarra;

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
        // el mantenimiento entra por el gestor (con reglas), nunca con cambiarEstado directo
        intentarMantenimiento(gestor, 3);              // la 3 pasa a mantenimiento
        mostrarPuestos(gestor);
        GestorSesiones gestorSesiones = new GestorSesiones(gestorClientes,gestor);
        // Pruebas RF-04 (documento, estacion, controles extra)
        intentarIniciarSesion(gestorSesiones, "1029", 1, 0);    // 1. valida
        intentarIniciarSesion(gestorSesiones, "10295", 1, 0);   // 2. estacion 1 ya en uso
        intentarIniciarSesion(gestorSesiones,cliente2.getDocumento(), 3, 0); // 3. estacion 3 en mantenimiento
        intentarIniciarSesion(gestorSesiones, "9999", 2, 0);    // 4. cliente no existe
        intentarIniciarSesion(gestorSesiones, "10295", 99, 0);  // 5. estacion no existe
        intentarIniciarSesion(gestorSesiones, "10295", 16, -2); // 6. controles negativos
        intentarIniciarSesion(gestorSesiones, "10295", 16, 2);  // 7. valida en consola
        mostrarPuestos(gestor);

        // Pruebas de las reglas de mantenimiento
        System.out.println("\n=== Mantenimiento ===");
        intentarMantenimiento(gestor, 1);      // la 1 tiene a pasco jugando: no se le quita
        intentarMantenimiento(gestor, 99);     // no existe
        intentarQuitarMantenimiento(gestor, 2); // la 2 no esta en mantenimiento
        intentarQuitarMantenimiento(gestor, 3); // la 3 vuelve a DISPONIBLE
        intentarMantenimiento(gestor, 3);      // y otra vez a mantenimiento para las pruebas de abajo

        // Pruebas RF-05: finalizar sesion, cobrar y liberar la estacion
        System.out.println("\n=== RF-05: finalizar y cobrar ===");
        intentarFinalizarSesion(gestorSesiones, 1);   // 1. PC de pasco: se cobra y se libera
        intentarFinalizarSesion(gestorSesiones, 1);   // 2. ya se cobro: no hay sesion activa
        intentarFinalizarSesion(gestorSesiones, 16);  // 3. consola con 2 controles
        intentarFinalizarSesion(gestorSesiones, 2);   // 4. estacion sin ninguna sesion
        intentarFinalizarSesion(gestorSesiones, 99);  // 5. estacion que no existe
        mostrarPuestos(gestor);                       // la 1 y la 16 deben volver a DISPONIBLE

        // El Main corre en milisegundos (0 minutos jugados), asi que la parte del tiempo
        // se prueba llamando directo al cobro polimorfico con horas fijas.
        System.out.println("\n=== RF-05: cobro polimorfico con horas fijas ===");
        mostrarCobro(gestor.buscarPorId(1), 2.0, 0);    // PC: 2 x 10.0 = 20.0
        mostrarCobro(gestor.buscarPorId(1), 2.0, 3);    // PC ignora controles: 20.0
        mostrarCobro(gestor.buscarPorId(16), 2.0, 0);   // Consola: 2 x 9.5 = 19.0
        mostrarCobro(gestor.buscarPorId(16), 2.0, 3);   // Consola: 19.0 + 3 x 1.0 = 22.0
        mostrarCobro(gestor.buscarPorId(16), 1.5, 1);   // Consola: 14.25 + 1.0 = 15.25

        // Pruebas RF-06: registrar y actualizar productos de la barra
        // Producto(codigo, nombre, categoria, costo, precioVenta, stock)
        System.out.println("\n=== RF-06: productos de la barra ===");
        InventarioBarra inventario = new InventarioBarra();
        intentarRegistrarProducto(inventario, new Producto("BEB-001", "Coca-Cola", "bebidas", 2000, 3500, 20)); // 1. valido
        intentarRegistrarProducto(inventario, new Producto("SNK-001", "Papas", "snacks", 1200, 2500, 15));      // 2. valido
        intentarRegistrarProducto(inventario, new Producto("SNK-002", "Chocolatina", "snacks", 800, 1800, 0));  // 3. valido: stock 0 (agotado)
        intentarRegistrarProducto(inventario, new Producto("BEB-001", "Pepsi", "bebidas", 2000, 3500, 10));     // 4. codigo repetido
        intentarRegistrarProducto(inventario, new Producto("BEB-002", "Agua", "bebidas", 500, -1000, 10));      // 5. precio negativo
        intentarRegistrarProducto(inventario, new Producto("BEB-003", "Jugo", "bebidas", 900, 2000, -4));       // 6. stock negativo
        intentarRegistrarProducto(inventario, new Producto("", "Sin codigo", "snacks", 100, 200, 1));           // 7. codigo vacio
        System.out.println("Productos registrados: " + inventario.cantidad());                                  // deben ser 3

        intentarActualizarPrecio(inventario, "BEB-001", 4000);  // 8. precio valido: 3500 -> 4000
        intentarActualizarPrecio(inventario, "BEB-001", -50);   // 9. precio negativo: no cambia
        intentarActualizarPrecio(inventario, "XXX-999", 100);   // 10. codigo que no existe
        intentarReabastecer(inventario, "SNK-002", 12);         // 11. chocolatina: 0 -> 12
        intentarReabastecer(inventario, "SNK-002", 0);          // 12. reabastecer 0: no tiene sentido
        intentarReabastecer(inventario, "XXX-999", 5);          // 13. codigo que no existe
        mostrarInventario(inventario);                          // Coca-Cola a 4000 y chocolatina con 12






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
    private static void intentarFinalizarSesion(GestorSesiones gestorSesiones, int id) {
        try {
            SesionJuego sesion = gestorSesiones.finalizarSesion(id);
            System.out.println("Sesion finalizada en el puesto " + id + " | cliente: "
                    + sesion.getCliente().getNombre() + " | total: $" + sesion.getTotal());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void intentarMantenimiento(GestorPuestos gestorPuestos, int id) {
        try {
            gestorPuestos.ponerEnMantenimiento(id);
            System.out.println("Puesto " + id + " en mantenimiento");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void intentarQuitarMantenimiento(GestorPuestos gestorPuestos, int id) {
        try {
            gestorPuestos.quitarMantenimiento(id);
            System.out.println("Puesto " + id + " disponible de nuevo");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void intentarRegistrarProducto(InventarioBarra inventario, Producto producto) {
        try {
            inventario.registrarProducto(producto);
            System.out.println("Registrado: " + producto);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void intentarActualizarPrecio(InventarioBarra inventario, String codigo, double nuevoPrecio) {
        try {
            inventario.actualizarPrecio(codigo, nuevoPrecio);
            System.out.println("Precio de " + codigo + " actualizado a $" + nuevoPrecio);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void intentarReabastecer(InventarioBarra inventario, String codigo, int cantidad) {
        try {
            inventario.reabastecer(codigo, cantidad);
            System.out.println("Reabastecido " + codigo + " con " + cantidad + " unidades");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void mostrarInventario(InventarioBarra inventario) {
        System.out.println("--- Inventario de la barra ---");
        for (Producto p : inventario.listar()) {
            System.out.println(p);
        }
    }
    // la variable es PuestoJuego: Java ejecuta el calcularCostoSesion del objeto real (PC o consola)
    private static void mostrarCobro(PuestoJuego puesto, double horas, int controles) {
        System.out.println("Puesto " + puesto.getId() + " | " + horas + " h | " + controles
                + " controles -> $" + puesto.calcularCostoSesion(horas, controles));
    }

}
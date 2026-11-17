package ui;

import interfaces.MetodoPago;
import modelos.*;
import servicios.GestorClientes;
import servicios.GestorPuestos;
import servicios.GestorSesiones;
import servicios.InventarioBarra;
import servicios.GestorVentas;
import servicios.GestorTorneos;
import servicios.GestorCaja;
import java.time.LocalDateTime;

//Las pruebas que estaban en el Main, ahora se ven desde la opcion 7 del menu principal (como la demo del profe)
//Usa sus propios gestores, asi no mezcla sus datos de prueba con los del programa
public class Demo {
    public static void ejecutarDemo() {
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
        intentarFinalizarSesion(gestorSesiones, 1, new Efectivo(10000));            // 1. PC de pasco: paga en efectivo y se libera
        intentarFinalizarSesion(gestorSesiones, 1, new Efectivo(10000));            // 2. ya se cobro: no hay sesion activa
        intentarFinalizarSesion(gestorSesiones, 16, new Efectivo(1));               // 3. consola con 2 controles ($2): $1 no alcanza
        System.out.println("Estado de la 16 tras el pago fallido: " + gestor.buscarPorId(16).getEstado()); // sigue EN_USO
        intentarFinalizarSesion(gestorSesiones, 16, new TarjetaCredito("1234", 1)); // 4. ahora si paga con tarjeta y se libera
        intentarFinalizarSesion(gestorSesiones, 2, new Efectivo(10000));            // 5. estacion sin ninguna sesion
        intentarFinalizarSesion(gestorSesiones, 99, new Efectivo(10000));           // 6. estacion que no existe
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

        // Pruebas RF-07: ventas de la barra (contado y cargo a sesion)
        // Stock al empezar: Coca-Cola 20 ($4000), Papas 15 ($2500), Chocolatina 12 ($1800)
        System.out.println("\n=== RF-07: ventas de la barra ===");
        GestorVentas gestorVentas = new GestorVentas(inventario, gestorSesiones);

        // 1. venta de contado valida: 2 Coca-Colas + 1 papas = 8000 + 2500 = 10500
        Venta contado = gestorVentas.nuevaVentaContado();
        intentarAgregarProducto(gestorVentas, contado, "BEB-001", 2);
        intentarAgregarProducto(gestorVentas, contado, "SNK-001", 1);
        intentarAgregarProducto(gestorVentas, contado, "BEB-001", 999);  // 2. mas de lo que hay en stock
        intentarAgregarProducto(gestorVentas, contado, "XXX-999", 1);    // 3. producto que no existe
        intentarAgregarProducto(gestorVentas, contado, "SNK-001", 0);    // 4. cantidad cero
        intentarConfirmarVenta(gestorVentas, contado, new Efectivo(5000));  // 5. $5000 no alcanza para $10500: no se descuenta nada
        intentarConfirmarVenta(gestorVentas, contado, null);                // 6. contado sin metodo de pago
        intentarConfirmarVenta(gestorVentas, contado, new Efectivo(20000)); // confirma y paga: cambio $9500; Coca 20->18, Papas 15->14
        intentarConfirmarVenta(gestorVentas, contado, new Efectivo(20000)); // 7. confirmar dos veces
        intentarAgregarProducto(gestorVentas, contado, "SNK-002", 1);       // 8. agregar a una venta confirmada
        intentarConfirmarVenta(gestorVentas, gestorVentas.nuevaVentaContado(), new Efectivo(5000)); // 9. venta vacia

        // 10. otras dos ventas de contado, cada una con un metodo de pago distinto (polimorfismo)
        Venta conTarjeta = gestorVentas.nuevaVentaContado();
        intentarAgregarProducto(gestorVentas, conTarjeta, "SNK-001", 2);                       // 2 papas = $5000
        intentarConfirmarVenta(gestorVentas, conTarjeta, new TarjetaCredito("4321", 3));
        Venta conNequi = gestorVentas.nuevaVentaContado();
        intentarAgregarProducto(gestorVentas, conNequi, "BEB-001", 1);                         // 1 Coca = $4000
        intentarConfirmarVenta(gestorVentas, conNequi, new PagoDigital("Nequi", "3001234567"));

        // 11. datos de pago invalidos: se rechazan al crear el metodo de pago
        probarMetodosDePagoInvalidos();

        // 12. cargo a sesion: pasco se sienta en la estacion 5 y pide 1 chocolatina a su cuenta (no paga ahora)
        intentarIniciarSesion(gestorSesiones, "1029", 5, 0);
        Venta cargo = intentarNuevaVentaCargo(gestorVentas, 5);
        if (cargo != null) {
            intentarAgregarProducto(gestorVentas, cargo, "SNK-002", 1);
            intentarConfirmarCargo(gestorVentas, cargo);                 // Chocolatina 12->11, $1800 a la cuenta
        }
        intentarNuevaVentaCargo(gestorVentas, 2);                        // 13. estacion 2 sin sesion activa

        // 14. al finalizar la sesion de la 5 se paga todo junto: 0 min + $1800 de la chocolatina
        intentarFinalizarSesion(gestorSesiones, 5, new PagoDigital("Daviplata", "3109876543"));

        intentarNuevaVentaCargo(gestorVentas, 5);                        // 15. la 5 ya se cerro: no se le carga nada mas
        mostrarVentas(gestorVentas);
        mostrarInventario(inventario);                                   // Coca 17, Papas 12, Chocolatina 11

        // Pruebas RF-12: cierre de caja del dia
        System.out.println("\n=== RF-12: cierre de caja ===");
        // un torneo con 2 inscritos (lo minimo para que el cierre tenga dinero de torneos)
        GestorTorneos gestorTorneos = new GestorTorneos(gestorClientes);
        try {
            gestorTorneos.registrar(new Torneo("Copa GG", "FIFA", LocalDateTime.now().plusDays(1), 8, 15000, new EliminacionDirecta()));
            // desde el merge con torneos, la inscripcion se cobra con un MetodoPago
            gestorTorneos.inscribirCliente("Copa GG", "1029", new Efectivo(20000));
            gestorTorneos.inscribirCliente("Copa GG", "10295", new PagoDigital("Nequi", "3001234567"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        // una sesion ABIERTA: no debe contar en el cierre porque todavia no se ha pagado
        intentarIniciarSesion(gestorSesiones, "10295", 20, 0);

        GestorCaja gestorCaja = new GestorCaja(gestorSesiones, gestorVentas, gestorTorneos);
        intentarCerrarCaja(gestorCaja, "Marco");   // 1. cierre valido
        intentarCerrarCaja(gestorCaja, "   ");     // 2. operador vacio
        // Lo que debe salir (las sesiones del Main duran 0 minutos, por eso juego = $0):
        //   juego $0 | controles $2 (la consola 16) | barra: contado 10500 + 5000 + 4000 y la chocolatina
        //   cargada 1800 (contada UNA sola vez) = $21300 | torneos 2 x 15000 = $30000
        //   sesiones 3 (la 1, la 16 y la 5; la 20 sigue abierta) | ventas de contado 3 | inscripciones 2






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
    // el parametro es MetodoPago: le sirve Efectivo, TarjetaCredito o PagoDigital (polimorfismo)
    private static void intentarFinalizarSesion(GestorSesiones gestorSesiones, int id, MetodoPago metodo) {
        try {
            SesionJuego sesion = gestorSesiones.finalizarSesion(id, metodo);
            System.out.println("Sesion finalizada en el puesto " + id + " | cliente: "
                    + sesion.getCliente().getNombre() + " | total: $" + sesion.getTotal()
                    + "\n   comprobante: " + metodo.obtenerComprobante());
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
    private static void intentarAgregarProducto(GestorVentas gestorVentas, Venta venta, String codigo, int cantidad) {
        try {
            gestorVentas.agregarProducto(venta, codigo, cantidad);
            System.out.println("Agregado " + codigo + " x" + cantidad);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    // venta de CONTADO: se confirma y se paga en el momento
    private static void intentarConfirmarVenta(GestorVentas gestorVentas, Venta venta, MetodoPago metodo) {
        try {
            gestorVentas.confirmar(venta, metodo);
            System.out.println("Venta confirmada: " + venta
                    + "\n   comprobante: " + metodo.obtenerComprobante());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    // venta con CARGO_SESION: se confirma sin pagar (se paga al finalizar la sesion)
    private static void intentarConfirmarCargo(GestorVentas gestorVentas, Venta venta) {
        try {
            gestorVentas.confirmar(venta);
            System.out.println("Venta confirmada: " + venta);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void intentarCerrarCaja(GestorCaja gestorCaja, String operador) {
        try {
            System.out.println(gestorCaja.cerrarCaja(operador));
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    // los datos de pago se validan al crear el metodo de pago
    private static void probarMetodosDePagoInvalidos() {
        try { new Efectivo(0); }
        catch (IllegalArgumentException e) { System.out.println("Efectivo de $0: " + e.getMessage()); }
        try { new TarjetaCredito("12", 1); }
        catch (IllegalArgumentException e) { System.out.println("Tarjeta '12': " + e.getMessage()); }
        try { new TarjetaCredito("1234", 48); }
        catch (IllegalArgumentException e) { System.out.println("Tarjeta a 48 cuotas: " + e.getMessage()); }
        try { new PagoDigital("Nequi", "300123"); }
        catch (IllegalArgumentException e) { System.out.println("Celular corto: " + e.getMessage()); }
        try { new PagoDigital("", "3001234567"); }
        catch (IllegalArgumentException e) { System.out.println("Sin plataforma: " + e.getMessage()); }
    }
    // devuelve la venta creada, o null si la estacion no tiene sesion activa
    private static Venta intentarNuevaVentaCargo(GestorVentas gestorVentas, int idPuesto) {
        try {
            return gestorVentas.nuevaVentaCargoSesion(idPuesto);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    private static void mostrarVentas(GestorVentas gestorVentas) {
        System.out.println("--- Ventas confirmadas (" + gestorVentas.cantidad() + ") ---");
        for (Venta v : gestorVentas.listar()) {
            System.out.println(v);
        }
    }
    // la variable es PuestoJuego: Java ejecuta el calcularCostoSesion del objeto real (PC o consola)
    private static void mostrarCobro(PuestoJuego puesto, double horas, int controles) {
        System.out.println("Puesto " + puesto.getId() + " | " + horas + " h | " + controles
                + " controles -> $" + puesto.calcularCostoSesion(horas, controles));
    }

}
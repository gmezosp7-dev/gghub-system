package ui;

import interfaces.MetodoPago;
import modelos.Efectivo;
import modelos.PagoDigital;
import modelos.Producto;
import modelos.TarjetaCredito;
import modelos.Venta;
import servicios.GestorVentas;
import servicios.InventarioBarra;

import java.util.ArrayList;
import java.util.Scanner;

//Menu de la barra (RF-06 y RF-07), igual que el de torneos
//Solo pide los datos y se los pasa al inventario o al gestor de ventas, que son los que validan
public class MenuBarra {

    private InventarioBarra inventarioBarra;
    private GestorVentas gestorVentas;
    private Scanner scanner;

    //Recibe los gestores y el Scanner del menu principal para no crear otros nuevos
    public MenuBarra(InventarioBarra inventarioBarra, GestorVentas gestorVentas, Scanner scanner) {
        this.inventarioBarra = inventarioBarra;
        this.gestorVentas = gestorVentas;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("        BARRA DE SNACKS       ");
            System.out.println("==============================");
            System.out.println("1. Registrar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Actualizar precio");
            System.out.println("4. Reabastecer producto");
            System.out.println("5. Venta de contado");
            System.out.println("6. Venta cargada a una sesion");
            System.out.println("7. Listar ventas");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: registrarProducto();     break;
                case 2: listarProductos();       break;
                case 3: actualizarPrecio();      break;
                case 4: reabastecer();           break;
                case 5: ventaContado();          break;
                case 6: ventaCargoSesion();      break;
                case 7: listarVentas();          break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void registrarProducto() {
        System.out.println("\n--- REGISTRAR PRODUCTO ---");
        System.out.print("Codigo: ");
        String codigo = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Categoria (ej: Bebidas): ");
        String categoria = scanner.nextLine();
        System.out.print("Costo: ");
        double costo = leerDouble();
        System.out.print("Precio de venta: ");
        double precio = leerDouble();
        System.out.print("Stock inicial: ");
        int stock = leerEntero();
        try {
            Producto producto = new Producto(codigo, nombre, categoria, costo, precio, stock);
            inventarioBarra.registrarProducto(producto);
            System.out.println("Producto registrado: " + producto);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarProductos() {
        System.out.println("\n--- PRODUCTOS ---");
        ArrayList<Producto> productos = inventarioBarra.listar();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    private void actualizarPrecio() {
        System.out.println("\n--- ACTUALIZAR PRECIO ---");
        System.out.print("Codigo del producto: ");
        String codigo = scanner.nextLine();
        System.out.print("Nuevo precio de venta: ");
        double precio = leerDouble();
        try {
            inventarioBarra.actualizarPrecio(codigo, precio);
            System.out.println("Precio actualizado.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void reabastecer() {
        System.out.println("\n--- REABASTECER ---");
        System.out.print("Codigo del producto: ");
        String codigo = scanner.nextLine();
        System.out.print("Cantidad que llega: ");
        int cantidad = leerEntero();
        try {
            inventarioBarra.reabastecer(codigo, cantidad);
            System.out.println("Stock actualizado.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ventaContado() {
        System.out.println("\n--- VENTA DE CONTADO ---");
        Venta venta = gestorVentas.nuevaVentaContado();
        if (!agregarProductos(venta)) {
            return;
        }
        try {
            MetodoPago metodoPago = seleccionarMetodoPago();
            if (metodoPago == null) {
                System.out.println("Metodo de pago no valido. Venta cancelada.");
                return;
            }
            //Se confirma por el gestor y no por la venta, asi queda guardada para el cierre de caja
            gestorVentas.confirmar(venta, metodoPago);
            System.out.println("Venta realizada: " + venta);
            System.out.println("  " + metodoPago.obtenerComprobante());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ventaCargoSesion() {
        System.out.println("\n--- VENTA CARGADA A UNA SESION ---");
        System.out.print("Id de la estacion: ");
        int id = leerEntero();
        try {
            Venta venta = gestorVentas.nuevaVentaCargoSesion(id);
            if (!agregarProductos(venta)) {
                return;
            }
            //No se paga ahora, se suma a la cuenta de la sesion y se paga al finalizarla
            gestorVentas.confirmar(venta);
            System.out.println("Venta cargada a la sesion: " + venta);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarVentas() {
        System.out.println("\n--- VENTAS ---");
        ArrayList<Venta> ventas = gestorVentas.listar();
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        for (Venta v : ventas) {
            System.out.println(v);
        }
    }

    //Pide productos hasta que dejen el codigo vacio; devuelve false si no se agrego ninguno
    private boolean agregarProductos(Venta venta) {
        System.out.println("Escriba los productos (deje el codigo vacio para terminar)");
        while (true) {
            System.out.print("Codigo: ");
            String codigo = scanner.nextLine().trim();
            if (codigo.isEmpty()) {
                break;
            }
            System.out.print("Cantidad: ");
            int cantidad = leerEntero();
            try {
                gestorVentas.agregarProducto(venta, codigo, cantidad);
                System.out.println("  Agregado. Total hasta ahora: $" + venta.calcularTotal());
            } catch (IllegalArgumentException e) {
                System.out.println("  " + e.getMessage());
            }
        }
        if (venta.getItems().isEmpty()) {
            System.out.println("No se agrego ningun producto. Venta cancelada.");
            return false;
        }
        return true;
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

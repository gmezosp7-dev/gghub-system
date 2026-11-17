package repositorios;

import interfaces.IRepository;
import modelos.Cliente;
import modelos.ItemConsumo;
import modelos.Producto;
import modelos.PuestoJuego;
import modelos.SesionJuego;
import persistencia.FileManage;
import servicios.GestorClientes;
import servicios.GestorPuestos;
import servicios.InventarioBarra;

import java.time.LocalDateTime;
import java.util.ArrayList;

//Guarda y lee las sesiones (abiertas y cerradas) en sesiones.txt
//Linea: documento|idPuesto|controlesExtra|inicio|fin|total|costoTiempo|costoControles|consumos
//fin es "-" si la sesion sigue abierta; consumos va como codigo:cantidad:precio;codigo:cantidad:precio
//Como el PrestamoRepository del profe: el cliente y el puesto se guardan por su id y al cargar se buscan en su gestor
public class SesionRepository implements IRepository<SesionJuego> {

    private FileManage fileManage = new FileManage("sesiones.txt");
    private GestorClientes gestorClientes;
    private GestorPuestos gestorPuestos;
    private InventarioBarra inventarioBarra;

    //Recibe los gestores para buscar el cliente, el puesto y los productos de cada sesion
    public SesionRepository(GestorClientes gestorClientes, GestorPuestos gestorPuestos, InventarioBarra inventarioBarra) {
        this.gestorClientes = gestorClientes;
        this.gestorPuestos = gestorPuestos;
        this.inventarioBarra = inventarioBarra;
    }

    @Override
    public ArrayList<SesionJuego> getDataList() {
        ArrayList<SesionJuego> sesiones = new ArrayList<>();
        ArrayList<String> lines = fileManage.getDataOfFile();
        if (lines == null) {
            return sesiones;
        }
        for (String line : lines) {
            String[] datos = line.split("\\|", -1);
            Cliente cliente = gestorClientes.buscarPorDocumento(datos[0]);
            PuestoJuego puesto = gestorPuestos.buscarPorId(Integer.parseInt(datos[1]));
            //si el cliente o el puesto ya no existen, esa sesion no se puede armar
            if (cliente == null || puesto == null) {
                continue;
            }
            LocalDateTime fin = null;
            if (!datos[4].equals("-")) {
                fin = LocalDateTime.parse(datos[4]);
            }
            sesiones.add(new SesionJuego(cliente, puesto, Integer.parseInt(datos[2]), LocalDateTime.parse(datos[3]), fin,
                    Double.parseDouble(datos[5]), Double.parseDouble(datos[6]), Double.parseDouble(datos[7]),
                    leerConsumos(datos[8])));
        }
        return sesiones;
    }

    @Override
    public void insertData(SesionJuego sesion) {
        fileManage.writeFile(getDataLine(sesion));
    }

    @Override
    public void updateData(SesionJuego oldSesion, SesionJuego newSesion) {
        fileManage.updateFile(getDataLine(oldSesion), getDataLine(newSesion));
    }

    @Override
    public void deleteData(SesionJuego sesion) {
        fileManage.deleteFile(getDataLine(sesion));
    }

    @Override
    public String getDataLine(SesionJuego sesion) {
        String fin = "-";
        if (sesion.getFin() != null) {
            fin = sesion.getFin().toString();
        }
        return sesion.getCliente().getDocumento() + "|" + sesion.getPuesto().getId() + "|" + sesion.getControlesExtra()
                + "|" + sesion.getInicio() + "|" + fin + "|" + sesion.getTotal() + "|" + sesion.getCostoTiempo()
                + "|" + sesion.getCostoControles() + "|" + escribirConsumos(sesion.listar());
    }

    //Reescribe el archivo con todas las sesiones de la lista
    public void guardarTodos(ArrayList<SesionJuego> sesiones) {
        ArrayList<String> lines = new ArrayList<>();
        for (SesionJuego s : sesiones) {
            lines.add(getDataLine(s));
        }
        fileManage.writeAllLinesInFile(lines);
    }

    //Pasa los consumos a texto: codigo:cantidad:precio separados por ; ("-" si no hay)
    private String escribirConsumos(ArrayList<ItemConsumo> items) {
        if (items.isEmpty()) {
            return "-";
        }
        String texto = "";
        for (ItemConsumo item : items) {
            if (!texto.isEmpty()) {
                texto = texto + ";";
            }
            texto = texto + item.getProducto().getCodigo() + ":" + item.getCantidad() + ":" + item.getPrecioUnitario();
        }
        return texto;
    }

    //Al reves: arma los consumos desde el texto, buscando cada producto en el inventario
    private ArrayList<ItemConsumo> leerConsumos(String texto) {
        ArrayList<ItemConsumo> items = new ArrayList<>();
        if (texto.equals("-")) {
            return items;
        }
        for (String parte : texto.split(";")) {
            String[] datos = parte.split(":");
            Producto producto = inventarioBarra.buscarProducto(datos[0]);
            if (producto != null) {
                items.add(new ItemConsumo(producto, Integer.parseInt(datos[1]), Double.parseDouble(datos[2])));
            }
        }
        return items;
    }
}

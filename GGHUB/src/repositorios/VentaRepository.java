package repositorios;

import enums.TipoVenta;
import interfaces.IRepository;
import modelos.ItemConsumo;
import modelos.Producto;
import modelos.SesionJuego;
import modelos.Venta;
import persistencia.FileManage;
import servicios.GestorSesiones;
import servicios.InventarioBarra;

import java.time.LocalDateTime;
import java.util.ArrayList;

//Guarda y lee las ventas de la barra en ventas.txt
//Linea: tipo|fechaHora|idPuesto|inicioSesion|items
//idPuesto e inicioSesion son "-" en las de contado; items va como codigo:cantidad:precio;codigo:cantidad:precio
public class VentaRepository implements IRepository<Venta> {

    private FileManage fileManage = new FileManage("ventas.txt");
    private InventarioBarra inventarioBarra;
    private GestorSesiones gestorSesiones;

    //Recibe el inventario para buscar los productos y el gestor de sesiones para las ventas cargadas a una sesion
    public VentaRepository(InventarioBarra inventarioBarra, GestorSesiones gestorSesiones) {
        this.inventarioBarra = inventarioBarra;
        this.gestorSesiones = gestorSesiones;
    }

    @Override
    public ArrayList<Venta> getDataList() {
        ArrayList<Venta> ventas = new ArrayList<>();
        ArrayList<String> lines = fileManage.getDataOfFile();
        if (lines == null) {
            return ventas;
        }
        for (String line : lines) {
            String[] datos = line.split("\\|", -1);
            TipoVenta tipo = TipoVenta.valueOf(datos[0]);
            SesionJuego sesion = null;
            if (tipo == TipoVenta.CARGO_SESION) {
                sesion = buscarSesion(Integer.parseInt(datos[2]), LocalDateTime.parse(datos[3]));
                //si no se encuentra la sesion, la venta no se puede armar
                if (sesion == null) {
                    continue;
                }
            }
            ventas.add(new Venta(tipo, sesion, LocalDateTime.parse(datos[1]), leerItems(datos[4])));
        }
        return ventas;
    }

    @Override
    public void insertData(Venta venta) {
        fileManage.writeFile(getDataLine(venta));
    }

    @Override
    public void updateData(Venta oldVenta, Venta newVenta) {
        fileManage.updateFile(getDataLine(oldVenta), getDataLine(newVenta));
    }

    @Override
    public void deleteData(Venta venta) {
        fileManage.deleteFile(getDataLine(venta));
    }

    @Override
    public String getDataLine(Venta venta) {
        //una sesion se reconoce por su estacion y la hora en que empezo
        String idPuesto = "-";
        String inicioSesion = "-";
        if (venta.getSesion() != null) {
            idPuesto = String.valueOf(venta.getSesion().getPuesto().getId());
            inicioSesion = venta.getSesion().getInicio().toString();
        }
        return venta.getTipo() + "|" + venta.getFechaHora() + "|" + idPuesto + "|" + inicioSesion + "|"
                + escribirItems(venta.getItems());
    }

    //Reescribe el archivo con todas las ventas de la lista
    public void guardarTodos(ArrayList<Venta> ventas) {
        ArrayList<String> lines = new ArrayList<>();
        for (Venta v : ventas) {
            lines.add(getDataLine(v));
        }
        fileManage.writeAllLinesInFile(lines);
    }

    //Busca la sesion de esa estacion que empezo a esa hora
    private SesionJuego buscarSesion(int idPuesto, LocalDateTime inicio) {
        for (SesionJuego s : gestorSesiones.listar()) {
            if (s.getPuesto().getId() == idPuesto && s.getInicio().equals(inicio)) {
                return s;
            }
        }
        return null;
    }

    //Pasa los productos a texto: codigo:cantidad:precio separados por ;
    private String escribirItems(ArrayList<ItemConsumo> items) {
        String texto = "";
        for (ItemConsumo item : items) {
            if (!texto.isEmpty()) {
                texto = texto + ";";
            }
            texto = texto + item.getProducto().getCodigo() + ":" + item.getCantidad() + ":" + item.getPrecioUnitario();
        }
        return texto;
    }

    //Al reves: arma los productos desde el texto, buscando cada uno en el inventario
    private ArrayList<ItemConsumo> leerItems(String texto) {
        ArrayList<ItemConsumo> items = new ArrayList<>();
        if (texto.isEmpty()) {
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

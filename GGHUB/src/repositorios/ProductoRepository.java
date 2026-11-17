package repositorios;

import interfaces.IRepository;
import modelos.Producto;
import persistencia.FileManage;

import java.util.ArrayList;

//Guarda y lee el inventario de la barra en productos.txt
//Linea: codigo|nombre|categoria|costo|precioVenta|stock
public class ProductoRepository implements IRepository<Producto> {

    private FileManage fileManage = new FileManage("productos.txt");

    @Override
    public ArrayList<Producto> getDataList() {
        ArrayList<Producto> productos = new ArrayList<>();
        ArrayList<String> lines = fileManage.getDataOfFile();
        if (lines == null) {
            return productos;
        }
        for (String line : lines) {
            String[] datos = line.split("\\|", -1);
            productos.add(new Producto(datos[0], datos[1], datos[2], Double.parseDouble(datos[3]),
                    Double.parseDouble(datos[4]), Integer.parseInt(datos[5])));
        }
        return productos;
    }

    @Override
    public void insertData(Producto producto) {
        fileManage.writeFile(getDataLine(producto));
    }

    @Override
    public void updateData(Producto oldProducto, Producto newProducto) {
        fileManage.updateFile(getDataLine(oldProducto), getDataLine(newProducto));
    }

    @Override
    public void deleteData(Producto producto) {
        fileManage.deleteFile(getDataLine(producto));
    }

    @Override
    public String getDataLine(Producto producto) {
        return producto.getCodigo() + "|" + producto.getNombre() + "|" + producto.getCategoria() + "|"
                + producto.getCosto() + "|" + producto.getPrecioVenta() + "|" + producto.getStock();
    }

    //Reescribe el archivo con todos los productos de la lista
    public void guardarTodos(ArrayList<Producto> productos) {
        ArrayList<String> lines = new ArrayList<>();
        for (Producto p : productos) {
            lines.add(getDataLine(p));
        }
        fileManage.writeAllLinesInFile(lines);
    }
}

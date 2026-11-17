package repositorios;

import interfaces.IRepository;
import modelos.Cliente;
import persistencia.FileManage;

import java.util.ArrayList;

//Guarda y lee los clientes en clientes.txt, como el EstudianteRepository del profe
//Linea: documento|nombre|telefono|correo
public class ClienteRepository implements IRepository<Cliente> {

    private FileManage fileManage = new FileManage("clientes.txt");

    @Override
    public ArrayList<Cliente> getDataList() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<String> lines = fileManage.getDataOfFile();
        if (lines == null) {
            return clientes;
        }
        for (String line : lines) {
            //el -1 es para que no se pierdan los campos vacios del final
            String[] datos = line.split("\\|", -1);
            clientes.add(new Cliente(datos[0], datos[1], datos[2], datos[3]));
        }
        return clientes;
    }

    @Override
    public void insertData(Cliente cliente) {
        fileManage.writeFile(getDataLine(cliente));
    }

    @Override
    public void updateData(Cliente oldCliente, Cliente newCliente) {
        fileManage.updateFile(getDataLine(oldCliente), getDataLine(newCliente));
    }

    @Override
    public void deleteData(Cliente cliente) {
        fileManage.deleteFile(getDataLine(cliente));
    }

    @Override
    public String getDataLine(Cliente cliente) {
        return cliente.getDocumento() + "|" + cliente.getNombre() + "|" + cliente.getTelefono() + "|" + cliente.getCorreo();
    }

    //Reescribe el archivo con todos los clientes de la lista (se usa al guardar todo el programa)
    public void guardarTodos(ArrayList<Cliente> clientes) {
        ArrayList<String> lines = new ArrayList<>();
        for (Cliente c : clientes) {
            lines.add(getDataLine(c));
        }
        fileManage.writeAllLinesInFile(lines);
    }
}

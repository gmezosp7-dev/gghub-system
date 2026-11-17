package repositorios;

import enums.EstadoPuesto;
import interfaces.IRepository;
import modelos.EstacionConsola;
import modelos.EstacionPC;
import modelos.PuestoJuego;
import persistencia.FileManage;

import java.util.ArrayList;

//Guarda y lee las estaciones en puestos.txt
//Linea PC:      PC|id|modelo|tarifa|estado|especificaciones
//Linea consola: CONSOLA|id|modelo|tarifa|estado|tipoConsola|tarifaControlExtra
public class PuestoRepository implements IRepository<PuestoJuego> {

    private FileManage fileManage = new FileManage("puestos.txt");

    @Override
    public ArrayList<PuestoJuego> getDataList() {
        ArrayList<PuestoJuego> puestos = new ArrayList<>();
        ArrayList<String> lines = fileManage.getDataOfFile();
        if (lines == null) {
            return puestos;
        }
        for (String line : lines) {
            String[] datos = line.split("\\|", -1);
            int id = Integer.parseInt(datos[1]);
            double tarifa = Double.parseDouble(datos[3]);
            //el primer dato dice que tipo de estacion es, asi se sabe cual crear
            PuestoJuego puesto;
            if (datos[0].equals("PC")) {
                puesto = new EstacionPC(id, datos[2], tarifa, datos[5]);
            } else {
                puesto = new EstacionConsola(id, datos[2], tarifa, datos[5], Double.parseDouble(datos[6]));
            }
            //toda estacion nueva arranca DISPONIBLE, entonces se le pone el estado que tenia guardado
            puesto.cambiarEstado(EstadoPuesto.valueOf(datos[4]));
            puestos.add(puesto);
        }
        return puestos;
    }

    @Override
    public void insertData(PuestoJuego puesto) {
        fileManage.writeFile(getDataLine(puesto));
    }

    @Override
    public void updateData(PuestoJuego oldPuesto, PuestoJuego newPuesto) {
        fileManage.updateFile(getDataLine(oldPuesto), getDataLine(newPuesto));
    }

    @Override
    public void deleteData(PuestoJuego puesto) {
        fileManage.deleteFile(getDataLine(puesto));
    }

    //Polimorfismo: cada estacion arma su propia linea, asi no hay que preguntar si es PC o consola
    @Override
    public String getDataLine(PuestoJuego puesto) {
        return puesto.aLinea();
    }

    //Reescribe el archivo con todas las estaciones de la lista
    public void guardarTodos(ArrayList<PuestoJuego> puestos) {
        ArrayList<String> lines = new ArrayList<>();
        for (PuestoJuego p : puestos) {
            lines.add(getDataLine(p));
        }
        fileManage.writeAllLinesInFile(lines);
    }
}

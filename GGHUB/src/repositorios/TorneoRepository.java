package repositorios;

import interfaces.IRepository;
import modelos.Cliente;
import modelos.EliminacionDirecta;
import modelos.FaseGrupos;
import modelos.Inscripcion;
import modelos.ModalidadTorneo;
import modelos.Partida;
import modelos.Torneo;
import persistencia.FileManage;
import servicios.GestorClientes;

import java.time.LocalDateTime;
import java.util.ArrayList;

//Guarda y lee los torneos con sus inscripciones y sus llaves (RNF-01: "torneos con sus llaves")
//torneos.txt:      nombre|videojuego|fecha|cupoMaximo|costo|modalidad|inscripcionesAbiertas
//inscripciones.txt: torneo|documento|fecha|montoPagado
//partidas.txt:      torneo|ronda|documento1|documento2|documentoGanador|marcador   ("-" si no hay)
//Las inscripciones y partidas son del torneo, por eso las maneja este mismo repositorio
public class TorneoRepository implements IRepository<Torneo> {

    private FileManage archivoTorneos = new FileManage("torneos.txt");
    private FileManage archivoInscripciones = new FileManage("inscripciones.txt");
    private FileManage archivoPartidas = new FileManage("partidas.txt");
    private GestorClientes gestorClientes;

    //Recibe el gestor de clientes para buscar a los inscritos y a los jugadores por su documento
    public TorneoRepository(GestorClientes gestorClientes) {
        this.gestorClientes = gestorClientes;
    }

    @Override
    public ArrayList<Torneo> getDataList() {
        ArrayList<Torneo> torneos = new ArrayList<>();
        ArrayList<String> lines = archivoTorneos.getDataOfFile();
        if (lines == null) {
            return torneos;
        }
        //1. los torneos
        for (String line : lines) {
            String[] datos = line.split("\\|", -1);
            torneos.add(new Torneo(datos[0], datos[1], LocalDateTime.parse(datos[2]), Integer.parseInt(datos[3]),
                    Double.parseDouble(datos[4]), leerModalidad(datos[5]), Boolean.parseBoolean(datos[6])));
        }
        //2. las inscripciones de cada torneo
        ArrayList<String> lineasInscripciones = archivoInscripciones.getDataOfFile();
        if (lineasInscripciones != null) {
            for (String line : lineasInscripciones) {
                String[] datos = line.split("\\|", -1);
                Torneo torneo = buscarTorneo(torneos, datos[0]);
                Cliente cliente = gestorClientes.buscarPorDocumento(datos[1]);
                if (torneo != null && cliente != null) {
                    torneo.cargarInscripcion(new Inscripcion(cliente, Double.parseDouble(datos[3]), LocalDateTime.parse(datos[2])));
                }
            }
        }
        //3. las partidas: se juntan por torneo y se cargan todas juntas para que la modalidad recalcule
        ArrayList<String> lineasPartidas = archivoPartidas.getDataOfFile();
        for (Torneo torneo : torneos) {
            ArrayList<Partida> partidas = new ArrayList<>();
            if (lineasPartidas != null) {
                for (String line : lineasPartidas) {
                    String[] datos = line.split("\\|", -1);
                    if (datos[0].equals(torneo.getNombre())) {
                        partidas.add(new Partida(Integer.parseInt(datos[1]), buscarCliente(datos[2]), buscarCliente(datos[3]),
                                buscarCliente(datos[4]), leerTexto(datos[5])));
                    }
                }
            }
            if (!partidas.isEmpty()) {
                torneo.cargarPartidas(partidas);
            }
        }
        return torneos;
    }

    @Override
    public void insertData(Torneo torneo) {
        archivoTorneos.writeFile(getDataLine(torneo));
    }

    @Override
    public void updateData(Torneo oldTorneo, Torneo newTorneo) {
        archivoTorneos.updateFile(getDataLine(oldTorneo), getDataLine(newTorneo));
    }

    @Override
    public void deleteData(Torneo torneo) {
        archivoTorneos.deleteFile(getDataLine(torneo));
    }

    //la modalidad se guarda con su nombre (polimorfismo: cada una sabe como se llama)
    @Override
    public String getDataLine(Torneo torneo) {
        return torneo.getNombre() + "|" + torneo.getVideojuego() + "|" + torneo.getFecha() + "|" + torneo.getCupoMaximo()
                + "|" + torneo.getCostoInscripcion() + "|" + torneo.getModalidad().getNombre() + "|"
                + torneo.isInscripcionesAbiertas();
    }

    //Reescribe los tres archivos con todos los torneos, sus inscripciones y sus partidas
    public void guardarTodos(ArrayList<Torneo> torneos) {
        ArrayList<String> lineasTorneos = new ArrayList<>();
        ArrayList<String> lineasInscripciones = new ArrayList<>();
        ArrayList<String> lineasPartidas = new ArrayList<>();
        for (Torneo t : torneos) {
            lineasTorneos.add(getDataLine(t));
            for (Inscripcion i : t.getInscripciones()) {
                lineasInscripciones.add(t.getNombre() + "|" + i.getCliente().getDocumento() + "|" + i.getFecha() + "|"
                        + i.getMontoPagado());
            }
            for (Partida p : t.getPartidas()) {
                lineasPartidas.add(t.getNombre() + "|" + p.getRonda() + "|" + documento(p.getJugador1()) + "|"
                        + documento(p.getJugador2()) + "|" + documento(p.getGanador()) + "|" + escribirTexto(p.getMarcador()));
            }
        }
        archivoTorneos.writeAllLinesInFile(lineasTorneos);
        archivoInscripciones.writeAllLinesInFile(lineasInscripciones);
        archivoPartidas.writeAllLinesInFile(lineasPartidas);
    }

    //El texto que se guardo con getNombre() se vuelve a convertir en su modalidad
    private ModalidadTorneo leerModalidad(String nombre) {
        FaseGrupos grupos = new FaseGrupos();
        if (nombre.equals(grupos.getNombre())) {
            return grupos;
        }
        return new EliminacionDirecta();
    }

    private Torneo buscarTorneo(ArrayList<Torneo> torneos, String nombre) {
        for (Torneo t : torneos) {
            if (t.getNombre().equals(nombre)) {
                return t;
            }
        }
        return null;
    }

    //"-" significa que no hay cliente (pase directo o partida sin ganador)
    private Cliente buscarCliente(String documento) {
        if (documento.equals("-")) {
            return null;
        }
        return gestorClientes.buscarPorDocumento(documento);
    }

    private String documento(Cliente cliente) {
        if (cliente == null) {
            return "-";
        }
        return cliente.getDocumento();
    }

    private String escribirTexto(String texto) {
        if (texto == null) {
            return "-";
        }
        return texto;
    }

    private String leerTexto(String texto) {
        if (texto.equals("-")) {
            return null;
        }
        return texto;
    }
}

package persistencia;

import java.io.*;
import java.util.ArrayList;


public class FileManage {

    private String nameFile;

    public FileManage(String nameFile) {
        this.nameFile = nameFile;
    }

    public File getFile() {
        File file = new File("src/files/" + this.nameFile);
        // si la carpeta src/files todavia no existe, se crea; si no, FileWriter lanza error al escribir
        file.getParentFile().mkdirs();
        return file;
    }

    //lee todas las lineas del archivo; devuelve null si el archivo no existe
    public ArrayList<String> getDataOfFile() {
        ArrayList<String> lines = null;
        try {
            File file = this.getFile();
            if (file.exists()) {
                lines = new ArrayList<>();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line);
                    }
                }
                bufferedReader.close();
            } else {
                System.out.println("El archivo '" + nameFile + "' no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return lines;
    }

    //agrega una linea al final del archivo (true = no borra lo que ya habia)
    public void writeFile(String newLine) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile(), true));
            writer.write(newLine);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //reemplaza una linea por otra
    public void updateFile(String oldLine, String newLine) {
        ArrayList<String> lines = this.getDataOfFile();
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(oldLine)) {
                    lines.set(i, newLine);
                    this.writeAllLinesInFile(lines);
                    break;
                }
            }
        }
    }

    //borra una linea
    public void deleteFile(String line) {
        ArrayList<String> lines = this.getDataOfFile();
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(line)) {
                    lines.remove(i);
                    this.writeAllLinesInFile(lines);
                    break;
                }
            }
        }
    }

    //reescribe todo el archivo desde cero
    public void writeAllLinesInFile(ArrayList<String> lines) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

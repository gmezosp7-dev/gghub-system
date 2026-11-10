package servicios;
/**
 * Clase de utilidades para validar el formato de los datos del sistema.
 * Todos sus miembros son static porque validar no depende de ningun objeto:


 */
public class ValidadorDatos {
    // "\\d+" significa "uno o mas digitos (0-9)
    //Es static: hay una sola copia para todo el programa
    public static final String PATRON_DOCUMENTO = "\\d+";
    // Lo usa GestorClientes.registrar() antes de agregar un cliente
    public static boolean documentoValido(String documento) {
        if (documento == null) {
            return false;
        }
        return documento.matches(PATRON_DOCUMENTO);
    }

}

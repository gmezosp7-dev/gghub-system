package servicios;
/**
 * Clase de utilidades para validar el formato de los datos del sistema.
 * Todos sus miembros son static porque validar no depende de ningun objeto:


 */
public class ValidadorDatos {
    // static final: regla fija del sistema, una sola copia para todo el programa.
// Si se modificara por error, se dañarian todas las validaciones de documento.
    public static final String PATRON_DOCUMENTO = "\\d+";
    // Lo usa GestorClientes.registrar() antes de agregar un cliente
    public static boolean documentoValido(String documento) {
        if (documento == null) {
            return false;
        }
        return documento.matches(PATRON_DOCUMENTO);
    }
    public static boolean precioValido(double precio){
        return precio > 0;
    }
    public static boolean validadorNegativo(double valor){
        if(valor >= 0){
            return false;

        }
        return true;
    }

}

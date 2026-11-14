package modelos;

public class Cliente {
    // final: el documento es la identidad del cliente. Si pudiera cambiar,
// se podrian tener dos clientes con el mismo documento sin que registrar() lo detecte.
    private final String documento;
    private String nombre;
    private String telefono;
    private String correo;

// instancia cada uno de los atributos del cliente
    public Cliente (String documento, String nombre, String telefono, String correo){
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;


    }
    //metodos getters and setters



    public String getDocumento() {
        return documento;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    @Override
    public String toString() {
        return "[" + documento + "] " + nombre + " | " + telefono + " | " + correo;
    }
}

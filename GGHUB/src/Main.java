import ui.MenuPrincipal;

/**
 * Punto de entrada del sistema GG Hub (centro de e-sports: 15 PCs, 10 consolas, barra y torneos).
 *
 * Arquitectura del sistema (por capas):
 *
 *   ┌──────────────────────────────────────────────────────────┐
 *   │  CAPA UI (ui/)                                           │
 *   │  MenuPrincipal, MenuClientes, MenuPuestos, MenuSesiones, │
 *   │  MenuBarra, MenuTorneos, MenuCaja, Demo                  │
 *   ├──────────────────────────────────────────────────────────┤
 *   │  CAPA SERVICIOS (servicios/)                             │
 *   │  GestorClientes, GestorPuestos, GestorSesiones,          │
 *   │  InventarioBarra, GestorVentas, GestorTorneos,           │
 *   │  GestorCaja, ValidadorDatos                              │
 *   ├──────────────────────────────────────────────────────────┤
 *   │  CAPA PERSISTENCIA (persistencia/ + repositorios/)       │
 *   │  FileManage (lee y escribe los .txt de src/files/)       │
 *   ├──────────────────────────────────────────────────────────┤
 *   │  CAPA MODELOS (modelos/ + enums/ + interfaces/)          │
 *   │  Cliente, PuestoJuego, EstacionPC, EstacionConsola,      │
 *   │  SesionJuego, Producto, ItemConsumo, Venta, CierreCaja,  │
 *   │  Torneo, Inscripcion, Partida, PosicionGrupo,            │
 *   │  ModalidadTorneo, EliminacionDirecta, FaseGrupos,        │
 *   │  Efectivo, TarjetaCredito, PagoDigital                   │
 *   │  EstadoPuesto, TipoVenta                                 │
 *   │  CalculableCobro, MetodoPago, IRepository                │
 *   └──────────────────────────────────────────────────────────┘
 *
 * Conceptos POO demostrados:
 *   • Herencia      → EstacionPC/EstacionConsola extienden PuestoJuego;
 *                     EliminacionDirecta/FaseGrupos extienden ModalidadTorneo
 *   • Abstraccion   → PuestoJuego y ModalidadTorneo (abstract); CalculableCobro, MetodoPago, IRepository
 *   • Polimorfismo  → calcularCostoSesion() cobra distinto en PC y consola; generarCruces() y
 *                     procesarResultado() cambian por modalidad; pagar() cambia por metodo de pago
 *   • Encapsulacion → atributos private, identificadores final, listar() devuelve copias,
 *                     el estado solo cambia por metodos con reglas (ej: cerrarInscripciones())
 *   • Composicion   → SesionJuego TIENE un Cliente y un PuestoJuego; Venta TIENE ItemConsumo;
 *                     Torneo TIENE Inscripciones y Partidas
 *   • Interfaces    → MetodoPago (Efectivo, TarjetaCredito, PagoDigital), CalculableCobro, IRepository<T>
 *   • Sobrecarga    → GestorVentas.confirmar(venta) y confirmar(venta, metodo)
 *   • Enum          → EstadoPuesto (DISPONIBLE, EN_USO, EN_MANTENIMIENTO) y TipoVenta
 *   • Excepciones   → reglas de negocio con IllegalArgumentException, atrapadas en los menus
 *   • Metodos static → ValidadorDatos (documentoValido, precioValido, textoValido)
 */
public class Main {

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.mostrar();
    }
}

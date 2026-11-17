package servicios;

import modelos.CierreCaja;
import modelos.SesionJuego;
import modelos.Venta;
import modelos.Torneo;
import modelos.Inscripcion;
import enums.TipoVenta;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorCaja {
    private ArrayList<CierreCaja> cierres = new ArrayList<>();
    private final GestorSesiones gestorSesiones;
    private final GestorVentas gestorVentas;
    private final GestorTorneos gestorTorneos;

    public GestorCaja(GestorSesiones gestorSesiones, GestorVentas gestorVentas, GestorTorneos gestorTorneos) {
        this.gestorSesiones = gestorSesiones;
        this.gestorVentas = gestorVentas;
        this.gestorTorneos = gestorTorneos;
    }
    public CierreCaja cerrarCaja(String operador){
        LocalDate hoy = LocalDate.now();
        double totalJuego = 0;
        double totalControles = 0;
        double totalBarra = 0;
        double totalTorneos = 0;
        int cantidadSesiones = 0;
        int cantidadVentas = 0;
        int cantidadInscripciones = 0;
        // SESIONES cobradas hoy: tiempo, controles y sus consumos (estos van a la barra)
        for (SesionJuego s : gestorSesiones.listar()) {
            // primero "no esta activa": si estuviera abierta, getFin() es null y toLocalDate() reventaria
            if (!s.estaActiva() && s.getFin().toLocalDate().equals(hoy)) {
                totalJuego += s.getCostoTiempo();
                totalControles += s.getCostoControles();
                totalBarra += s.totalConsumos();
                cantidadSesiones++;
            }
        }
        // 2. VENTAS de CONTADO de hoy. Las de CARGO_SESION NO se suman aqui:
        // ya entraron en el paso 1, dentro de los consumos de su sesion (si no, se contarian dos veces)
        for (Venta v : gestorVentas.listar()) {
            if (v.getTipo() == TipoVenta.CONTADO && v.getFechaHora().toLocalDate().equals(hoy)) {
                totalBarra += v.calcularTotal();
                cantidadVentas++;
            }
        }
        // 3. INSCRIPCIONES de hoy: cada torneo tiene las suyas, por eso un for dentro de otro
        for (Torneo t : gestorTorneos.listar()) {
            for (Inscripcion i : t.getInscripciones()) {
                if (i.getFecha().toLocalDate().equals(hoy)) {
                    totalTorneos += i.getMontoPagado();
                    cantidadInscripciones++;
                }
            }
        }
        // 4. se crea el informe. Si el operador esta vacio, CierreCaja lanza el error
        // ANTES de agregarlo a la lista: nunca se guarda un cierre invalido
        CierreCaja cierre = new CierreCaja(operador, totalJuego, totalControles, totalBarra, totalTorneos,
                cantidadSesiones, cantidadVentas, cantidadInscripciones);
        cierres.add(cierre);
        return cierre;
    }

    public ArrayList<CierreCaja> listar(){
        return new ArrayList<>(cierres);
    }
}


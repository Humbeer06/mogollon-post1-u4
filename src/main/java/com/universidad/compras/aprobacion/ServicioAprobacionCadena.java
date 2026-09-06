package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

/**
 * Implementación de ServicioAprobacion que arma la cadena de niveles
 * de aprobación. Agregar, quitar o reordenar un nivel se hace aquí,
 * en un único lugar, sin que ControladorSolicitudes conozca cuántos
 * niveles existen ni en qué orden se consultan.
 */
@Service
public class ServicioAprobacionCadena implements ServicioAprobacion {

    private final NivelAprobacion primerNivel;

    public ServicioAprobacionCadena() {
        NivelAprobacion cumplimiento = new RevisorCumplimientoNormativo();
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente = new GerenteArea();
        NivelAprobacion director = new DirectorFinanciero();

        // El Revisor de Cumplimiento va primero: si la solicitud es
        // INTERNACIONAL, la resuelve él; si no, delega al Supervisor,
        // que a su vez delega según el monto.
        cumplimiento.enlazarCon(supervisor);
        supervisor.enlazarCon(gerente);
        gerente.enlazarCon(director);

        this.primerNivel = cumplimiento;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        return primerNivel.procesar(solicitud);
    }
}
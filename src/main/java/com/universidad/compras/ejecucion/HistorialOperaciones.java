package com.universidad.compras.ejecucion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Conserva todas las operaciones ejecutadas sobre una solicitud, no
 * solo la última, para que cualquiera pueda inspeccionarse o
 * deshacerse de forma independiente.
 */
public class HistorialOperaciones {

    private final List<OperacionEjecucion> operaciones = new ArrayList<>();

    public void registrar(OperacionEjecucion operacion) {
        operaciones.add(operacion);
    }

    public void deshacer(OperacionEjecucion operacion) {
        operacion.deshacer();
        operaciones.remove(operacion);
    }

    public List<OperacionEjecucion> consultarHistorial() {
        return Collections.unmodifiableList(operaciones);
    }

    public int size() {
        return operaciones.size();
    }
}
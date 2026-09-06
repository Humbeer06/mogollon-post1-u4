package com.universidad.compras.ejecucion;

/**
 * Command: encapsula una operación como objeto, permitiendo
 * ejecutarla y deshacerla de forma independiente, y quedar
 * registrada en un historial consultable.
 */
public interface OperacionEjecucion {
    void ejecutar();
    void deshacer();
    String getDescripcion();
}
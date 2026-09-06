package com.universidad.compras.ejecucion;

public class GenerarOrdenCompraCommand implements OperacionEjecucion {

    private final OrdenCompraService ordenCompraService;
    private final String solicitudId;
    private final String proveedor;
    private String numeroOrdenGenerada;

    public GenerarOrdenCompraCommand(OrdenCompraService ordenCompraService, String solicitudId, String proveedor) {
        this.ordenCompraService = ordenCompraService;
        this.solicitudId = solicitudId;
        this.proveedor = proveedor;
    }

    @Override
    public void ejecutar() {
        this.numeroOrdenGenerada = ordenCompraService.generar(solicitudId, proveedor);
    }

    @Override
    public void deshacer() {
        ordenCompraService.cancelar(numeroOrdenGenerada);
    }

    @Override
    public String getDescripcion() {
        return "Orden de compra para " + solicitudId + " con proveedor " + proveedor;
    }
}
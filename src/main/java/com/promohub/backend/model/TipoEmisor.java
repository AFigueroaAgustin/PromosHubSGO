package com.promohub.backend.model;

public enum TipoEmisor {
    TARJETA_REGIONAL("Tarjetas Regionales"),
    BANCO_TRADICIONAL("Bancos"),
    BILLETERA_VIRTUAL("Billeteras Virtuales y QR");

    private final String descripcion;

    TipoEmisor(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoEmisor desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return BANCO_TRADICIONAL;
        }

        String t = texto.toUpperCase();

        if (t.contains("MODO") || t.contains("NAVE") || t.contains("VIÜMI") || t.contains("VIUMI")
                || t.contains("MERCADO PAGO") || t.contains("PERSONAL PAY") || t.contains("UALA") || t.contains("UALÁ")
                || t.contains("CLARO PAY") || t.contains("SOL PAGO") || t.contains("BILLETERA")) {
            return BILLETERA_VIRTUAL;
        }

        if (t.contains("TARJETA") || t.contains("SOL") || t.contains("UNICA") || t.contains("ÚNICA")
                || t.contains("SUCREDITO") || t.contains("SUCRÉDITO") || t.contains("NARANJA") || t.contains("CREDICASH")) {
            return TARJETA_REGIONAL;
        }

        return BANCO_TRADICIONAL;
    }
}

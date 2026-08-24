package com.promohub.backend.model;

public enum Categoria {

    SUPERMERCADOS("Supermercados y Alimentos"),
    TECNOLOGIA_Y_ELECTRO("Tecnología y Electro"),
    PELUQUERIA_Y_ESTETICA("Peluquería y Spa"),
    HOGAR_Y_CONSTRUCCION("Hogar y Construcción"),
    GASTRONOMIA("Gastronomía y Bares"),
    FARMACIA("Farmacias y Salud"),
    COMBUSTIBLE("Combustibles"),
    INDUMENTARIA("Indumentaria y Calzado"),
    VARIOS("Otros Rubros");

    private final String descripcionVisual;

    Categoria(String descripcionVisual) {
        this.descripcionVisual = descripcionVisual;
    }

    public String getDescripcionVisual() {
        return descripcionVisual;
    }

    public static Categoria desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return VARIOS;
        }
        String t = texto.toUpperCase().trim();
        if (t.contains("PELUQUERIA") || t.contains("SPA") || t.contains("ESTETICA")) {
            return PELUQUERIA_Y_ESTETICA;
        }
        if (t.contains("ELECTRO") || t.contains("TECNOLOGIA")) {
            return TECNOLOGIA_Y_ELECTRO;
        }
        if (t.contains("SUPER") || t.contains("ALIMENTO")) {
            return SUPERMERCADOS;
        }
        if (t.contains("RESTO") || t.contains("GASTRONOM") || t.contains("PASTELERIA") || t.contains("BAR")) {
            return GASTRONOMIA;
        }
        if (t.contains("FARMACIA") || t.contains("SALUD")) {
            return FARMACIA;
        }
        if (t.contains("COMBUSTIBLE")) {
            return COMBUSTIBLE;
        }
        if (t.contains("INDUMENTARIA") || t.contains("MODA") || t.contains("CALZADO")) {
            return INDUMENTARIA;
        }
        if (t.contains("HOGAR") || t.contains("CONSTRUCCION")) {
            return HOGAR_Y_CONSTRUCCION;
        }

        try {
            return Categoria.valueOf(t.replace(" ", "_"));
        } catch (Exception e) {
            return VARIOS;
        }
    }
}

package com.mdmc.posofmyheart.domain;

/**
 * Enumeración para representar los estados de una orden.
 */
public enum OrderStatus {
    RECEIVED(1, "RECEIVED"),
    COMPLETED(2, "COMPLETED");

    // Getters para los campos
    final int status;
    final String name;

    OrderStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    /**
     * Busca y retorna un OrderStatus que coincida con el ID proporcionado.
     *
     * @param id El ID numérico del estado (campo 'status').
     * @return El {@link OrderStatus} correspondiente o {@code null} si no se encuentra.
     */
    public static OrderStatus getById(int id) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.status == id) {
                return orderStatus;
            }
        }
        return null;
    }

    /**
     * Busca y retorna un OrderStatus que coincida con el nombre proporcionado.
     * La comparación no distingue entre mayúsculas y minúsculas.
     *
     * @param name El nombre del estado a buscar (campo 'name').
     * @return El {@link OrderStatus} correspondiente o {@code null} si no se encuentra.
     */
    public static OrderStatus getByName(String name) {
        if (name == null) {
            return null;
        }
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.name.equalsIgnoreCase(name)) {
                return orderStatus;
            }
        }
        return null;
    }
}
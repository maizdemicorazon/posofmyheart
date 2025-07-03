package com.mdmc.posofmyheart.domain;

/**
 * Enumeración para representar los estados de una orden.
 */
public enum OrderStatusEnum {
    RECEIVED(1, "RECEIVED"),
    ATTENDING(1, "ATTENDING"),
    COMPLETED(2, "COMPLETED");

    // Getters para los campos
    final int status;
    final String name;

    OrderStatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    /**
     * Busca y retorna un OrderStatus que coincida con el ID proporcionado.
     *
     * @param id El ID numérico del estado (campo 'status').
     * @return El {@link OrderStatusEnum} correspondiente o {@code null} si no se encuentra.
     */
    public static OrderStatusEnum getById(int id) {
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.status == id) {
                return orderStatusEnum;
            }
        }
        return null;
    }

    /**
     * Busca y retorna un OrderStatus que coincida con el nombre proporcionado.
     * La comparación no distingue entre mayúsculas y minúsculas.
     *
     * @param name El nombre del estado a buscar (campo 'name').
     * @return El {@link OrderStatusEnum} correspondiente o {@code null} si no se encuentra.
     */
    public static OrderStatusEnum getByName(String name) {
        if (name == null) {
            return null;
        }
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.name.equalsIgnoreCase(name)) {
                return orderStatusEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
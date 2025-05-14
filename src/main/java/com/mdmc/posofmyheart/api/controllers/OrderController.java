package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Listado de ordenes.",
            description = "Recupera todas las ordenes creadas."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista recuperada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @Operation(
            summary = "Obtiene una orden.",
            description = "Recupera una orden con id como parametro"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long idOrder) {
        return ResponseEntity.ok(orderService.findOrderById(idOrder));
    }

    @Operation(
            summary = "Listado de ordes por fecha",
            description = "Recupera una lista de ordenes usando como parametro una fecha dada con formato yyyy-MM-dd"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/date/{date}")
    public ResponseEntity<List<OrderResponse>> getOrdersByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(orderService.listOrdersByDate(date));
    }

    @Operation(
            summary = "Creación de orden",
            description = "Crea una orden con detalles y extras."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Orden creada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        try {
            return ResponseEntity.ok(
                    orderService.createOrder(request)
            );
        } catch (Exception e) {
            log.error("Error al crear la orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

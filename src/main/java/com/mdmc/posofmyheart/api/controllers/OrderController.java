package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
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
            description = "Recupera una orden con idProduct como parametro"
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
    @GetMapping("/by-date/{date}")
    public ResponseEntity<List<OrderResponse>> getOrdersByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(orderService.listOrdersByDate(date));
    }

    @Operation(
            summary = "Crear de una orden",
            description = "Crea una orden con detalles y extras."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Orden creada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        orderService.createOrder(orderRequest)
                );
    }

    @Operation(
            summary = "Crear una lista de ordenes",
            description = "Crea una lista de ordenes con detalles y extras."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Ordenes creadas"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )

    @PostMapping("/batch")
    public ResponseEntity<List<CreateOrderResponseDto>> createOrders(
            @Valid @RequestBody List<OrderRequest> orderRequests) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        orderService.createOrders(orderRequests)
                );
    }

    @Operation(
            summary = "Actualizar una orden",
            description = "Actualiza y valida relaciones para una orden existente"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Orden actualizada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateRequest updateRequest) {
        OrderResponse updatedOrder = orderService.updateOrder(id, updateRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

}

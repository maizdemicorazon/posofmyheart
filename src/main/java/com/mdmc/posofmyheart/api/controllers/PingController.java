package com.mdmc.posofmyheart.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Ping class is a REST controller that provides an endpoint for a simple health check
 * or status response. It acts as a mechanism to verify that the service is reachable and
 * operational by responding with a predefined string.
 * The controller exposes the following functionality:
 * - A GET endpoint that responds with a "pong!" message when accessed.
 */
@RestController
public class PingController {


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("ping")
    public String ping() {
        return "pong!";
    }

}

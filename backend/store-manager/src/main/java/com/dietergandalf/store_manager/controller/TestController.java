package com.dietergandalf.store_manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Test Controller", description = "Test endpoints for system health and connectivity")
public class TestController {

    @GetMapping("/")
    @Operation(summary = "Get application status", description = "Returns basic application information and status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is running",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Store Manager Backend is running!");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns application health status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is healthy",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "store-manager");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/actuator/health")
    @Operation(summary = "Actuator health check", description = "Returns application health status (actuator-compatible)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is healthy",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> actuatorHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "store-manager");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/test")
    @Operation(summary = "CORS test endpoint", description = "Test endpoint to verify CORS configuration and backend connectivity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CORS test successful",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "CORS test successful!");
        response.put("backend", "accessible");
        return ResponseEntity.ok(response);
    }
}

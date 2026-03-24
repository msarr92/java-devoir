package com.groupeisi.achat_en_ligne_ms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 * Cette classe permet de gérer les exceptions globales
 * dans toute l'application Spring Boot.
 * Elle évite d'écrire la gestion d'erreur dans chaque controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Cette méthode intercepte les exceptions de type ResourceNotFoundException.
     * Elle est utilisée lorsque une ressource demandée n'existe pas dans la base de données.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {

        // Retourne une réponse HTTP 404 (NOT_FOUND)
        // avec un message d'erreur dans le body
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    /*
     * Cette méthode intercepte toutes les autres exceptions
     * non gérées dans l'application.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex) {

        // Retourne une réponse HTTP 500 (INTERNAL_SERVER_ERROR)
        // avec le message de l'exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", ex.getMessage()));
    }
}
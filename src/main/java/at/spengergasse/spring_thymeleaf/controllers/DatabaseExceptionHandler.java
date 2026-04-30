package at.spengergasse.spring_thymeleaf.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(DatabaseExceptionHandler.class);

    @ExceptionHandler({ CannotGetJdbcConnectionException.class, DataAccessResourceFailureException.class })
    public String handleDbConnection(Throwable ex, Model model) {
        log.error("Datenbankverbindung fehlgeschlagen", ex);
        model.addAttribute("dbErrorMessage", "Datenbankverbindung konnte nicht hergestellt werden. Bitte prüfen Sie, ob der MS SQL Server läuft.");
        return "db_unavailable";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccess(DataAccessException ex, Model model) {
        log.error("Datenbankzugriffsfehler", ex);
        model.addAttribute("dbErrorMessage", "Datenbankfehler: " + ex.getMostSpecificCause().getMessage());
        return "db_unavailable";
    }
}
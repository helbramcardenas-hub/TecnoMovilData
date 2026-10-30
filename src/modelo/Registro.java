package modelo;

import java.time.LocalDateTime;

public record Registro(String idUsuario, String ruta, String estacion, String accion, LocalDateTime timestamp) {
}

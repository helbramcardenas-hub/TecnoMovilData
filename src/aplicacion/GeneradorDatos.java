package aplicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.IntStream;
import modelo.Registro;

public class GeneradorDatos {

    private static final List<String> RUTAS = List.of("R1", "R2", "R3", "R4", "R5");
    private static final List<String> ESTACIONES = List.of("Central", "Norte", "Sur", "Oriente", "Occidente", "Universidad", "Hospital", "Terminal");
    private static final List<String> ACCIONES = List.of("entrada", "salida");
    private static final LocalDateTime INICIO_DIA = LocalDate.of(2026, 10, 29).atStartOfDay();

    public static List<Registro> generar(int cantidad) {
        return IntStream.range(0, cantidad)
                .mapToObj(GeneradorDatos::crearRegistro)
                .sorted(Comparator.comparing(Registro::timestamp))
                .toList();
    }

    private static Registro crearRegistro(int indice) {
        SplittableRandom azar = new SplittableRandom(indice);
        String idUsuario = "U" + (1 + azar.nextInt(500));
        String ruta = RUTAS.get(azar.nextInt(RUTAS.size()));
        String estacion = ESTACIONES.get(azar.nextInt(ESTACIONES.size()));
        String accion = ACCIONES.get(azar.nextInt(ACCIONES.size()));
        LocalDateTime timestamp = INICIO_DIA.plusSeconds(azar.nextInt(24 * 60 * 60));
        return new Registro(idUsuario, ruta, estacion, accion, timestamp);
    }
}
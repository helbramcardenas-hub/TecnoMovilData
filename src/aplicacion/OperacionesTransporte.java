package aplicacion;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import modelo.Registro;
import java.util.TreeMap;

public class OperacionesTransporte {

    public static Map<String, Long> afluenciaPorEstacion(List<Registro> registros) {
        return registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.groupingBy(Registro::estacion, Collectors.counting()));
    }
    
        public static Map<Integer, Long> flujoPorHora(List<Registro> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(r -> r.timestamp().getHour(), TreeMap::new, Collectors.counting()));
    }

    public static List<Map.Entry<Integer, Long>> horasPico(List<Registro> registros, int cantidad) {
        return flujoPorHora(registros).entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(cantidad)
                .toList();
    }
    
        public static List<Map.Entry<String, Long>> rutasMasUtilizadas(List<Registro> registros, int cantidad) {
        return registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.groupingBy(Registro::ruta, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(cantidad)
                .toList();
    
    }
    
        public static Map<String, List<String>> patronesDeViaje(List<Registro> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        Registro::idUsuario,
                        TreeMap::new,
                        Collectors.mapping(Registro::estacion, Collectors.toUnmodifiableList())));
    }
    
        private record Tramo(Registro origen, Registro destino) {
    }

    private static Stream<Tramo> tramosConsecutivos(List<Registro> registrosDelUsuario) {
        return IntStream.range(0, registrosDelUsuario.size() - 1)
                .mapToObj(i -> new Tramo(registrosDelUsuario.get(i), registrosDelUsuario.get(i + 1)));
    }

    public static Map<String, Double> tiempoPromedioEntreEstaciones(List<Registro> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(Registro::idUsuario))
                .values().stream()
                .flatMap(OperacionesTransporte::tramosConsecutivos)
                .filter(t -> !t.origen().estacion().equals(t.destino().estacion()))
                .collect(Collectors.groupingBy(
                        t -> t.origen().estacion() + " -> " + t.destino().estacion(),
                        TreeMap::new,
                        Collectors.averagingDouble(t -> Duration.between(t.origen().timestamp(), t.destino().timestamp()).getSeconds() / 60.0)));
    }
    
        public static Map<String, String> estadoDeRutas(List<Registro> registros, long umbral) {
        return registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.groupingBy(Registro::ruta, TreeMap::new, Collectors.counting()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() > umbral ? "CRITICA" : "NORMAL",
                        (a, b) -> a,
                        TreeMap::new));
    }
}
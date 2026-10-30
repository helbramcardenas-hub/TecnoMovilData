package aplicacion;

import java.util.List;
import modelo.Registro;

public class Main {

    public static void main(String[] args) {
        List<Registro> registros = GeneradorDatos.generar(100000);
        System.out.println("Total de registros: " + registros.size());

        System.out.println("\na) Afluencia por estacion:");
        OperacionesTransporte.afluenciaPorEstacion(registros)
                .forEach((estacion, total) -> System.out.println(estacion + ": " + total));

        System.out.println("\nb) Flujo por hora:");
        OperacionesTransporte.flujoPorHora(registros)
                .forEach((hora, total) -> System.out.println(hora + "h: " + total));
        System.out.println("Top 3 horas pico:");
        OperacionesTransporte.horasPico(registros, 3)
                .forEach(e -> System.out.println(e.getKey() + "h: " + e.getValue()));

        System.out.println("\nc) Rutas mas utilizadas:");
        OperacionesTransporte.rutasMasUtilizadas(registros, 5)
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println("\nd) Patron de viaje de U1 (primeras 10 estaciones):");
        System.out.println(OperacionesTransporte.patronesDeViaje(registros).get("U1").stream().limit(10).toList());
        System.out.println("Usuarios distintos: " + OperacionesTransporte.patronesDeViaje(registros).size());

        System.out.println("\ne) Tiempo promedio entre estaciones (primeros 5 trayectos):");
        OperacionesTransporte.tiempoPromedioEntreEstaciones(registros).entrySet().stream()
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + ": " + String.format("%.2f", e.getValue()) + " min"));

        System.out.println("\nf) Estado de rutas (umbral 10100 entradas):");
        OperacionesTransporte.estadoDeRutas(registros, 10100)
                .forEach((ruta, estado) -> System.out.println(ruta + ": " + estado));
    }
}


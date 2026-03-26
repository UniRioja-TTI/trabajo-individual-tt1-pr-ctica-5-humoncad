package com.tt1.trabajo.utilidades;

import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Punto;
import modelo.RespuestaResultados;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClaseConsumidoraServicio {

    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(ClaseConsumidoraServicio.class);

    private final String usuario = "humoncad";

    public ClaseConsumidoraServicio() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public int enviarSolicitudSimulacion(DatosSolicitud solicitud) {
        try {
            logger.info("Enviando solicitud de simulación al servidor externo...");

            Map<String, Object> cuerpoPeticion = new HashMap<>();
            List<Integer> cantidades = new ArrayList<>();
            List<String> nombres = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : solicitud.getNums().entrySet()) {
                cantidades.add(entry.getValue());
                nombres.add("Entidad " + entry.getKey());
            }
            cuerpoPeticion.put("cantidadesIniciales", cantidades);
            cuerpoPeticion.put("nombreEntidades", nombres);

            Map respuestaJson = restClient.post()
                    .uri("/Solicitud/Solicitar?nombreUsuario={user}", usuario)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpoPeticion)
                    .retrieve()
                    .body(Map.class);

            if (respuestaJson != null && respuestaJson.get("tokenSolicitud") != null) {
                String tokenStr = String.valueOf(respuestaJson.get("tokenSolicitud"));
                int token = Integer.parseInt(tokenStr);

                logger.info("¡Token extraído con éxito!: " + token);
                return token;
            }

            return -1;

        } catch (Exception e) {
            logger.error("Error al comunicar con la API externa para solicitar simulación: " + e.getMessage());
            return -1;
        }
    }

    public DatosSimulation obtenerResultadosSimulacion(int token) {
        try {
            logger.info("Solicitando datos de simulación para el token: " + token);

            RespuestaResultados respuesta = restClient.post()
                    .uri("/Resultados?nombreUsuario={user}&tok={token}", usuario, token)
                    .retrieve()
                    .body(RespuestaResultados.class);

            if (respuesta == null || respuesta.getData() == null) {
                logger.warn("Respuesta vacía o sin datos");
                return null;
            }

            logger.info("Datos recibidos correctamente, parseando...");
            return parsearTextoASimulacion(respuesta.getData());

        } catch (Exception e) {
            logger.error("Error al descargar los datos: " + e.getMessage());
            return null;
        }
    }

    private DatosSimulation parsearTextoASimulacion(String texto) {
        DatosSimulation ds = new DatosSimulation();
        Map<Integer, List<Punto>> mapaPuntos = new HashMap<>();
        int tiempoMaximo = 0;

        String[] lineas = texto.split("\\r?\\n");
        if (lineas.length == 0) return ds;

        try {
            ds.setAnchoTablero(Integer.parseInt(lineas[0].trim()));
        } catch (NumberFormatException e) {
            logger.error("No se pudo leer el ancho del tablero: " + lineas[0]);
            return ds;
        }

        for (int i = 1; i < lineas.length; i++) {
            String linea = lineas[i].trim();
            if (linea.isEmpty()) continue;

            String[] partes = linea.split(",");
            if (partes.length == 4) {
                try {
                    int tiempo = Integer.parseInt(partes[0].trim());
                    int y = Integer.parseInt(partes[1].trim());
                    int x = Integer.parseInt(partes[2].trim());
                    String color = partes[3].trim();

                    Punto p = new Punto();
                    p.setX(x);
                    p.setY(y);
                    p.setColor(color);

                    mapaPuntos.computeIfAbsent(tiempo, k -> new ArrayList<>()).add(p);

                    if (tiempo > tiempoMaximo) {
                        tiempoMaximo = tiempo;
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Saltando línea mal formateada: " + linea);
                }
            }
        }

        ds.setMaxSegundos(tiempoMaximo + 1);
        ds.setPuntos(mapaPuntos);

        return ds;
    }
}
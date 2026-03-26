package servicios;

import com.tt1.trabajo.utilidades.ClaseConsumidoraServicio;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContactoSimTest {

    private ContactoSim contactoSim;

    @BeforeEach
    void setUp() {
        ClaseConsumidoraServicio apiFalsa = new ClaseConsumidoraServicio() {
            @Override
            public int enviarSolicitudSimulacion(DatosSolicitud solicitud) {
                return 10;
            }

            @Override
            public DatosSimulation obtenerResultadosSimulacion(int token) {
                return null;
            }
        };

        contactoSim = new ContactoSim(apiFalsa);
    }

    @Test
    void solicitarSimulation() {

        Map<Integer, Integer> datosMap = new HashMap<>();
        datosMap.put(1, 10);

        int token = contactoSim.solicitarSimulation(new DatosSolicitud(datosMap));

        assertTrue(token >= 0);
    }

    @Test
    void descargarDatos() {
        DatosSimulation resultado = contactoSim.descargarDatos(999999);

        assertNull(resultado);
    }

    @Test
    void getEntities() {
        List<Entidad> entidades = contactoSim.getEntities();

        assertNotNull(entidades);
        assertEquals(3, entidades.size());
    }

    @Test
    void isValidEntityId() {
        assertTrue(contactoSim.isValidEntityId(3));
        assertFalse(contactoSim.isValidEntityId(5));
    }
}
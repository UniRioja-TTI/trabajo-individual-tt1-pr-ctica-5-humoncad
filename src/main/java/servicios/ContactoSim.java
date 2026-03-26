package servicios;

import com.tt1.trabajo.utilidades.ClaseConsumidoraServicio;
import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoSim implements InterfazContactoSim {

    private final ClaseConsumidoraServicio apiExterna;

    private final List<Entidad> entidades = List.of(
            new Entidad(1, "Entidad A"),
            new Entidad(2, "Entidad B"),
            new Entidad(3, "Entidad C")
    );

    public ContactoSim(ClaseConsumidoraServicio apiExterna) {
        this.apiExterna = apiExterna;
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        return apiExterna.enviarSolicitudSimulacion(sol);
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        return apiExterna.obtenerResultadosSimulacion(ticket);
    }

    @Override
    public List<Entidad> getEntities() {
        return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return entidades.stream().anyMatch(e -> e.getId() == id);
    }
}
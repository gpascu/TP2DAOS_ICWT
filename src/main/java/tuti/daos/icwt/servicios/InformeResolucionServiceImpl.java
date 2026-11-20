
package tuti.daos.icwt.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import tuti.daos.icwt.accesoDatos.IHistorialIncidenteRepo;
import tuti.daos.icwt.accesoDatos.IIncidenteRepo;
import tuti.daos.icwt.accesoDatos.IInformeResolucionRepo;
import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.HistorialEstadoIncidente;
import tuti.daos.icwt.entidades.Incidente;
import tuti.daos.icwt.entidades.InformeResolucion;
import tuti.daos.icwt.presentacion.informes.InformeResolucionDTO;

@Service
public class InformeResolucionServiceImpl
        implements InformeResolucionService {

    private final IInformeResolucionRepo informeRepo;
    private final IIncidenteRepo incidenteRepo;
    private final IHistorialIncidenteRepo historialRepo;

    public InformeResolucionServiceImpl(
            IInformeResolucionRepo informeRepo,
            IIncidenteRepo incidenteRepo,
            IHistorialIncidenteRepo historialRepo) {

        this.informeRepo = informeRepo;
        this.incidenteRepo = incidenteRepo;
        this.historialRepo = historialRepo;
    }

    @Override
    @Transactional
    public InformeResolucion crearInforme(
            InformeResolucionDTO datos) {

        // 1. Buscar el incidente
        Incidente incidente = incidenteRepo
        .buscarPorIdConBloqueo(datos.getIncidenteId())
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "El incidente no existe"
            ));

        // 2. Comprobar su estado
        EstadoIncidente estadoAnterior = incidente.getEstado();
        if (estadoAnterior != EstadoIncidente.EN_PROCESO) {
           throw new ResponseStatusException(
           HttpStatus.CONFLICT,
           "El incidente debe estar EN_PROCESO"
    );
}

        // 3. Obtener la fecha y hora
        LocalDateTime fechaHora = LocalDateTime.now();

        // 4. Crear el informe
        InformeResolucion informe = new InformeResolucion();

        informe.setIncidente(incidente);
        informe.setDescripcionTrabajo(
                datos.getDescripcionTrabajo());
        informe.setCosto(datos.getCosto());
        informe.setObservaciones(datos.getObservaciones());
        informe.setResponsable(datos.getResponsable());
        informe.setFechaHoraResolucion(fechaHora);

        // 5. Cambiar el estado del incidente
        incidente.setEstado(EstadoIncidente.RESUELTO);

        // 6. Registrar el historial
        HistorialEstadoIncidente historial =
                new HistorialEstadoIncidente(
                    incidente,
                    estadoAnterior,
                    EstadoIncidente.RESUELTO,
                    fechaHora
                );

        // 7. Guardar los cambios
        incidenteRepo.save(incidente);
        historialRepo.save(historial);

        return informeRepo.save(informe);
    }

    
    @Override
    @Transactional(readOnly = true)
     public InformeResolucion obtenerPorId(Long id) {

          return informeRepo.findById(id)
                .orElseThrow(() ->
                   new ResponseStatusException(
                   HttpStatus.NOT_FOUND,
                   "El informe no existe"
                ));
}


    @Override
    @Transactional(readOnly = true)
    public List<InformeResolucion> listarInformes(
            Long incidenteId) {

        if (incidenteId != null) {
            return informeRepo
                .findByIncidenteIdOrderByFechaHoraResolucionAsc(
                    incidenteId);
        }

        return informeRepo.findAll();
    }
}

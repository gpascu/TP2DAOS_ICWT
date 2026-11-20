
package tuti.daos.icwt.servicios;

import java.util.List;

import tuti.daos.icwt.entidades.InformeResolucion;
import tuti.daos.icwt.presentacion.informes.InformeResolucionDTO;

public interface InformeResolucionService {

    InformeResolucion crearInforme(InformeResolucionDTO datos);

    InformeResolucion obtenerPorId(Long id);

    List<InformeResolucion> listarInformes(Long incidenteId);
}

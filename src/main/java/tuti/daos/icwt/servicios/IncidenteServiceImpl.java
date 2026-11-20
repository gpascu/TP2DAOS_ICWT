package tuti.daos.icwt.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.daos.icwt.accesoDatos.IContratoRepo;
import tuti.daos.icwt.accesoDatos.IIncidenteRepo;
import tuti.daos.icwt.entidades.CategoriaIncidente;
import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.entidades.EstadoContrato;
import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.HistorialEstadoIncidente;
import tuti.daos.icwt.entidades.Incidente;
import tuti.daos.icwt.entidades.PrioridadIncidente;
import tuti.daos.icwt.excepciones.Excepcion;

@Service
@Transactional(readOnly = true)
public class IncidenteServiceImpl implements IncidenteService {

	private final IIncidenteRepo incidenteRepo;
	private final IContratoRepo contratoRepo;

	public IncidenteServiceImpl(IIncidenteRepo incidenteRepo, IContratoRepo contratoRepo) {
		this.incidenteRepo = incidenteRepo;
		this.contratoRepo = contratoRepo;
	}

	@Override
	@Transactional(rollbackFor = Excepcion.class)
	public Incidente registrar(Long contratoId, String titulo, String descripcion, CategoriaIncidente categoria,
			PrioridadIncidente prioridad) throws Excepcion {
		Contrato contrato = contratoRepo.findById(contratoId)
				.orElseThrow(() -> new Excepcion("contratoId", "El contrato indicado no existe.", 404));
		LocalDateTime fechaHoraAlta = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
		LocalDate fechaAlta = fechaHoraAlta.toLocalDate();

		// El primer y el último día cuentan como parte del contrato.
		if (contrato.isEliminado() || contrato.getEstado() != EstadoContrato.ACTIVO
				|| fechaAlta.isBefore(contrato.getFechaInicio()) || fechaAlta.isAfter(contrato.fechaFin())) {
			throw new Excepcion("contratoId", "El contrato debe estar activo, vigente y sin eliminar.", 409);
		}

		Incidente incidente = new Incidente();
		incidente.setContrato(contrato);
		incidente.setTitulo(titulo.strip());
		incidente.setDescripcion(descripcion.strip());
		incidente.setCategoria(categoria);
		incidente.setPrioridad(prioridad);
		incidente.setFechaHoraAlta(fechaHoraAlta);
		incidente.registrarCambioEstado(EstadoIncidente.ABIERTO, fechaHoraAlta);
		// JPA guarda el historial junto con el incidente por la cascada.
		return incidenteRepo.save(incidente);
	}

	@Override
	public Incidente getById(Long id) throws Excepcion {
		return incidenteRepo.findById(id)
				.orElseThrow(() -> new Excepcion("id", "El incidente indicado no existe.", 404));
	}

	@Override
	public List<Incidente> listar(Long contratoId, EstadoIncidente estado, CategoriaIncidente categoria,
			PrioridadIncidente prioridad) {
		return incidenteRepo.listar(contratoId, estado, categoria, prioridad);
	}

	@Override
	@Transactional(rollbackFor = Excepcion.class)
	public Incidente cambiarEstado(Long id, EstadoIncidente nuevoEstado) throws Excepcion {
		Incidente incidente = incidenteRepo.buscarPorIdConBloqueo(id)
				.orElseThrow(() -> new Excepcion("id", "El incidente indicado no existe.", 404));

		// La vigencia se controla al dar el alta. El seguimiento puede continuar después.
		EstadoIncidente estadoActual = incidente.getEstado();
		if (!transicionPermitida(estadoActual, nuevoEstado)) {
			throw new Excepcion("estado", "No se puede pasar de " + estadoActual + " a " + nuevoEstado + ".", 409);
		}

		incidente.registrarCambioEstado(nuevoEstado, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
		return incidente;
	}

	@Override
	public List<HistorialEstadoIncidente> consultarHistorial(Long id) throws Excepcion {
		// Copiamos la lista mientras JPA todavía puede traer el historial.
		return List.copyOf(getById(id).getHistorial());
	}

	private boolean transicionPermitida(EstadoIncidente actual, EstadoIncidente nuevo) {
		// RESUELTO se alcanza desde S04, cuando se guarda el informe.
		return switch (actual) {
			case ABIERTO -> nuevo == EstadoIncidente.EN_PROCESO || nuevo == EstadoIncidente.CANCELADO;
			case EN_PROCESO -> nuevo == EstadoIncidente.CANCELADO;
			case RESUELTO -> nuevo == EstadoIncidente.REABIERTO;
			case REABIERTO -> nuevo == EstadoIncidente.EN_PROCESO;
			case CANCELADO -> false;
		};
	}
}

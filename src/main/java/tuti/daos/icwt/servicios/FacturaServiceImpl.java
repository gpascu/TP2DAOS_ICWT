package tuti.daos.icwt.servicios;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.daos.icwt.accesoDatos.IFacturaRepo;
import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.entidades.EstadoContrato;
import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.facturas.FacturaDTO;


@Service
public class FacturaServiceImpl implements FacturaService {

    // Spring asigna el repositorio para acceder a las facturas.
    @Autowired
    private IFacturaRepo repo;

    // Spring asigna el servicio para consultar los contratos.
    @Autowired
    private ContratoService contratoService;

    @Override
    public Long emitir(FacturaDTO dto) throws Excepcion {

        // Busca el contrato indicado.
        Optional<Contrato> rta = contratoService.getById(dto.getIdContrato());

        // Si el contrato no existe, lanza un error.
        if (!rta.isPresent())
            throw new Excepcion("idContrato", "El contrato indicado no existe.", 404);

        // Obtiene el contrato encontrado dentro del Optional.
        Contrato contrato = rta.get();

        // Impide crear facturas para contratos eliminados.
        if (contrato.isEliminado())
            throw new Excepcion("idContrato", "El contrato indicado fue eliminado.", 409);

        // Comprueba que el contrato esté activo.
        if (contrato.getEstado() != EstadoContrato.ACTIVO)
            throw new Excepcion("idContrato", "El contrato no se encuentra activo.", 409);

        // Convierte el texto del período en un año y mes.
        YearMonth periodo = YearMonth.parse(dto.getPeriodo());

        // Obtiene el primer y el último día del mes.
        LocalDate primerDiaMes = periodo.atDay(1);
        LocalDate ultimoDiaMes = periodo.atEndOfMonth();

        // Comprueba que al menos un día del mes esté dentro de las fechas del contrato.
        if (ultimoDiaMes.isBefore(contrato.getFechaInicio()) || primerDiaMes.isAfter(contrato.fechaFin()))
            throw new Excepcion("periodo", "El mes facturado no tiene ningun dia dentro de la vigencia del contrato.", 409);

        // Si no se indica un importe, la factura es mensual.
        boolean esMensual = dto.getImporte() == null;

        if (esMensual) {

            // Busca facturas mensuales del mismo contrato y período que no estén anuladas.
            List<Factura> existentes = repo.findByContratoIdAndPeriodoAndMensualAndEstadoNot(
                    contrato.getId(),
                    dto.getPeriodo(),
                    true,
                    EstadoFactura.ANULADA);

            // Impide crear otra factura mensual para el mismo contrato y período.
            if (!existentes.isEmpty())
                throw new Excepcion("periodo","Ya existe una factura de facturacion mensual para ese contrato y periodo.",409);
        }

        // Crea la factura.
        Factura f = new Factura();

        // Asigna el contrato.
        f.setContrato(contrato);

        // Asigna el período facturado.
        f.setPeriodo(dto.getPeriodo());

        // Indica si la factura es mensual o adicional.
        f.setMensual(esMensual);

        // La factura comienza con estado pendiente.
        f.setEstado(EstadoFactura.PENDIENTE);

        // Usa el concepto recibido si tiene texto; si no, coloca uno por defecto.
        if (dto.getConcepto() != null && !dto.getConcepto().isBlank())
            f.setConcepto(dto.getConcepto());
        else
            f.setConcepto("Alquiler mensual " + dto.getPeriodo());

        // Usa la fecha de emisión recibida o, si no hay una, la fecha de hoy.
        LocalDate emision = dto.getFechaEmision() != null
                ? dto.getFechaEmision()
                : LocalDate.now();

        f.setFechaEmision(emision);

        // Usa el vencimiento recibido o lo calcula con el día indicado en el contrato.
        if (dto.getFechaVencimiento() != null)
            f.setFechaVencimiento(dto.getFechaVencimiento());
        else
            f.setFechaVencimiento(
                    calcularVencimiento(periodo, contrato.getDiaVencimiento()));

        // Impide que la emisión sea después del vencimiento.
        if (f.getFechaEmision().isAfter(f.getFechaVencimiento()))
            throw new Excepcion( "fechaEmision", "La fecha de emision no puede ser posterior al vencimiento.",400);

        // Si la factura es mensual, usa el importe del contrato; si no, usa el recibido.
        if (esMensual) {
            f.setImporte(contrato.getImporteMensual());
        } else {
            f.setImporte(dto.getImporte());
        }

        // Guarda la factura en la base de datos.
        Factura guardada = repo.save(f);

        // Devuelve el ID de la factura guardada.
        return guardada.getId();
    }

    @Override
    public Optional<Factura> getById(Long id) {

        // Busca por ID; devuelve un Optional vacío si la factura no existe.
        return repo.findById(id);
    }

    @Override
    public List<Factura> listar(Long idContrato,
            String periodo,
            EstadoFactura estado) {

        // Crea la lista de resultados.
        List<Factura> resultado = new ArrayList<Factura>();

        // Recorre todas las facturas guardadas.
        for (Factura f : repo.findAll()) {

            // Si se indicó un contrato y no coincide, pasa a la siguiente factura.
            if (idContrato != null &&
                    !f.getContrato().getId().equals(idContrato))
                continue;

            // Si se indicó un período y no coincide, pasa a la siguiente factura.
            if (periodo != null &&
                    !f.getPeriodo().equals(periodo))
                continue;

            // Si se indicó un estado y no coincide con el actual, pasa a la siguiente factura.
            if (estado != null &&
                    f.estadoActual() != estado)
                continue;

            // Agrega la factura que cumple los filtros.
            resultado.add(f);
        }

        return resultado;
    }

    @Override
    public void anular(Long id) throws Excepcion {

        // Busca la factura que se quiere anular.
        Optional<Factura> rta = repo.findById(id);

        // Si no existe, lanza un error.
        if (!rta.isPresent())
            throw new Excepcion( "id","No se encuentra la factura que desea anular.",404);

        Factura f = rta.get();

        // Consulta el estado actual de la factura.
        EstadoFactura actual = f.estadoActual();

        // Impide anular una factura pagada.
        if (actual == EstadoFactura.PAGADA)
            throw new Excepcion(
                    "estado",
                    "Una factura pagada no puede anularse.",
                    409);

        // Impide anular una factura que ya está anulada.
        if (actual == EstadoFactura.ANULADA)
            throw new Excepcion(
                    "estado",
                    "La factura ya se encuentra anulada.",
                    409);

        // Marca la factura como anulada.
        f.setEstado(EstadoFactura.ANULADA);

        // Guarda el cambio en la base de datos.
        repo.save(f);
    }

    // Calcula el vencimiento; si el día indicado no existe en ese mes, usa el último día.
    private LocalDate calcularVencimiento(YearMonth periodo,Integer diaVencimiento) {

        // Elige el menor entre el día indicado y la cantidad de días del mes.
        int dia = Math.min(diaVencimiento, periodo.lengthOfMonth());

        // Crea la fecha con el año, el mes y el día elegido.
        return periodo.atDay(dia);
    }
}
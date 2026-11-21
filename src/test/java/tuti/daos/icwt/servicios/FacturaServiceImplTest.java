package tuti.daos.icwt.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tuti.daos.icwt.accesoDatos.IContratoRepo;
import tuti.daos.icwt.accesoDatos.IFacturaRepo;
import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.entidades.EstadoContrato;
import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.facturas.FacturaDTO;

/** Con este tes se puede verificar reglas S01 sin levantar Spring ni depender de MySQL */
@ExtendWith(MockitoExtension.class)
class FacturaServiceImplTest {

    @Mock private IContratoRepo contratoRepo;
    @Mock private IFacturaRepo facturaRepo;
    @InjectMocks private FacturaServiceImpl service;

    private Contrato contrato;
    private FacturaDTO dto;

    @BeforeEach
    void preparar() {
        contrato = new Contrato();
        contrato.setId(15L);
        contrato.setEstado(EstadoContrato.ACTIVO);
        contrato.setEliminado(false);
        contrato.setFechaInicio(LocalDate.of(2026, 1, 1));
        contrato.setDuracionMeses(12);
        contrato.setDiaVencimiento(10);
        contrato.setImporteMensual(new BigDecimal("350000.00"));

        dto = new FacturaDTO();
        dto.setIdContrato(15L);
        dto.setPeriodo("2026-11");
        dto.setFechaEmision(LocalDate.of(2026, 11, 1));
    }

    @Test
    void rechazaFacturaMensualDuplicada() {
        when(contratoRepo.buscarPorIdParaFacturacion(15L)).thenReturn(Optional.of(contrato));
        when(facturaRepo.findByContratoIdAndPeriodoAndMensualAndEstadoNot(
                15L, "2026-11", true, EstadoFactura.ANULADA)).thenReturn(List.of(new Factura()));

        Excepcion error = assertThrows(Excepcion.class, () -> service.emitir(dto));
        assertEquals(409, error.getStatusCode());
        verify(facturaRepo, never()).save(any(Factura.class));
    }

    @Test
    void mensualUsaImporteDelContratoYCalculaVencimiento() throws Excepcion {
        when(contratoRepo.buscarPorIdParaFacturacion(15L)).thenReturn(Optional.of(contrato));
        when(facturaRepo.findByContratoIdAndPeriodoAndMensualAndEstadoNot(
                15L, "2026-11", true, EstadoFactura.ANULADA)).thenReturn(List.of());
        when(facturaRepo.save(any(Factura.class))).thenAnswer(invocacion -> {
            Factura factura = invocacion.getArgument(0);
            assertEquals(0, factura.getImporte().compareTo(new BigDecimal("350000.00")));
            assertEquals(LocalDate.of(2026, 11, 10), factura.getFechaVencimiento());
            factura.setId(99L);
            return factura;
        });

        assertEquals(99L, service.emitir(dto));
    }
}

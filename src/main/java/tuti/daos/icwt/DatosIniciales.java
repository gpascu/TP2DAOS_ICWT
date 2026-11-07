package tuti.daos.icwt;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import tuti.daos.icwt.accesoDatos.IContratoRepo;
import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.entidades.EstadoContrato;

//Con esta classe cargo unos contratos de ejemplo cuando la base esta vacia, para poder probar los servicios sin tener que cargar todo a mano
@Component
public class DatosIniciales implements CommandLineRunner {

	@Autowired
	private IContratoRepo contratoRepo;

	@Override
	public void run(String... args) throws Exception {
		if (contratoRepo.count() > 0)
			return;

		// contrato activo y vigente, vence los dias 10
		Contrato c1 = new Contrato();
		c1.setPropietario("Maria Gomez");
		c1.setInquilino("Juan Perez");
		c1.setDireccionPropiedad("Bv. Gálvez 1550, Santa Fe");
		c1.setFechaInicio(LocalDate.of(2026, 1, 1));
		c1.setDuracionMeses(12);
		c1.setImporteMensual(new BigDecimal("350000"));
		c1.setDiaVencimiento(10);
		c1.setDescripcion("Depto 2 ambientes");
		c1.setEstado(EstadoContrato.ACTIVO);
		c1.setEliminado(false);
		contratoRepo.save(c1);

		// contrato activo con dia de vencimiento 31, para probar el ultimo dia del mes
		Contrato c2 = new Contrato();
		c2.setPropietario("Carlos Ruiz");
		c2.setInquilino("Lucia Fernandez");
		c2.setDireccionPropiedad("San Martín 2340, Santo Tomé");
		c2.setFechaInicio(LocalDate.of(2026, 6, 15));
		c2.setDuracionMeses(12);
		c2.setImporteMensual(new BigDecimal("420000"));
		c2.setDiaVencimiento(31);
		c2.setDescripcion("Casa 3 ambientes con patio");
		c2.setEstado(EstadoContrato.ACTIVO);
		c2.setEliminado(false);
		contratoRepo.save(c2);

		// contrato finalizado, sirve para probar los rechazos por reglas de negocio
		Contrato c3 = new Contrato();
		c3.setPropietario("Ana Torres");
		c3.setInquilino("Pedro Sanchez");
		c3.setDireccionPropiedad("Urquiza 980, Santa Fe");
		c3.setFechaInicio(LocalDate.of(2024, 3, 1));
		c3.setDuracionMeses(12);
		c3.setImporteMensual(new BigDecimal("280000"));
		c3.setDiaVencimiento(5);
		c3.setDescripcion("Local comercial");
		c3.setEstado(EstadoContrato.FINALIZADO);
		c3.setEliminado(false);
		contratoRepo.save(c3);
	}
}

package tuti.daos.icwt.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.daos.icwt.accesoDatos.IContratoRepo;
import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.entidades.EstadoContrato;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.contratos.ContratoDTO;


@Service
public class ContratoServiceImpl implements ContratoService {

    // asigna el repositorio para acceder a los contratos.
    @Autowired
    private IContratoRepo repo;

    @Override
    public List<Contrato> getAll() {
        // Devuelve todos los contratos guardados.
        return repo.findAll();
    }

    @Override
    public Optional<Contrato> getById(Long id) {
        // Busca por ID; devuelve un Optional vacío si el contrato no existe.
        return repo.findById(id);
    }

    @Override
    public Long insert(ContratoDTO dto) throws Excepcion {
        // Convierte los datos recibidos en un objeto Contrato.
        Contrato c = dto.toPojo();

        // Todo contrato nuevo comienza activo y sin estar eliminado.
        c.setEstado(EstadoContrato.ACTIVO);
        c.setEliminado(false);

        // Guarda el contrato y devuelve su ID.
        Contrato guardado = repo.save(c);
        return guardado.getId();
    }

    @Override
    public void eliminar(Long id) throws Excepcion {
        // Busca el contrato que se quiere eliminar.
        Optional<Contrato> rta = repo.findById(id);

        // Si el contrato no existe, lanza un error.
        if (!rta.isPresent())
            throw new Excepcion("id", "No se encuentra el contrato que desea eliminar.", 404);

        // Lo marca como eliminado, pero lo mantiene en la base de datos.
        Contrato c = rta.get();
        c.setEliminado(true);
        repo.save(c);
    }
}
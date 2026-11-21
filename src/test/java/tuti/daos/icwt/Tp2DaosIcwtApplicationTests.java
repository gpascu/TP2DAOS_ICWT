package tuti.daos.icwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@EnabledIfEnvironmentVariable(named = "HOGARYA_INTEGRATION_TESTS", matches = "true")
class Tp2DaosIcwtApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto completo arranca contra la BD de integracion.
    }
}

package servicios;

import modelo.Destinatario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class EnvioEmailsTest {

    private EnvioEmails envioEmails;

    @BeforeEach
    void setUp() {
        Logger logger = LoggerFactory.getLogger(EnvioEmailsTest.class);
        envioEmails = new EnvioEmails(logger);
    }

    @Test
    void enviarEmail_debeDevolverTrue() {
        Destinatario dest = new Destinatario();

        boolean resultado = envioEmails.enviarEmail(dest, "Mensaje de prueba");

        assertTrue(resultado);
    }
}
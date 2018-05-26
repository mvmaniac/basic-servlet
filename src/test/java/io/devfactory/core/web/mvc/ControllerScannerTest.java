package io.devfactory.core.web.mvc;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ControllerScannerTest {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner cf;

    @Before
    public void setup() {
        cf = new ControllerScanner("io.devfactory.core.web.nmvc");
    }

    @Test
    public void getControllers() throws Exception {

        Map<Class<?>, Object> controllers = cf.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            logger.debug("controller : {}", controller);
        }
    }
}

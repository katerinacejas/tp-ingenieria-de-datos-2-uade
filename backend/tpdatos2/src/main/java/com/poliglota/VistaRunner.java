package com.poliglota;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.poliglota.vista.VistaCompartida;

@Component
@Order(2)  // corre después del DataInitializer
public class VistaRunner implements CommandLineRunner {

    private final VistaCompartida vista;

    public VistaRunner(VistaCompartida vista) {
        this.vista = vista;
    }

    @Override
    public void run(String... args) {
        vista.start();
    }
}
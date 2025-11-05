package com.poliglota;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class BackendPoliglotaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendPoliglotaApplication.class, args);
    }
}



// @Component
// @RequiredArgsConstructor
// public class StartupRunner implements CommandLineRunner {

//     private final ProcessService processService;

//     @Override
//     public void run(String... args) {
//         System.out.println("🚀 Backend Poliglota iniciado correctamente");

//         // Crear un proceso de ejemplo
//         Process process = new Process();
//         process.setName("Producción de fertilizante");
//         process.setDescription("Etapa química del proceso de fertilización");
//         process.setProcessType("Industrial");
//         process.setCost(1200.50);

//         processService.saveProcess(process);

//         // Mostrar todos los procesos
//         System.out.println("\n📋 Procesos cargados en la BD:");
//         processService.getAllProcesses().forEach(System.out::println);
//     }
// }
package ai;

import javafx.application.Application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class AiMlApplication {
    public static void main(String[] args) {
        Application.launch(FxApplication.class, args);
//        SpringApplication.run(AiMlApplication.class, args);
    }
}
package hello.matdil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MatDilApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatDilApplication.class, args);
    }

}

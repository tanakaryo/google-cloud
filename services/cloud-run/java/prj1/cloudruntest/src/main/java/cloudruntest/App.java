package cloudruntest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class App {
    @Value("${NAME:world}")
    String name;

    /**
     * 
     * GreetingController
     */
    @RestController
    public class GreetingController {
        
        
        @GetMapping("/")
        public String greeting() {
            return new StringBuilder().append("Guten ").append(name).toString();
        }
        
    }

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}

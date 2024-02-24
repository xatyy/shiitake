package ro.foodx.backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {
    @GetMapping("/health")
    public ResponseEntity<String> sayHello() {

        return ResponseEntity.ok("Working Hot as Chili Sauce");
    }
}

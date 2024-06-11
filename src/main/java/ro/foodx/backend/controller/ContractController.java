package ro.foodx.backend.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.foodx.backend.service.contract.ContractService;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/contract")
public class ContractController {
    private ContractService contractService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateContract() {
        try {
            Map<String, String> placeholders = Map.of(
                    "[STORE_NAME]", "TEST STORE"
            );

            contractService.generateAndUploadContract(placeholders);

            return ResponseEntity.ok("Contract generated and uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate contract.");
        }
    }
}

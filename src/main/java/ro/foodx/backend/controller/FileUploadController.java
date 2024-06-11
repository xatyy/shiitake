package ro.foodx.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.foodx.backend.dto.dashboard.UploadRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileUploadController {
    private final S3Client s3Client;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@Valid @ModelAttribute UploadRequest uploadRequest) throws IOException {

        MultipartFile file = uploadRequest.getImage();

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();



                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket("foodx-media")
                                .key(uuidAsString + '-' + file.getOriginalFilename())
                                .contentType(file.getContentType()).build(),
                        software.amazon.awssdk.core.sync.RequestBody.fromInputStream((uploadRequest.getImage().getInputStream()), uploadRequest.getImage().getSize())
                );

        String responseURL = "https://foodx-media.s3.eu-north-1.amazonaws.com/" + uuidAsString + '-' + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseURL);
    }
}

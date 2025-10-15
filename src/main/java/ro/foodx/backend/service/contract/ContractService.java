package ro.foodx.backend.service.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class ContractService {

    private final S3Client s3Client;

    @Value("${app.environment:dev}")
    private String environment;

    @Value("${app.local.contract-folder:tmp/contracts}")
    private String localContractFolder;

    public String generateAndUploadContract(Map<String, String> placeholders) {
        File tempDocxFile = null;
        File tempPdfFile = null;

        try (FileInputStream fis = new FileInputStream("src/main/resources/contract-template.docx");
             FileOutputStream fos = new FileOutputStream(tempDocxFile = File.createTempFile("contract-", ".docx"))) {

            XWPFDocument document = new XWPFDocument(fis);

            // Replace placeholders
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null) {
                        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                            text = text.replace(entry.getKey(), entry.getValue());
                        }
                        run.setText(text, 0);
                    }
                }
            }

            document.write(fos);

            // Convert to PDF
            tempPdfFile = File.createTempFile("contract-", ".pdf");
            convertDocxToPdf(tempDocxFile, tempPdfFile);

            // Save or upload
            if ("prod".equalsIgnoreCase(environment)) {
                return uploadToS3(tempPdfFile);
            } else {
                return saveLocally(tempPdfFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error during contract generation: " + e.getMessage();
        } finally {
            if (tempDocxFile != null && !tempDocxFile.delete())
                System.err.println("Failed to delete temp docx file");
        }
    }

    private void convertDocxToPdf(File docxFile, File pdfFile) throws Docx4JException, IOException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docxFile);
        FOSettings foSettings = Docx4J.createFOSettings();
        foSettings.setWmlPackage(wordMLPackage);

        try (OutputStream os = new FileOutputStream(pdfFile)) {
            Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
        }
    }

    private String uploadToS3(File pdfFile) throws IOException {
        try (FileInputStream pdfInputStream = new FileInputStream(pdfFile)) {
            String key = "contracts/" + pdfFile.getName();
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket("foodx-media")
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(pdfInputStream, pdfInputStream.available())
            );
            return s3Client.utilities().getUrl(b -> b.bucket("foodx-media").key(key)).toExternalForm();
        }
    }

    private String saveLocally(File pdfFile) throws IOException {
        Path targetDir = Path.of(localContractFolder);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        Path targetFile = targetDir.resolve(pdfFile.getName());
        Files.copy(pdfFile.toPath(), targetFile);
        System.out.println(" Saved local contract at: " + targetFile.toAbsolutePath());
        return targetFile.toAbsolutePath().toString();
    }
}

package ro.foodx.backend.service.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class ContractService {

    private final S3Client s3Client;

    public String generateAndUploadContract(Map<String, String> placeholders) {
        File tempDocxFile = null;
        File tempPdfFile = null;
        String url = "";

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

            // Save modified document to a temporary file
            document.write(fos);

            // Convert .docx to PDF
            convertDocxToPdf(tempDocxFile, tempPdfFile = File.createTempFile("contract-", ".pdf"));

            // Upload PDF to S3
            url = uploadToS3(tempPdfFile);

        } catch (Exception e) {
            System.err.println("Error during contract generation and upload: " + e.getMessage());
        } finally {
            // Cleanup temporary files
            if (tempDocxFile != null && !tempDocxFile.delete()) {
                System.err.println("Failed to delete temp docx file");
            }
            if (tempPdfFile != null && !tempPdfFile.delete()) {
                System.err.println("Failed to delete temp pdf file");
            }
        }
        return url;
    }

    private void convertDocxToPdf(File docxFile, File pdfFile) throws Docx4JException, IOException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docxFile);

        FOSettings foSettings = Docx4J.createFOSettings();
        foSettings.setWmlPackage(wordMLPackage);

        try (OutputStream os = new FileOutputStream(pdfFile)) {
            Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
        } catch (Exception e) {
            System.err.println("Error during PDF conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String uploadToS3(File pdfFile) throws IOException {
        String fileURL = "";
        try (FileInputStream pdfInputStream = new FileInputStream(pdfFile)) {
            String key = "contracts/" + pdfFile.getName();
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket("foodx-media")
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(pdfInputStream, pdfInputStream.available())
            );
            fileURL = s3Client.utilities().getUrl(builder -> builder.bucket("foodx-media").key(key)).toExternalForm();
        }
        return fileURL;
    }
}

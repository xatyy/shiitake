package ro.foodx.backend.service.email;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateUtil {

    public static String readTemplate(String templateName, Map<String, String> replacements) throws IOException {
        ClassPathResource resource = new ClassPathResource(templateName);
        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // Replace placeholders with actual values
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        return content;
    }
}

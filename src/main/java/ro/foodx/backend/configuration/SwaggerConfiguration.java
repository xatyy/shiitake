package ro.foodx.backend.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;


public class SwaggerConfiguration {


    private String appName;

    private String appDescription;

    private String appVersion;

    private String contactName;


    private String contactMail;

    @Bean
    public OpenAPI openAPI() {

        final Info apiInformation = getApiInformation();
        final Components components = new Components();

        final String schemeName = "bearerAuth";
        components.addSecuritySchemes(schemeName, new SecurityScheme().name(schemeName).type(HTTP).scheme("bearer").bearerFormat("JWT"));

        final OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(apiInformation);
        openAPI.setComponents(components);
        openAPI.addSecurityItem(new SecurityRequirement().addList(schemeName));

        return openAPI;
    }

    private Info getApiInformation() {


        final Contact contact = new Contact();
        contact.setName(contactName);
        contact.setEmail(contactMail);


        final Info info = new Info();
        info.setTitle(appName);
        info.setVersion(appVersion);
        info.setDescription(appDescription);

        return info;
    }


}

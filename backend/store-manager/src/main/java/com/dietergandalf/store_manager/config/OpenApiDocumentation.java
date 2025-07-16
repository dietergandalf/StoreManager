package com.dietergandalf.store_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocumentation {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Store Manager API")
                        .version("1.0.0")
                        .description("Comprehensive API for managing store owners, stands, and operations")
                        .contact(new Contact()
                                .name("Store Manager Team")
                                .email("support@storemanager.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addServersItem(new Server()
                        .url("http://localhost:9080")
                        .description("Development server"))
                .addServersItem(new Server()
                        .url("https://api.storemanager.com")
                        .description("Production server"))
                .addTagsItem(new Tag()
                        .name("Owner Management")
                        .description("Operations related to store owners and their profile management"));
    }

    @Bean
    public OpenApiCustomizer schemaCustomizer() {
        return openApi -> {
            // Define OwnerDto schema
            Schema<?> ownerDtoSchema = new Schema<>()
                    .type("object")
                    .addProperty("personId", new Schema<>().type("integer").format("int64").readOnly(true).description("Unique identifier for the owner"))
                    .addProperty("firstName", new Schema<>().type("string").example("John").description("First name of the owner"))
                    .addProperty("lastName", new Schema<>().type("string").example("Doe").description("Last name of the owner"))
                    .addProperty("email", new Schema<>().type("string").format("email").example("john.doe@example.com").description("Email address (must be unique)"))
                    .addProperty("phoneNumber", new Schema<>().type("string").example("+1234567890").description("Phone number"))
                    .addProperty("dateOfBirth", new Schema<>().type("string").format("date").example("1990-01-15").description("Date of birth"))
                    .addProperty("address", new Schema<>().$ref("#/components/schemas/AddressDto"))
                    .addProperty("standIds", new Schema<>().type("array").items(new Schema<>().type("integer").format("int64")).readOnly(true).description("List of stand IDs owned by this owner"))
                    .addProperty("totalRent", new Schema<>().type("number").format("double").readOnly(true).description("Total rent amount for all stands"))
                    .addProperty("hasAvailableStands", new Schema<>().type("boolean").readOnly(true).description("Whether the owner has any available stands"));

            // Define RegisterRequestDto schema
            Schema<?> registerRequestSchema = new Schema<>()
                    .type("object")
                    .addProperty("firstName", new Schema<>().type("string").example("John").description("First name of the owner"))
                    .addProperty("lastName", new Schema<>().type("string").example("Doe").description("Last name of the owner"))
                    .addProperty("email", new Schema<>().type("string").format("email").example("john.doe@example.com").description("Email address (must be unique)"))
                    .addProperty("password", new Schema<>().type("string").writeOnly(true).description("Password for authentication"))
                    .addProperty("phoneNumber", new Schema<>().type("string").example("+1234567890").description("Phone number"))
                    .addProperty("dateOfBirth", new Schema<>().type("string").format("date").example("1990-01-15").description("Date of birth"))
                    .addProperty("address", new Schema<>().$ref("#/components/schemas/AddressDto"))
                    .addProperty("userType", new Schema<>().type("string").example("owner").description("Type of user (should be 'owner' for owner registration)"));

            // Define UpdateProfileRequestDto schema
            Schema<?> updateProfileRequestSchema = new Schema<>()
                    .type("object")
                    .addProperty("firstName", new Schema<>().type("string").example("John").description("First name of the owner"))
                    .addProperty("lastName", new Schema<>().type("string").example("Doe").description("Last name of the owner"))
                    .addProperty("email", new Schema<>().type("string").format("email").example("john.doe@example.com").description("Email address (must be unique)"))
                    .addProperty("phoneNumber", new Schema<>().type("string").example("+1234567890").description("Phone number"))
                    .addProperty("dateOfBirth", new Schema<>().type("string").format("date").example("1990-01-15").description("Date of birth"))
                    .addProperty("address", new Schema<>().$ref("#/components/schemas/AddressDto"));

            // Define AddressDto schema
            Schema<?> addressDtoSchema = new Schema<>()
                    .type("object")
                    .addProperty("street", new Schema<>().type("string").example("123 Main St").description("Street address"))
                    .addProperty("city", new Schema<>().type("string").example("New York").description("City"))
                    .addProperty("province", new Schema<>().type("string").example("NY").description("Province/State"))
                    .addProperty("postalCode", new Schema<>().type("string").example("10001").description("Postal/ZIP code"))
                    .addProperty("country", new Schema<>().type("string").example("USA").description("Country"));

            // Add schemas to components
            openApi.getComponents()
                    .addSchemas("OwnerDto", ownerDtoSchema)
                    .addSchemas("RegisterRequestDto", registerRequestSchema)
                    .addSchemas("UpdateProfileRequestDto", updateProfileRequestSchema)
                    .addSchemas("AddressDto", addressDtoSchema);
        };
    }
}

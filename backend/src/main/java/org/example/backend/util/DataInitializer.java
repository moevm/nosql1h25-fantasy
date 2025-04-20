package org.example.backend.util;

import lombok.RequiredArgsConstructor;
import org.example.backend.repository.CatalogItemRepository;
import org.example.backend.service.DataImportExportService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.nio.charset.StandardCharsets;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CatalogItemRepository catalogItemRepository;
    private final DataImportExportService dataImportExportService;

    @Override
    public void run(String... args) {
        if (catalogItemRepository.count() == 0) {
            ClassPathResource resource = new ClassPathResource("data/data.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    byte[] bytes = is.readAllBytes();
                    String json = new String(bytes, StandardCharsets.UTF_8);
                    int count = dataImportExportService.importDataFromJson(json);
                    System.out.println("Imported " + count + " items from data.json");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


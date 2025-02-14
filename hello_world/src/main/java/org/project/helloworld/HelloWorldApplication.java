package org.project.helloworld;

import org.project.helloworld.model.CatalogItem;
import org.project.helloworld.repo.CatalogItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class HelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CatalogItemRepository repository) {
        return args -> {
            repository.deleteAll();

            CatalogItem item = new CatalogItem(
                    null,
                    "Властелин колец",
                    "Эпическая фэнтези-сага Дж. Р. Р. Толкиена",
                    "Book",
                    1954,
                    List.of("Фэнтези", "Эпическая"),
                    List.of("Средиземье", "Приключения"),
                    List.of("Классика", "Бестселлер")
            );

            repository.save(item);

            repository.findAll().forEach(catalogItem ->
                    System.out.println(catalogItem.toString())
            );
        };
    }

}

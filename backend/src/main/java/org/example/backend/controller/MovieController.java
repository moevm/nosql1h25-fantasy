package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.MovieFilterRequest;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.CatalogItem;
import org.example.backend.service.CatalogItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final CatalogItemService catalogItemService;
    private final CatalogItemMapper catalogItemMapper;

    @PostMapping("/search")
    public List<CatalogItemDto> searchMovies(@RequestBody MovieFilterRequest filter,
                                             @RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size) {
        List<CatalogItem> movies = catalogItemService.searchMovies(filter, page, size);
        return catalogItemMapper.toDtoList(movies);
    }
}

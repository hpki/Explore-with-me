package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.EwmMainService.ID_PATH_VARIABLE_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService compilationsService;

    // Получение сптска всех подборок
    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size
    ) {
        log.info("Getting request GET for path /compilations");
        return compilationsService.getAll(pinned, from, size);
    }

    // Получение подборки
    @GetMapping("/{id}")
    public CompilationDto getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request GET for path /compilations with id {}", id);
        return compilationsService.getDtoCompilationById(id);
    }

}

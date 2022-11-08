package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        log.info("Getting list of all compilation");
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilationList = compilationRepository.findAll(pageable).getContent();
        return compilationList.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getDtoCompilationById(Long id) {
        log.info("Getting dto compilation with id {}", id);
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("compilation with id %s is not found", id)));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public Compilation getCompilationById(Long id) {
        log.info("Getting compilation with id {}", id);
        return compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("compilation with id %s is not found", id)));
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(Compilation compilation) {
        log.info("Adding compilation: {}", compilation);
        Compilation newCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compilationId, Long eventId, Event event) {
        log.info("Adding event with id {} to compilation with id {}", eventId, compilationId);
        Compilation compilation = getCompilationById(compilationId);
        compilation.getEvents().add(event);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        log.info("Deleting compilation with id {}", id);
        Compilation compilation = getCompilationById(id);
        compilationRepository.delete(compilation);
        log.info("Compilation with id {} is deleted", id);
    }

    @Override
    @Transactional
    public CompilationDto pinCompilation(Long id) {
        log.info("Pinning compilation with id {} on page", id);
        Compilation compilation = getCompilationById(id);
        compilation.setPinned(true);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto unpin(Long id) {
        log.info("Unpinning compilation with id {} on page", id);
        Compilation compilation = getCompilationById(id);
        compilation.setPinned(false);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteEvent(Long compilationId, Long eventId, Event event) {
        log.info("Deleting event with id {} from compilation with id {}", eventId, compilationId);
        Compilation compilation = getCompilationById(compilationId);
        compilation.getEvents().remove(event);
        log.info("Event with id {} from compilation with id {} is deleted", eventId, compilationId);
    }
}

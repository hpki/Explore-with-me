package ru.practicum.explorewithme.compilation.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

   public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> eventList) {
       return Compilation.builder()
               .title( newCompilationDto.getTitle())
               .pinned(newCompilationDto.isPinned())
               .events(eventList)
               .build();
   }

   public static CompilationDto toCompilationDto(Compilation compilation) {
       return CompilationDto.builder().id(compilation.getId())
               .title(compilation.getTitle())
               .pinned(compilation.isPinned())
               .events(compilation.getEvents())
               .build();
   }
}

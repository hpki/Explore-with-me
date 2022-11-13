package ru.practicum.explorewithme.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.dto.RequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

   public static RequestDto toRequestDto(Request request) {
       return RequestDto.builder()
               .id(request.getId())
               .requester(request.getRequester().getId())
               .event(request.getEvent().getId())
               .created(request.getCreated())
               .status(request.getStatus())
               .build();
   }
}

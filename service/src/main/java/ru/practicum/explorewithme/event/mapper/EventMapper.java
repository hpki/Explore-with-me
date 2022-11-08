package ru.practicum.explorewithme.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

   public static Event toEvent(NewEventDto eventDto, Category category, User initiator) {
       return Event.builder()
               .title(eventDto.getTitle())
               .annotation( eventDto.getAnnotation())
               .description(eventDto.getDescription())
               .category(category)
               .state(State.PENDING)
               .createdOn(LocalDateTime.now())
               .eventDate(eventDto.getEventDate())
               .initiator(initiator)
               .location(eventDto.getLocation())
               .participantLimit( eventDto.getParticipantLimit())
               .paid(eventDto.isPaid())
               .requestModeration(eventDto.getRequestModeration())
               .build();
   }


   public static EventShortDto toEventShortDto(Event event, int confirmedRequests, int views) {
       return EventShortDto.builder()
               .id( event.getId())
               .title(event.getTitle())
               .annotation(event.getAnnotation())
               .category(event.getCategory())
               .eventDate(event.getEventDate())
               .initiator(event.getInitiator())
               .paid(event.isPaid())
               .confirmedRequests(confirmedRequests)
               .views(views)
               .build();
   }

    public static EventFullDto toEventFullDto(Event event, int confirmedRequests, int views) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.getEventDate(),
                event.getInitiator(),
                event.isPaid(),
                confirmedRequests,
                views,
                event.getDescription(),
                event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getLocation(),
                event.getParticipantLimit(),
                event.isRequestModeration()
        );
    }

}

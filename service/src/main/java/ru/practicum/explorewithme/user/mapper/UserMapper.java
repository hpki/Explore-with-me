package ru.practicum.explorewithme.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.dto.NewUserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

  public static UserDto toUserDto(User user) {
      return UserDto.builder()
              .id(user.getId())
              .name( user.getName())
              .email( user.getEmail())
              .build();
  }

    public static User toUser(NewUserDto newUserDto) {
        return User.builder()
                .name(newUserDto.getName())
                .email(newUserDto.getEmail())
                .build();
    }
}

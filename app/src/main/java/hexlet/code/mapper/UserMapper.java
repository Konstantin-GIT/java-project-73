package hexlet.code.mapper;

import hexlet.code.DTO.UserCreateDTO;
import hexlet.code.DTO.UserDTO;
import hexlet.code.DTO.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;

    @Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    public abstract class UserMapper {
        public abstract User map(UserCreateDTO dto);
        public abstract UserDTO map(User model);
        public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
    }


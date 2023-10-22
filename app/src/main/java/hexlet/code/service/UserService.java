package hexlet.code.service;

import hexlet.code.DTO.UserCreateDTO;
import hexlet.code.DTO.UserDTO;
import hexlet.code.DTO.UserUpdateDTO;
import hexlet.code.model.User;

public interface UserService {

    UserDTO create(UserCreateDTO userData);

    UserDTO update(long id, UserUpdateDTO userData);

    String getCurrentUserName();

    User getCurrentUser();
}

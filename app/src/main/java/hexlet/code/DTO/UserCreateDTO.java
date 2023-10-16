package hexlet.code.DTO;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserCreateDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

}

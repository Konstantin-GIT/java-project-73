package hexlet.code.DTO;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserUpdateDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;

}

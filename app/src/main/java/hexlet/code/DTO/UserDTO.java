package hexlet.code.DTO;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserDTO {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private Date createdAt;
}

package iskander.tabaev.CoffeMachine.dto.personDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonDtoForPut {
    private String username;
    private String password;

    private int id;

    public PersonDtoForPut(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

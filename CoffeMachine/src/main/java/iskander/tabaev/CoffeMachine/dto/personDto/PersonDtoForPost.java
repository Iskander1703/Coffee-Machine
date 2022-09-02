package iskander.tabaev.CoffeMachine.dto.personDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonDtoForPost {
    private String username;
    private String password;

    public PersonDtoForPost(String username) {
        this.username = username;
    }
}

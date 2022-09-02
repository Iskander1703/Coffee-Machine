package iskander.tabaev.CoffeMachine.dto.personDto;

import iskander.tabaev.CoffeMachine.models.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonDtoForGet {

    private String username;

    private Date addedDate;

    private List<Role> roles;


}

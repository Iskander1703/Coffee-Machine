package iskander.tabaev.CoffeMachine.dto.coffeeOrderDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CoffeeOrderDtoForPost {

    private String drinkName;

    private Date date;


}
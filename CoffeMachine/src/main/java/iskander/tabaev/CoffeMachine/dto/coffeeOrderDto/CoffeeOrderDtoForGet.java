package iskander.tabaev.CoffeMachine.dto.coffeeOrderDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CoffeeOrderDtoForGet {

    private String drinkName;

    private Date date;

    private String personName;

    public CoffeeOrderDtoForGet(String drinkName) {
        this.drinkName = drinkName;
    }

}

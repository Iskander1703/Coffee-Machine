package iskander.tabaev.CoffeMachine.dto.coffeeOrderDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CoffeeOrderDtoForPut {

    private int id;
    private String drinkName;



    private Date date;


    public CoffeeOrderDtoForPut(String drinkName, Date date) {
        this.drinkName = drinkName;
        this.date = date;
    }
}
package iskander.tabaev.CoffeMachine.util.exceptions;

import java.time.DateTimeException;

public class IncorrectCoffeeOrderException extends DateTimeException {
    public IncorrectCoffeeOrderException(String message) {
        super(message);
    }
}

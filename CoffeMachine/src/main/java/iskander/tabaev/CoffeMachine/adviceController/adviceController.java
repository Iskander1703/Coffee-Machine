package iskander.tabaev.CoffeMachine.adviceController;

import iskander.tabaev.CoffeMachine.util.ErrorResponse;
import iskander.tabaev.CoffeMachine.util.exceptions.IncorrectCoffeeOrderException;
import iskander.tabaev.CoffeMachine.util.exceptions.NoDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
//Класс для перехвата и обработки исключений
@ControllerAdvice
public class adviceController {

    //Метод обработки исключения, когда данные не найдены
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> noDataExceptionHandler(NoDataException noCoffeeOrderException){
       ErrorResponse errorResponse=new ErrorResponse(noCoffeeOrderException.getMessage(),
               new Date(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value());
       return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //Метод обаботки исключения, когда введена некорректная дата
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> incorrectCoffeeOrderDateExceptionHandler(IncorrectCoffeeOrderException incorrectCoffeeOrderDateException){
        ErrorResponse errorResponse=new ErrorResponse(incorrectCoffeeOrderDateException.getMessage(),
        new Date(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

package iskander.tabaev.CoffeMachine.controllers;

import io.swagger.annotations.ApiOperation;
import iskander.tabaev.CoffeMachine.dto.coffeeOrderDto.CoffeeOrderDtoForGet;
import iskander.tabaev.CoffeMachine.dto.coffeeOrderDto.CoffeeOrderDtoForPost;
import iskander.tabaev.CoffeMachine.dto.coffeeOrderDto.CoffeeOrderDtoForPut;
import iskander.tabaev.CoffeMachine.models.CoffeeOrder;
import iskander.tabaev.CoffeMachine.services.CoffeeService;
import iskander.tabaev.CoffeMachine.util.OkeyResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CoffeMachineController {

    private final CoffeeService coffeeService;
    private final ModelMapper modelMapper;


    @Autowired
    public CoffeMachineController(CoffeeService coffeeService, ModelMapper modelMapper) {
        this.coffeeService = coffeeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("coffeeOrders")
    @ApiOperation(value = "Find all coffee orders",
            notes = "Find all coffee orders of all time. " +
                    "You can send a request if you are logged in as Admin or User")
    public List<CoffeeOrderDtoForGet> getAllCoffeeOrders() {
        return coffeeService.findAll().stream().map((arg) -> convertToCoffeOrderDtoForGet(arg)).collect(Collectors.toList());
    }


    @GetMapping("/coffeeOrders/{id}")
    @ApiOperation(value = "Find coffee order by Id",
            notes = "Find coffee order by Id, if there is an incorrect id, then there will be a status of 400." +
                    "You can send a request if you are logged in as Admin or User")
    public CoffeeOrderDtoForGet getCoffeeOrderbyId(@PathVariable(name = "id") Integer id) {
        return convertToCoffeOrderDtoForGet(coffeeService.findCoffeeById(id));
    }

    @PostMapping("/coffeeOrders")
    @ApiOperation(value = "Adding a new coffee order",
            notes = "Adding a new coffee order. You can order " +
                    "coffee the day before. If you do not specify " +
                    "a time, the order will be for the current time." +
                    " It is also necessary to introduce a drink. " +
                    "You can send a request if you are logged in as Admin or User.")
    private ResponseEntity<OkeyResponse> saveCoffeeOrder(@RequestBody CoffeeOrderDtoForPost coffeeOrderDtoForPost) {

        coffeeService.saveCoffeOrder(convertToCoffeOrderFromPost(coffeeOrderDtoForPost));
        OkeyResponse okeyResponse = new OkeyResponse(new Date(System.currentTimeMillis()),
                new String("Add a new coffee order was successful"),
                HttpStatus.CREATED.value());
        return new ResponseEntity<>(okeyResponse, HttpStatus.CREATED);

    }

    @PutMapping("/coffeeOrders")
    @ApiOperation(value = "Changing coffee order",
            notes = "You can only change an unfulfilled order. " +
                    "You can change the date of the order and the selected drink. " +
                    "You can send a request if you are logged in as Admin or User")
    private ResponseEntity<OkeyResponse> updateCoffeeOrder(@RequestBody CoffeeOrderDtoForPut coffeeOrderDtoForPut) {
        coffeeService.updateCoffeeOrder(convertToCoffeOrderFromPut(coffeeOrderDtoForPut));

        OkeyResponse okeyResponse = new OkeyResponse(new Date(System.currentTimeMillis()),
                new String("Change coffee order with id = " + coffeeOrderDtoForPut.getId() + " was successful"),
                HttpStatus.OK.value());
        return new ResponseEntity<>(okeyResponse, HttpStatus.OK);

    }

    @DeleteMapping("coffeeOrders/{id}")
    @PutMapping("/coffeeOrders")
    @ApiOperation(value = "Deleting a coffee order by ID",
            notes = "Deleting a coffee order by ID." +
                    "You can send a request if you are logged in as Admin or User")
    private ResponseEntity<OkeyResponse> deleteCoffeeOrderById(@PathVariable Integer id) {
        coffeeService.deleteCoffeeOrder(id);
        OkeyResponse okeyResponse = new OkeyResponse(new Date(System.currentTimeMillis()),
                new String("Delete coffee order with id = " + id + " was successful"),
                HttpStatus.OK.value());
        return new ResponseEntity<>(okeyResponse, HttpStatus.OK);
    }

    private CoffeeOrderDtoForGet convertToCoffeOrderDtoForGet(CoffeeOrder coffeOrder) {
        CoffeeOrderDtoForGet coffeeOrderDto = modelMapper.map(coffeOrder, CoffeeOrderDtoForGet.class);
        coffeeOrderDto.setPersonName(coffeOrder.getPerson().getUsername());
        return coffeeOrderDto;
    }

    private CoffeeOrder convertToCoffeOrderFromPost(CoffeeOrderDtoForPost coffeeOrderDtoForPost) {
        return modelMapper.map(coffeeOrderDtoForPost, CoffeeOrder.class);
    }


    private CoffeeOrder convertToCoffeOrderFromPut(CoffeeOrderDtoForPut coffeeOrderDtoForPut) {
        return modelMapper.map(coffeeOrderDtoForPut, CoffeeOrder.class);
    }

}

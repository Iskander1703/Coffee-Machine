package iskander.tabaev.CoffeMachine.controllers;

import io.swagger.annotations.ApiOperation;
import iskander.tabaev.CoffeMachine.dto.personDto.PersonDtoForGet;
import iskander.tabaev.CoffeMachine.dto.personDto.PersonDtoForPost;
import iskander.tabaev.CoffeMachine.dto.personDto.PersonDtoForPut;
import iskander.tabaev.CoffeMachine.models.Person;
import iskander.tabaev.CoffeMachine.services.PersonService;
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
public class PersonController {

    private final PersonService personService;

    private final ModelMapper modelMapper;

    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/persons")
    @ApiOperation(value = "Find all person (users)",
            notes = "Find all person (users) of all time. " +
                    "You can send a request if you are logged in as Admin")
    public List<PersonDtoForGet> getAllPersons(){
        return personService.findAll().stream().map((a)->convertToPersonDtoGet(a)).collect(Collectors.toList());
    }

    @ApiOperation(value = "Find user bu id",
            notes = "To find a user by id, you must enter the id in the parameters " +
                    "You can send a request if you are logged in as Admin")
    @GetMapping("/persons/{id}")
    public PersonDtoForGet getPersonById(@PathVariable Integer id){
        return convertToPersonDtoGet(personService.findPersonById(id));
    }


    @ApiOperation(value = "Adding a new user",
            notes = "To add a new user, you need to give him a unique username" +
                    " and password, passing both values in the body of the method " +
                    "You can send a request if you are logged in as Admin")
    @PostMapping("/persons")
    public ResponseEntity<OkeyResponse> savePerson(@RequestBody PersonDtoForPost personDto){
     personService.registerPerson(convertToPersonFromPost(personDto));
        OkeyResponse okeyResponse=new OkeyResponse(new Date(System.currentTimeMillis()),
                new String("Add a new Person was successful"),
                HttpStatus.CREATED.value());
        return new ResponseEntity<>(okeyResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Deleting a user by id",
            notes = "Deleting a user by id " +
                    "You can send a request if you are logged in as Admin")
    @DeleteMapping("/persons/{id}")
    private ResponseEntity<OkeyResponse> deletePersonById(@PathVariable Integer id) {
        personService.deletePerson(id);
        OkeyResponse okeyResponse = new OkeyResponse(new Date(System.currentTimeMillis()),
                new String("Delete Person with id = " + id + " was successful"),
                HttpStatus.OK.value());
        return new ResponseEntity<>(okeyResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "User change",
            notes = "changing a user by his id. You can change the username of" +
                    " the user, as well as his password. The password will be" +
                    " stored in the database in encrypted form. " +
                    "You can send a request if you are logged in as Admin")
    @PutMapping("/persons")
    private ResponseEntity<OkeyResponse> updatePerson(@RequestBody PersonDtoForPut personDtoForPut) {
        personService.updatePersonOrder(convertToPersonFromPut(personDtoForPut));

        OkeyResponse okeyResponse = new OkeyResponse(new Date(System.currentTimeMillis()),
                new String("Change person with id = " + personDtoForPut.getId() + " was successful"),
                HttpStatus.OK.value());
        return new ResponseEntity<>(okeyResponse, HttpStatus.OK);

    }

    private PersonDtoForGet convertToPersonDtoGet(Person person){
        return modelMapper.map(person, PersonDtoForGet.class);
    }

    private Person convertToPersonFromPost(PersonDtoForPost personDtoForPost){
        return modelMapper.map(personDtoForPost, Person.class);
    }

    private Person convertToPersonFromPut(PersonDtoForPut personDtoForPut){
        return modelMapper.map(personDtoForPut, Person.class);
    }

}

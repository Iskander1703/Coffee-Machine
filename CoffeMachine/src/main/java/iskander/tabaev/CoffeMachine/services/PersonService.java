package iskander.tabaev.CoffeMachine.services;

import iskander.tabaev.CoffeMachine.models.Person;
import iskander.tabaev.CoffeMachine.models.Role;
import iskander.tabaev.CoffeMachine.repositories.PersonRepository;
import iskander.tabaev.CoffeMachine.repositories.RoleRepository;
import iskander.tabaev.CoffeMachine.util.exceptions.NoDataException;
import iskander.tabaev.CoffeMachine.util.exceptions.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findPersonById(Integer id ){
        Optional<Person> personOptional=personRepository.findById(id);
        if (!personOptional.isPresent()){
            throw new NoDataException("Person with id = "+id +" does not exist");
        }
        return personOptional.get();
    }

    public void registerPerson(Person person){

        //Проверка на уникальность юзернейма пользователя, если юзернейм не уникален
        //То будет выброшено исключение
        Optional<Person> personOptional=personRepository.findByUsername(person.getUsername());
        if (personOptional.isPresent()){
            throw new UniqueException("Person with username = "+person.getUsername() +" already exist");
        }

        //Шифрование пароля, используя Bcrypt шифрование
       String encodedPassword= passwordEncoder.encode(person.getPassword());
       person.setPassword(encodedPassword);
       person.setAddedDate(new Date(System.currentTimeMillis()));

        //По умолчанию созданному пользователю дается роль User
        Role role=roleRepository.findByRole("ROLE_USER").get();
        person.setRoles(List.of(role));

        //Сохранение в БД, используя Репозиторий
        personRepository.save(person);
    }

    public void deletePerson(Integer id){

        //Поиск пользователя по id. Если пользователь с id не найден, то будет выброшено исключение
        Optional<Person> personOptional=personRepository.findById(id);
        if (!personOptional.isPresent()){
            throw new NoDataException("Person order with id = "+id +" does not exist");
        }

        //Удаление пользователя по id, используя возможность репозитория
        else{
            personRepository.deleteById(id);
        }
    }

    public void updatePersonOrder(Person person){
        //Проверка на уникальность юзернейма пользователя, если новый юзернейм не уникален
        //То будет выброшено исключениеx
        //Проверка на существование пользователя с таким же юзернеймом
        Optional<Person> personOptional=personRepository.findByUsername(person.getUsername());
        if (personOptional.isPresent()){
            //Если id изменяемого пользователя и id совпавшего пользователя разные, то
            //будет выброшено исключение, так как username уже занят
            if (personOptional.get().getId()!=person.getId())
            throw new UniqueException("Person with username = "+person.getUsername() +" already exist");
        }

        //Поиск пользователя по id. Если пользователь с id не найден, то будет выброшено исключение
        Optional<Person> personOptional1=personRepository.findById(person.getId());
        if (!personOptional1.isPresent()){
            throw new NoDataException("Person with id = "+person.getId() +" does not exist");
        }

        //Устанавливаем новое значение пароля, если оно не изменилось, то
        //пароль просто будет перекодирован с добавлением соли
        String encodedPassword= passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);

        //Дата добавления остается прежний
        person.setAddedDate(personOptional1.get().getAddedDate());
        //Роли у пользователя должны остаться такими же, какие были до этого
        person.setRoles(personOptional1.get().getRoles());
        personRepository.save(person);
    }
}

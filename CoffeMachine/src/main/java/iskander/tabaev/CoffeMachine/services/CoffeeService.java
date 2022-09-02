package iskander.tabaev.CoffeMachine.services;

import iskander.tabaev.CoffeMachine.models.CoffeeOrder;
import iskander.tabaev.CoffeMachine.models.Person;
import iskander.tabaev.CoffeMachine.repositories.CoffeeOrderRepository;
import iskander.tabaev.CoffeMachine.repositories.PersonRepository;
import iskander.tabaev.CoffeMachine.util.exceptions.IncorrectCoffeeOrderException;
import iskander.tabaev.CoffeMachine.util.exceptions.NoDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CoffeeService {

    private final CoffeeOrderRepository coffeeOrderRepository;

    private final PersonRepository personRepository;


    @Autowired
    public CoffeeService(CoffeeOrderRepository coffeeOrderRepository, PersonRepository personRepository) {
        this.coffeeOrderRepository = coffeeOrderRepository;
        this.personRepository = personRepository;
    }


    //Метод обращается к репозиторию, что бы получить список всех заказов кофе
    public List<CoffeeOrder> findAll() {
        return coffeeOrderRepository.findAll();
    }

    //Метод обращается к репозиторию, что бы получить информацию о заказе кофе по его ид,
    //Репозиторий возвращает объект класса Optional, если
    // Optional.get==0, то будет выброшено исключение NoCofeeOrderWhithIdException
    public CoffeeOrder findCoffeeById(Integer id) {
        Optional<CoffeeOrder> coffeeOrderOptional = coffeeOrderRepository.findById(id);
        if (!coffeeOrderOptional.isPresent()) {
            throw new NoDataException("Coffee order with id = " + id + " does not exist");
        } else {
            return coffeeOrderOptional.get();
        }
    }

    //Метод сохранения заказа кофе в базу данных
    public CoffeeOrder saveCoffeOrder(CoffeeOrder coffeeOrder) {
        //Проверка, что введенная дата для заказа введена верно
        if (coffeeOrder.getDate() != null) {
            //Проверка введенной даты на корректность, если введена некорректная дата,
            //то будет выкинут Exception
            if (checkDate(coffeeOrder.getDate()) == false) {
                throw new IncorrectCoffeeOrderException("Incorrect date");
            }
        }
        //Если дата не указана, то заказу присваивается текущая дата
        else {
            coffeeOrder.setDate(new Date(System.currentTimeMillis()));
        }
        //Проверка, что введенный введенный кофе можно приготовить.
        if (!checkDrink(coffeeOrder.getDrinkName())){
            throw new IncorrectCoffeeOrderException("Drink not found. Available drinks: Cappuccino, Coffee with milk," +
                    " Espresso, Latte, Americano");
        }
        //Находим пользователя, который заказал кофе по логину и сохраняем заказ в базу данных
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personRepository.findByUsername(username).get();
        coffeeOrder.setPerson(person);
        coffeeOrderRepository.save(coffeeOrder);
        return coffeeOrder;
    }

    public void updateCoffeeOrder(CoffeeOrder coffeeOrder) {

        //Проверка, что введенный id, изменяемого кофе дейтвительно существует
        Optional<CoffeeOrder> coffeeOrderOptional = coffeeOrderRepository.findById(coffeeOrder.getId());
        if (!coffeeOrderOptional.isPresent()) {
            throw new NoDataException("Coffee order with id = " + coffeeOrder.getId() + " does not exist");
        }

        //Проверка на дату. Менять можно только заказы, которые еще не начались
        Date dateForCompare = new Date(System.currentTimeMillis() - 1000);
        if (coffeeOrder.getDate().before(dateForCompare)) {
            throw new IncorrectCoffeeOrderException("Coffee orders that are already detected or delayed cannot be changed."+ "Only scheduled coffee orders can be changed.");
        }

        //Проверка, что введенный введенный кофе можно приготовить.
        if (!checkDrink(coffeeOrder.getDrinkName())){
            throw new IncorrectCoffeeOrderException("Drink not found. Available drinks: Cappuccino, Coffee with milk," +
                    " Espresso, Latte, Americano");
        }

        //Устанавливаем значение того, кто сделал заказ. Так же можно сделать проверку на то,
        //кто изменяет заказ. Если человек не заказывал заказ, то и изменять он его не может
        coffeeOrder.setPerson(coffeeOrderOptional.get().getPerson());
        coffeeOrderRepository.save(coffeeOrder);
    }

    public void deleteCoffeeOrder(Integer id) {
        //Если заказ с кофе не найден, то выбросится исключение
        Optional<CoffeeOrder> coffeeOrderOptional = coffeeOrderRepository.findById(id);
        if (!coffeeOrderOptional.isPresent()) {
            throw new NoDataException("Coffee order with id = " + id + " does not exist");
        } else {
            coffeeOrderRepository.deleteById(id);
        }
    }

    //Метод проверки даты на Правильность
    private Boolean checkDate(Date date) {
        //Получаем дату, которая на 10 сек. меньше, чем время заказа
        Date dateForCompare1 = new Date(new Date().getTime() - 10000);
        //Получаем дату, которая на 1 сутки больше, чем время заказа
        Date dateForCompare2 = new Date(new Date().getTime() + 86_400_000);

        //Идет проверка правильность даты. Заказать кофе в прошедшем времени нельзя,
        //Если вы попытаетесь это сделать, то метод вернет false
        //Идет проверка правильность даты. Заказать кофе более чем за 24 часа нельзя,
        //Если вы попытаетесь это сделать, то метод вернет false
        if (date.after(dateForCompare1) || date.before(dateForCompare2)) {
            return true;
        }

        //Если дата корректна, то вернется true
        return false;
    }

    //Метод на проверку напитка. В данный момент кофемашина умеет готовить только эти напитика
    private Boolean checkDrink(String drink){
        if (drink.equals("Cappuccino") || drink.equals("Coffee with milk")
        || drink.equals("Espresso") || drink.equals("Latte") || drink.equals("Americano")){
            return true;
        } else {
            return false;
        }


    }

}

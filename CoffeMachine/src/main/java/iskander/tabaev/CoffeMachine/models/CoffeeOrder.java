package iskander.tabaev.CoffeMachine.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "coffee_orders")
@Getter
@Setter
@NoArgsConstructor
public class CoffeeOrder {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "coffee_order_id")
    private int id;

    @Column(name = "drink_name")
    private String drinkName;

    @Column(name = "date")
    private Date date;


    @ManyToOne
    @JoinColumn(name="person_id", referencedColumnName = "person_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Person person;
}

package iskander.tabaev.CoffeMachine.security;

import iskander.tabaev.CoffeMachine.models.Person;
import iskander.tabaev.CoffeMachine.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersonDetails implements UserDetails {

    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //Возвращаем список ролей пользователя, на которые он авторизован
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (Role role:person.getRoles()){
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRole()));
        }
        System.out.println(grantedAuthorityList);
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //Метод нужен, что бы получать данные аутентифицированного пользователя
    public Person getPerson() {
        return person;
    }
}

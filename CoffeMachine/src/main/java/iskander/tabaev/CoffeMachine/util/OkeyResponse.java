package iskander.tabaev.CoffeMachine.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  OkeyResponse{
    private Date date;
    private String message;
    private int Status;

}

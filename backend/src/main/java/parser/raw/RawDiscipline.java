package parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Value
@AllArgsConstructor
public class RawDiscipline {
    private String name;
    private int practiceHours;
}

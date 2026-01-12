package parser.raw;

import entity.types.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@AllArgsConstructor
@Value
public class RawLabWork {
    private String name;
    private RawCoordinates coordinates;
    private String description;
    private Difficulty difficulty;
    private RawDiscipline discipline;
    private Double minimalPoint;
    private Float averagePoint;
    private RawPerson author;
}

package parser.raw;

import entity.types.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

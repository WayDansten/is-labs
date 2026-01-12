package parser.raw;

import java.time.LocalDateTime;

import entity.types.Color;
import entity.types.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Value
@AllArgsConstructor
public class RawPerson {
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private RawLocation location;
    private LocalDateTime birthday;
    private Country nationality;
}

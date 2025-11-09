package app.dto.person;

import java.io.Serializable;
import java.time.LocalDateTime;

import app.dto.location.LocationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.types.Color;
import app.types.Country;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDTO implements Serializable {
    private int id;

    private String name;

    private Color eyeColor;

    private Color hairColor;

    private LocationResponseDTO location;

    private LocalDateTime birthday;

    private Country nationality;

}

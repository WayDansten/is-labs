package app.dto.person;

import java.io.Serializable;
import java.time.LocalDateTime;

import app.dto.location.LocationRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PersonRequestDTO implements Serializable {
    private Integer id;

    @NotNull
    @NotEmpty
    private String name;

    private Color eyeColor;

    @NotNull
    private Color hairColor;

    private LocationRequestDTO location;

    @NotNull
    private LocalDateTime birthday;

    @NotNull
    private Country nationality;
}

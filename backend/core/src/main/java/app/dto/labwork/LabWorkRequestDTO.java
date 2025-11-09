package app.dto.labwork;

import java.io.Serializable;
import java.time.LocalDateTime;

import app.dto.coordinates.CoordinatesRequestDTO;
import app.dto.discipline.DisciplineRequestDTO;
import app.dto.person.PersonRequestDTO;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.entity.types.Difficulty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabWorkRequestDTO implements Serializable {
    private Integer id;
    
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private CoordinatesRequestDTO coordinates;

    private LocalDateTime creationDate;

    private String description;

    private Difficulty difficulty;

    private DisciplineRequestDTO discipline;

    @DecimalMin(value = "0", inclusive = false)
    private Double minimalPoint;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private Float averagePoint;
    
    private PersonRequestDTO author;
}

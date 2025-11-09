package app.dto.labwork;

import java.io.Serializable;
import java.time.LocalDateTime;

import app.dto.coordinates.CoordinatesResponseDTO;
import app.dto.discipline.DisciplineResponseDTO;
import app.dto.person.PersonResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.entity.types.Difficulty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabWorkResponseDTO implements Serializable {
    private int id;

    private String name;

    private CoordinatesResponseDTO coordinates;

    private LocalDateTime creationDate;

    private String description;

    private Difficulty difficulty;

    private DisciplineResponseDTO discipline;

    private Double minimalPoint;

    private float averagePoint;

    private PersonResponseDTO author;

}

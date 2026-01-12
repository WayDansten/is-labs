package parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Getter
@Setter
@Value
@AllArgsConstructor
public class RawCoordinates implements Serializable {
    private float x;
    private double y;
}
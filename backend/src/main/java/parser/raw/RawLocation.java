package parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Value
@AllArgsConstructor
public class RawLocation {
    private String name;
    private Long x;
    private Double y;
    private Float z;
}

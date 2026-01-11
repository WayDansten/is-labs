package parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawLocation {
    private String name;
    private Long x;
    private Double y;
    private Float z;
}

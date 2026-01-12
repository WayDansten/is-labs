package dto.upload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponseDTO implements Serializable{
    private int id;

    private boolean status;

    private int objectsAdded;
}


package dto.upload;

import java.io.InputStream;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.ImportFileFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestDTO implements Serializable {
    private InputStream fileStream;
    
    private String fileName;
    
    private ImportFileFormat format;
}

package parser;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import dto.labwork.LabWorkRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import mapper.LabWorkMapper;

@ApplicationScoped
public class JsonLabWorkParser {
    @Inject
    LabWorkMapper mapper;

    public List<LabWorkRequestDTO> parse(InputStream inputStream) {
        Jsonb jsonb = JsonbBuilder.create();
        LabWorkRequestDTO[] arr = jsonb.fromJson(inputStream,LabWorkRequestDTO[].class);
        return Arrays.asList(arr);
    }
}

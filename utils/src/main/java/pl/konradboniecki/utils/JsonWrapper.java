package pl.konradboniecki.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
  * Class provides simplicity of creating jsons.
 **/

@Component
@Scope(SCOPE_SINGLETON)
public class JsonWrapper {

    private static ObjectWriter objectWriter;

    public JsonWrapper(){
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public String objectMapToJsonString(Map<String, Object> objects) throws JsonProcessingException {
        return objectWriter.writeValueAsString(objects);
    }
}

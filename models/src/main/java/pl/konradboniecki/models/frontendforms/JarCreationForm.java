package pl.konradboniecki.models.frontendforms;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class JarCreationForm {

    @NotEmpty(message = "{jarCreationForm.capacityRequired}")
    @Size(min=1, message = "{jarCreationForm.capacitySize}")
    private Long capacity;

    @NotEmpty(message = "{jarCreationForm.jarNameRequired}")
    @Pattern(regexp = "[0-9a-zA-Z]{1,30}", message = "{jarCreationForm.jarNameRegex}")
    private String jarName;
}

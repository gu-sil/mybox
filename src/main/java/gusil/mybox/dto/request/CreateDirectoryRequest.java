package gusil.mybox.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateDirectoryRequest {
    @NotEmpty
    @Length(max = 20)
    private String directoryName;

    @NotEmpty
    private String directoryOwner;

    private String directoryParent;
}

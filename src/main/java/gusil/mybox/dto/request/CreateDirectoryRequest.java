package gusil.mybox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDirectoryRequest {
    @NotEmpty
    @Length(max = 20)
    private String directoryName;

    @NotEmpty
    private String directoryOwner;

    private String directoryParent;
}

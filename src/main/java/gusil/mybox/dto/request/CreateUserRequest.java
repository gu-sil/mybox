package gusil.mybox.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {
    @NotBlank
    @Length(max = 20)
    private String userName;

    @NotBlank
    private String userPassword;
}

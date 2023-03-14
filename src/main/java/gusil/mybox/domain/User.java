package gusil.mybox.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "users")
public class User extends BaseEntity{
    @Id
    private String userId = UUID.randomUUID().toString();
    private String userName;
    private String userPassword;
    private Long maxUsage;
    private Long currentUsage;

    @Builder
    public User(String userName, String userPassword, Long maxUsage, Long currentUsage) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.maxUsage = maxUsage;
        this.currentUsage = currentUsage;
    }
}

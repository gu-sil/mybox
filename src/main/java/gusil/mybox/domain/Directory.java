package gusil.mybox.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "directories")
public class Directory extends BaseEntity{
    @Id
    private String directoryId = UUID.randomUUID().toString();
    private String directoryName;
    private String directoryOwner;
    private String directoryParent;
    private Long size;

    @Builder
    public Directory(String directoryName, String directoryOwner, String directoryParent, Long size) {
        this.directoryName = directoryName;
        this.directoryOwner = directoryOwner;
        this.directoryParent = directoryParent;
        this.size = size;
    }
}

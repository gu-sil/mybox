package gusil.mybox.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "files")
public class File extends BaseEntity{
    @Id
    private String fileId = UUID.randomUUID().toString();
    private String fileName;
    private String fileOwner;
    private String fileParent;
    private Long size;

    @Builder
    public File(String fileName, String fileOwner, String fileParent, Long size) {
        this.fileName = fileName;
        this.fileOwner = fileOwner;
        this.fileParent = fileParent;
        this.size = size;
    }
}

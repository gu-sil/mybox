package gusil.mybox.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

@Data
@Builder
public class UploadFileRequest {
    private String userId;
    private String fileName;
    private String fileParent;
    private Mono<FilePart> filePart;
    private Long fileSize;
}

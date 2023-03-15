package gusil.mybox.mapper;

import gusil.mybox.domain.File;
import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public File mapToFile(UploadFileRequest request) {
        return File.builder()
                .fileName(request.getFileName())
                .fileParent(request.getFileParent())
                .fileOwner(request.getUserId())
                .size(request.getFileSize())
                .build();
    }

    public UploadFileResponse mapToUploadFileResponse(File file) {
        return UploadFileResponse.builder()
                .fileId(file.getFileId())
                .fileName(file.getFileName())
                .fileOwnerId(file.getFileOwner())
                .fileParent(file.getFileParent())
                .fileSize(file.getSize())
                .build();
    }
}

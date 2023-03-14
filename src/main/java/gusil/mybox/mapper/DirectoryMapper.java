package gusil.mybox.mapper;

import gusil.mybox.domain.Directory;
import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import org.springframework.stereotype.Component;

@Component
public class DirectoryMapper {
    public Directory mapToDirectory (CreateDirectoryRequest request) {
        return Directory.builder()
                .directoryName(request.getDirectoryName())
                .directoryOwner(request.getDirectoryOwner())
                .directoryParent(request.getDirectoryParent() == null ? "root" : request.getDirectoryParent())
                .size(0L)
                .build();
    }

    public CreateDirectoryResponse mapToCreateDirectoryResponse(Directory directory) {
        return CreateDirectoryResponse.builder()
                .directoryId(directory.getDirectoryId())
                .directoryName(directory.getDirectoryName())
                .directoryOwner(directory.getDirectoryOwner())
                .directoryParent(directory.getDirectoryParent())
                .size(directory.getSize())
                .build();
    }
}

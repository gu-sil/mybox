package gusil.mybox.dto.response;

import gusil.mybox.dto.type.DirectoryItemType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadDirectoryItemListResponseItem {
    private DirectoryItemType itemType;
    private String itemId;
    private String itemName;
}

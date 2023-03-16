package gusil.mybox.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReadDirectoryItemListResponse {
    private List<ReadDirectoryItemListResponseItem> items = new ArrayList<>();
}

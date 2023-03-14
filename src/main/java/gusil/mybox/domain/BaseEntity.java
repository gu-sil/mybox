package gusil.mybox.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

public class BaseEntity {
    @CreatedDate
    private LocalDateTime createdDt;

    @LastModifiedDate
    private LocalDateTime modifiedDt;

    @Version
    private Integer version;
}

package foo.study.url.domain.entities;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseDateTimeEntity {

    @CreatedDate
    private LocalDateTime createdDateTime;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;

}

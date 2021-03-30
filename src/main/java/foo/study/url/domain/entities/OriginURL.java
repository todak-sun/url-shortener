package foo.study.url.domain.entities;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name = "createdDateTime", column = @Column(name = "CREATED_DATETIME")),
        @AttributeOverride(name = "updatedDateTime", column = @Column(name = "UPDATED_DATETIME"))
})
@Table(name = "TB_ORIGIN_URL")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class OriginURL extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ORIGIN_URL_ID")
    private Long id;

    @Column(name = "URL")
    private String url;

    @Column(name = "SHORTEN_COUNT")
    private int shortenCount;

    @Builder
    public OriginURL(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originURL")
    private List<ShortURL> shortURLS = new ArrayList<>();

    public void increaseShortenCount() {
        this.shortenCount += 1;
    }

}

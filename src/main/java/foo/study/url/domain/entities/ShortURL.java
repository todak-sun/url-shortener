package foo.study.url.domain.entities;


import foo.study.url.ifs.RandomPathGenerator;
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
@Table(name = "TB_SHORT_URL")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ShortURL extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SHORT_URL_ID")
    private Long id;

    @Column(name = "PATH")
    private String path;

    @Column(name = "REQUEST_COUNT")
    private long requestCount;

    @Builder
    public ShortURL(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORIGIN_URL_ID")
    private OriginURL originURL;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shortURL")
    private List<RequestLog> requestLogs = new ArrayList<>();

    public void generatePath(RandomPathGenerator generator) {
        this.path = generator.generate();
    }

    public void increaseRequestCount() {
        ++this.requestCount;
    }

    // ------ 연관관계 메서드 시작
    public void referTo(OriginURL originURL) {
        this.originURL = originURL;
        originURL.getShortURLS().add(this);
        originURL.increaseShortenCount();
    }
    // ------ 연관관계 메서드 끝

}

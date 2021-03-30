package foo.study.url.domain.entities;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name = "createdDateTime", column = @Column(name = "CREATED_DATETIME")),
        @AttributeOverride(name = "updatedDateTime", column = @Column(name = "UPDATED_DATETIME"))
})
@Table(name = "TB_REQUEST_LOG")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class RequestLog extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "REQUEST_LOG_ID")
    private Long id;

    @Column(name = "IP")
    private String ip;

    @Column(name = "REFERER")
    private String referer;

    @Builder
    public RequestLog(Long id, String ip, String referer) {
        this.id = id;
        this.ip = ip;
        this.referer = referer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHORT_URL_ID")
    private ShortURL shortURL;

    // -------- 연관관계 메서드 시작
    public void loggedFrom(ShortURL shortURL) {
        this.shortURL = shortURL;
        shortURL.getRequestLogs().add(this);
        shortURL.increaseRequestCount();
    }
    // -------- 연관관계 메서드 끝

}

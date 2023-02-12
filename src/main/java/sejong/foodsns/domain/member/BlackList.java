package sejong.foodsns.domain.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BlackList extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "black_list_id")
    private Long id;

    // 블랙리스트인 사유
    @Column(name = "penalty_reason")
    private String reason;

    // 신고 당한 회원
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public BlackList(String reason, Member member) {
        this.reason = reason;
        this.member = member;
    }
}

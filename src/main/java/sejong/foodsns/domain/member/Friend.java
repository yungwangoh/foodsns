package sejong.foodsns.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Friend extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    private String friendName;

    private String friendEmail;

    private int recommendCount;

    private MemberRank memberRank;

    private Long reportCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Friend(Member member) {
        this.friendName = member.getUsername();
        this.friendEmail = member.getEmail();
        this.recommendCount = member.getRecommendCount();
        this.memberRank = member.getMemberRank();
        this.reportCount = member.getReportCount();
    }

    /**
     * 친구 추가할 주최자
     * @param member 회원 (친구 추가하고자 하는 회원 아님).
     */
    public void setMember(Member member) {
        if(member != null) {
            if(member != this.getMember()) {
                this.member = member;
            } else {
                throw new IllegalArgumentException("친구가 본인이 될 수 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("본인 회원이 없습니다.");
        }
    }
}

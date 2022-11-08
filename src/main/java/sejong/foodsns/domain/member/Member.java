package sejong.foodsns.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static sejong.foodsns.domain.member.MemberNumberOfCount.*;
import static sejong.foodsns.domain.member.MemberRank.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "report_count")
    private int reportCount;

    @Enumerated(value = STRING)
    @Column(name = "member_rank")
    private MemberRank memberRank;

    @Enumerated(value = STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    @Column(name = "penalty")
    private int penalty;

    @OneToMany
    @JoinColumn(name = "board_id")
    private List<Board> boards;

    @Builder
    public Member(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 연관 관계 편의 메서드, 비즈니스 로직

    // 추천 수 -> 회원 등급
    public void memberRecommendUp(int recommendCount) {
        if(bronzeRank(recommendCount)) {
            this.memberRank = BRONZE;
        } else if (silverRank(recommendCount)) {
            this.memberRank = SILVER;
        } else if (goldRank(recommendCount)) {
            this.memberRank = GOLD;
        } else if (platinumRank(recommendCount)) {
            this.memberRank = PLATINUM;
        } else if (diamondRank(recommendCount)) {
            this.memberRank = DIAMOND;
        } else if (vipRank(recommendCount)) {
            this.memberRank = VIP;
        } else {
            this.memberRank = NORMAL;
        }
    }

    public void reportCount() {
        this.reportCount++;
    }

    public void penaltyCount() {
        this.penalty++;
    }

    private boolean bronzeRank(int recommendCount) {
        return recommendCount >= bronzeNumOfRecommend && recommendCount < silverNumOfRecommend;
    }

    private boolean silverRank(int recommendCount) {
        return recommendCount >= silverNumOfRecommend && recommendCount < goldNumOfRecommend;
    }

    private boolean goldRank(int recommendCount) {
        return recommendCount >= goldNumOfRecommend && recommendCount < platinumNumOfRecommend;
    }

    private boolean platinumRank(int recommendCount) {
        return recommendCount >= platinumNumOfRecommend && recommendCount < diamondNumOfRecommend;
    }

    private boolean diamondRank(int recommendCount) {
        return recommendCount >= diamondNumOfRecommend && recommendCount < vipNumOfRecommend;
    }

    private boolean vipRank(int recommendCount) {
        return recommendCount >= vipNumOfRecommend;
    }
}

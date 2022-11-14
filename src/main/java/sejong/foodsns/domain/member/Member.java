package sejong.foodsns.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
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
    @NotEmpty
    private String username;

    @Column(name = "email")
    @NotEmpty
    private String email;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "report_count")
    private Long reportCount;

    @Enumerated(value = STRING)
    @Column(name = "member_rank")
    private MemberRank memberRank;

    @Enumerated(value = STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Board> boards;

    @Column(name = "penalty")
    private int penalty;

    @Builder
    public Member(String username, String email, String password, MemberType memberType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.memberType = memberType;
    }

    // 연관 관계 편의 메서드, 비즈니스 로직

    // 추천 수 -> 회원 등급

    /**
     * @param recommendCount
     */
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

    /**
     * @param username
     */
    public void memberNameUpdate(String username) {
        this.username = username;
    }

    /**
     * @param email
     */
    public void memberEmailUpdate(String email) {
        this.email = email;
    }

    /**
     * @param password
     */
    public void memberPasswordUpdate(String password) {
        this.password = password;
    }

    public void reportCount() {
        this.reportCount++;
    }

    public void penaltyCount() {
        this.penalty++;
    }

    /**
     * @param recommendCount
     * @return
     */
    private boolean bronzeRank(int recommendCount) {
        return recommendCount >= bronzeNumOfRecommend && recommendCount < silverNumOfRecommend;
    }

    /**
     * @param recommendCount
     * @return
     */
    private boolean silverRank(int recommendCount) {
        return recommendCount >= silverNumOfRecommend && recommendCount < goldNumOfRecommend;
    }

    /**
     * @param recommendCount
     * @return
     */
    private boolean goldRank(int recommendCount) {
        return recommendCount >= goldNumOfRecommend && recommendCount < platinumNumOfRecommend;
    }

    /**
     * @param recommendCount
     * @return
     */
    private boolean platinumRank(int recommendCount) {
        return recommendCount >= platinumNumOfRecommend && recommendCount < diamondNumOfRecommend;
    }

    /**
     * @param recommendCount
     * @return
     */
    private boolean diamondRank(int recommendCount) {
        return recommendCount >= diamondNumOfRecommend && recommendCount < vipNumOfRecommend;
    }

    /**
     * @param recommendCount
     * @return
     */
    private boolean vipRank(int recommendCount) {
        return recommendCount >= vipNumOfRecommend;
    }
}

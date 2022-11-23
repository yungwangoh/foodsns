package sejong.foodsns.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    // 유저 이름 20자
    @Column(name = "username", unique = true)
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2 ~ 10자리여야 합니다.")
    private String username;

    // 유저 이메일 100자
    @Column(name = "email", length = 50, unique = true)
    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    // 유저 비밀번호 20자
    @Column(name = "password")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Column(name = "report_count")
    private Long reportCount = 0L;

    @Enumerated(value = STRING)
    @Column(name = "member_rank")
    private MemberRank memberRank;

    @Enumerated(value = STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    @Column(name = "recommend_count")
    private int recommendCount;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();

    @Column(name = "penalty")
    private int penalty = 0;

    @Builder
    public Member(String username, String email, String password, MemberType memberType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.memberType = memberType;
    }

    // 연관 관계 편의 메서드, 비즈니스 로직
    public void setBoards(Board boards) {
        this.boards.add(boards);
        if(boards.getMember() != this) {
            boards.setMember(this);
        }
    }
    // 추천 수 -> 회원 등급

    /**
     * 유저 회원 등급
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
     * 유저 이름 수정
     * @param username
     * @return
     */
    public Member memberNameUpdate(String username) {
        this.username = username;
        return this;
    }

    /**
     * 유저 이메일 수정
     * @param email
     * @return
     */
    public Member memberEmailUpdate(String email) {
        this.email = email;
        return this;
    }

    /**
     * 유저 비밀번호 수정
     * @param password
     * @return
     */
    public Member memberPasswordUpdate(String password) {
        this.password = password;
        return this;
    }

    /**
     * 유저 추천 수
     * @param recommendCount
     * @return
     */
    public Member memberRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
        return this;
    }

    /**
     * 유저 신고 리포트 수 증가
     */
    public void reportCount() {
        this.reportCount++;
    }

    /**
     * 유저 패널티 수 증가
     */
    public void penaltyCount() {
        this.penalty++;
    }

    /**
     * 유저 브론즈 등급 추천 수 10 ~ 29
     * @param recommendCount
     * @return
     */
    private boolean bronzeRank(int recommendCount) {
        return recommendCount >= bronzeNumOfRecommend && recommendCount < silverNumOfRecommend;
    }

    /**
     * 유저 실버 등급 추천 수 30 ~ 49
     * @param recommendCount
     * @return
     */
    private boolean silverRank(int recommendCount) {
        return recommendCount >= silverNumOfRecommend && recommendCount < goldNumOfRecommend;
    }

    /**
     * 유저 골드 등급 추천 수 50 ~ 79
     * @param recommendCount
     * @return
     */
    private boolean goldRank(int recommendCount) {
        return recommendCount >= goldNumOfRecommend && recommendCount < platinumNumOfRecommend;
    }

    /**
     * 유저 플래티넘 등급 추천 수 80 ~ 99
     * @param recommendCount
     * @return
     */
    private boolean platinumRank(int recommendCount) {
        return recommendCount >= platinumNumOfRecommend && recommendCount < diamondNumOfRecommend;
    }

    /**
     * 유저 다이아몬드 등급 추천 수 100 ~ 149
     * @param recommendCount
     * @return
     */
    private boolean diamondRank(int recommendCount) {
        return recommendCount >= diamondNumOfRecommend && recommendCount < vipNumOfRecommend;
    }

    /**
     * 유저 VIP 등급 추천 수 150 ~ inf
     * @param recommendCount
     * @return
     */
    private boolean vipRank(int recommendCount) {
        return recommendCount >= vipNumOfRecommend;
    }
}

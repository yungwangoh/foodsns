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
import java.util.*;
import java.util.stream.Stream;

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

    // 신고 수
    @Column(name = "report_count")
    private Long reportCount;

    // 회원 등급
    @Enumerated(value = STRING)
    @Column(name = "member_rank")
    private MemberRank memberRank;

    // 일반 회원, 관리자 회원
    @Enumerated(value = STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    // 추천 수
    @Column(name = "recommend_count")
    private int recommendCount;

    // 친구 목록
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Friend> friends;

    // 회원 게시물
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Board> boards;

    @Builder
    public Member(String username, String email, String password, MemberType memberType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.memberType = memberType;
        this.memberRank = NORMAL;
        this.reportCount = 0L;
        this.boards = new ArrayList<>();
        this.friends = new ArrayList<>(5);
    }

    /**
     * 게시물 연관 관계 편의 메서드, 비즈니스 로직
     * @param board
     */
    public void setBoards(Board board) {
        this.boards.add(board);
        if(board.getMember() != this) {
            board.setMember(this);
        }
    }

    /**
     * 친구 추가 연관 관계 편의 메서드
     * @param friend
     */
    public void setFriends(Friend friend) {
        if(!this.username.equals(friend.getFriendName())) {

            // 친구 중복 체크.
            if(duplicatedCheck(friend))
                throw new IllegalArgumentException("같은 친구를 추가할 수 없습니다.");

            this.friends.add(friend);
        } else {
            throw new IllegalArgumentException("자신을 친구 추가할 수 없습니다.");
        }
    }

    /**
     * 중복 체크 로직
     * @param friend 친구
     * @return 중복이면 true, 아니면 false
     */
    private boolean duplicatedCheck(Friend friend) {
        for(Friend f : this.getFriends()) {
            if(friend.getFriendName().equals(f.getFriendName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 유저 회원 등급
     * @param recommendCount
     */
    public void memberRankUp(int recommendCount) {
        negativeNumExceptionCheck(recommendCount);

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
        negativeNumExceptionCheck(recommendCount);
        this.recommendCount = recommendCount;
        return this;
    }

    public void memberBlackListType(MemberType memberType) {
        this.memberType = memberType;
    }

    /**
     * 유저 신고 리포트 수 증가
     */
    public void reportCount() {
        this.reportCount++;
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

    /**
     * 회원 추천수가 음수일 떄 예외
     * @param recommendCount
     */
    private void negativeNumExceptionCheck(int recommendCount) {
        if(recommendCount < 0) {
            throw new IllegalArgumentException("잘못된 추천 수 입니다.");
        }
    }
}

package sejong.foodsns.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
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
    private static MemberRank memberRank;

    @OneToOne(fetch = LAZY)
    private ReportMember reportMember;

    @Builder
    public Member(String username, String email, String password, int reportCount, ReportMember reportMember) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.reportCount = reportCount;
        this.reportMember = reportMember;
    }

    // 연관 관계 편의 메서드, 비즈니스 로직

    // 추천 수 -> 회원 등급
    public static void memberRecommendUp(int recommendCount) {
        if(bronzeRank(recommendCount)) {
            Member.memberRank = BRONZE;
        } else if (silverRank(recommendCount)) {
            Member.memberRank = SILVER;
        } else if (goldRank(recommendCount)) {
            Member.memberRank = GOLD;
        } else if (platinumRank(recommendCount)) {
            Member.memberRank = PLATINUM;
        } else if (diamondRank(recommendCount)) {
            Member.memberRank = DIAMOND;
        } else if (vipRank(recommendCount)) {
            Member.memberRank = VIP;
        } else {
            Member.memberRank = NORMAL;
        }
    }

    public void memberReportCount() {
        
    }

    // 신고 수 -> 총 신고 수가 30(패널티 3개 : 10개당 패널티 하나) 넘을 경우 강제 회원 탈퇴 -> 게시물 다 삭제
    public void memberRemove(int penaltyCount) {
        if(penaltyCount >= penalty) {
            List<Board> board = reportMember.getBlackList().getBoard();
            board.clear();
        }
    }

    private static boolean bronzeRank(int recommendCount) {
        return recommendCount >= bronzeNumOfRecommend && recommendCount < silverNumOfRecommend;
    }

    private static boolean silverRank(int recommendCount) {
        return recommendCount >= silverNumOfRecommend && recommendCount < goldNumOfRecommend;
    }

    private static boolean goldRank(int recommendCount) {
        return recommendCount >= goldNumOfRecommend && recommendCount < platinumNumOfRecommend;
    }

    private static boolean platinumRank(int recommendCount) {
        return recommendCount >= platinumNumOfRecommend && recommendCount < diamondNumOfRecommend;
    }

    private static boolean diamondRank(int recommendCount) {
        return recommendCount >= diamondNumOfRecommend && recommendCount < vipNumOfRecommend;
    }

    private static boolean vipRank(int recommendCount) {
        return recommendCount >= vipNumOfRecommend;
    }
}

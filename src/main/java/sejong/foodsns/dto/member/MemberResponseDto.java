package sejong.foodsns.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Data
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String username;
    private String email;
    private MemberRank memberRank;
    private int recommendCount;
    private Long reportCount;
    private int penaltyCount;
    private MemberType memberType;

    @Builder
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.recommendCount = member.getRecommendCount();
        this.reportCount = member.getReportCount();
        this.memberRank = member.getMemberRank();
        this.memberType = member.getMemberType();
    }
}

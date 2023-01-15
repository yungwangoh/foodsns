package sejong.foodsns.dto.member.friend;

import lombok.*;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberFriendResponseDto {

    private String email;
    private String username;
    private int recommendCount;
    private Long reportCount;
    private MemberType memberType;

    public MemberFriendResponseDto(Friend friend) {
        this.email = friend.getMember().getEmail();
        this.username = friend.getMember().getUsername();
        this.recommendCount = friend.getMember().getRecommendCount();
        this.memberType = friend.getMember().getMemberType();
        this.reportCount = friend.getMember().getReportCount();
    }
}

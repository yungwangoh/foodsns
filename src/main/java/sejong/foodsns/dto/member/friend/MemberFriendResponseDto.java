package sejong.foodsns.dto.member.friend;

import lombok.*;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.MemberRank;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberFriendResponseDto {

    private String email;
    private String username;
    private int recommendCount;
    private Long reportCount;
    private MemberRank memberRank;

    public MemberFriendResponseDto(Friend friend) {
        this.email = friend.getFriendEmail();
        this.username = friend.getFriendName();
        this.recommendCount = friend.getRecommendCount();
        this.reportCount = friend.getReportCount();
        this.memberRank = friend.getMemberRank();
    }
}

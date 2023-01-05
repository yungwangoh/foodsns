package sejong.foodsns.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;

import java.util.Optional;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private MemberRank memberRank;

    private MemberType memberType;

    @Builder
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.memberRank = member.getMemberRank();
        this.memberType = member.getMemberType();
    }

}

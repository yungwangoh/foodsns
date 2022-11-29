package sejong.foodsns.dto.member.login;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberLogoutDto {

    private String message;
}

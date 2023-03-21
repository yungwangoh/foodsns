package sejong.foodsns.dto.reply;

import lombok.*;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder
public class ReplyUpdateDto {

    private Long replyId;
    private String updateContent;
}

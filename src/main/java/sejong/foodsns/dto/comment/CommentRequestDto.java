package sejong.foodsns.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentRequestDto {

    @Schema(description = "댓글 내용", example = "레시피 후기 Good.")
    private String content;
    @Schema(description = "유저 이메일", example = "swager253@naver.com")
    private String email;
    @Schema(description = "게시물 아이디")
    private Long boardId;
}

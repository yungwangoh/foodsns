package sejong.foodsns.dto.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.file.BoardFileResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long check;
    private int recommCount;
    private MemberResponseDto memberResponseDto;
    private List<BoardFileResponseDto> boardFileResponseDtos;

    @Builder
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberResponseDto = new MemberResponseDto(board.getMember());
        this.check = board.getCheck();
        this.recommCount = board.getRecommCount();
        this.boardFileResponseDtos = getBoardFileResponseDtos(board);
    }

    /**
     * 게시물 파일 리스트 -> 게시물 파일 응답 Dto Convert
     * @param board 게시물 Object
     * @return 게시물 파일 Dto List
     */
    private static List<BoardFileResponseDto> getBoardFileResponseDtos(Board board) {
        return board.getBoardFiles().stream().map(BoardFileResponseDto::new)
                .collect(toList());
    }
}

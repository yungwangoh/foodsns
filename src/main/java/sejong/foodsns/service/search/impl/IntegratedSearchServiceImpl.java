package sejong.foodsns.service.search.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.search.IntegratedSearchResponseDto;
import sejong.foodsns.repository.querydsl.search.IntegratedSearchRepository;
import sejong.foodsns.service.search.IntegratedSearchService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class IntegratedSearchServiceImpl implements IntegratedSearchService {

    private final IntegratedSearchRepository integratedSearchRepository;

    @Override
    public ResponseEntity<IntegratedSearchResponseDto> integratedSearch(String content) {

        List<Board> boards = integratedSearchRepository.boardIntegratedSearch(content);
        List<Comment> comments = integratedSearchRepository.commentIntegratedSearch(content);
        List<Reply> replies = integratedSearchRepository.replyIntegratedSearch(content);

        List<BoardResponseDto> boardResponseDtos = boards.stream().map(BoardResponseDto::new).collect(toList());
        List<CommentResponseDto> commentResponseDtos = comments.stream().map(CommentResponseDto::new).collect(toList());
        List<ReplyResponseDto> replyResponseDtos = replies.stream().map(ReplyResponseDto::new).collect(toList());

        IntegratedSearchResponseDto integratedSearchResponseDto = new IntegratedSearchResponseDto(
                boardResponseDtos,
                commentResponseDtos,
                replyResponseDtos
        );

        return new ResponseEntity<>(integratedSearchResponseDto, OK);
    }

    @Override
    public ResponseEntity<List<BoardResponseDto>> boardIntegratedSearch(String content) {

        List<Board> boards = integratedSearchRepository.boardIntegratedSearch(content);

        List<BoardResponseDto> boardResponseDtos = boards.stream().map(BoardResponseDto::new).collect(toList());

        return new ResponseEntity<>(boardResponseDtos, OK);
    }

    @Override
    public ResponseEntity<List<CommentResponseDto>> commentIntegratedSearch(String content) {

        List<Comment> comments = integratedSearchRepository.commentIntegratedSearch(content);

        List<CommentResponseDto> commentResponseDtos = comments.stream().map(CommentResponseDto::new).collect(toList());

        return new ResponseEntity<>(commentResponseDtos, OK);
    }

    @Override
    public ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearch(String content) {

        List<Reply> replies = integratedSearchRepository.replyIntegratedSearch(content);

        List<ReplyResponseDto> replyResponseDtos = replies.stream().map(ReplyResponseDto::new).collect(toList());

        return new ResponseEntity<>(replyResponseDtos, OK);
    }
}

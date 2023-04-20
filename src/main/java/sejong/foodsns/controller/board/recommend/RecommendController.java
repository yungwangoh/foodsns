package sejong.foodsns.controller.board.recommend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.service.board.crud.BoardCrudService;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final BoardCrudService boardCrudService;

    @GetMapping("/board/recommend-up")
    public ResponseEntity<BoardResponseDto> recommendUpController(@RequestParam("username") String username,
                                                                  @RequestParam("boardId") Long boardId) {

        ResponseEntity<BoardResponseDto> boardRecommendUp = boardCrudService.boardRecommendUp(username, boardId);

        return new ResponseEntity<>(boardRecommendUp.getBody(), boardRecommendUp.getStatusCode());
    }

    @GetMapping("/board/recommend-down")
    public ResponseEntity<BoardResponseDto> recommendDownController(@RequestParam("username") String username,
                                                                    @RequestParam("boardId") Long boardId) {

        ResponseEntity<BoardResponseDto> boardRecommendDown = boardCrudService.boardRecommendDown(username, boardId);

        return new ResponseEntity<>(boardRecommendDown.getBody(), boardRecommendDown.getStatusCode());
    }
}

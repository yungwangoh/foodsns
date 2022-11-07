package sejong.foodsns.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BlackList extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "blacklist_id")
    private Long id;

    @Column(name = "penalty_reason")
    private String reason;

    @OneToMany
    @JoinColumn(name = "board_id")
    private List<Board> board;

    @Builder
    public BlackList(String reason, List<Board> board) {
        this.reason = reason;
        this.board = board;
    }
}

package sejong.foodsns.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BoardFile extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFile(UUID uuid, String fileName, Board board) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.board = board;
    }
}

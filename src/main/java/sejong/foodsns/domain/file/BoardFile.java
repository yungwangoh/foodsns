package sejong.foodsns.domain.file;

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

    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;
    @Column(name = "file_name")
    private String originFilename;
    @Column(name = "store_name")
    private String storeFileName;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFile(String originFileName, String storePath, Board board) {
        this.originFilename = originFileName;
        this.storeFileName = storePath;
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}

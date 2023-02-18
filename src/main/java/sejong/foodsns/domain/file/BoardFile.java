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

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "file_name")
    private String originFilename;

    private String storeFileName;

    @Enumerated(EnumType.STRING)
    private BoardFileType boardFileType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFile(UUID uuid, String originFileName, String storePath,
                     Board board, BoardFileType boardFileType) {
        this.uuid = uuid;
        this.originFilename = originFileName;
        this.storeFileName = storePath;
        this.board = board;
        this.boardFileType = boardFileType;
    }
}

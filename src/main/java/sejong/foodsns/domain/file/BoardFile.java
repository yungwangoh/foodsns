package sejong.foodsns.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.UUID;

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

    @Builder
    public BoardFile(UUID uuid, String fileName) {
        this.uuid = uuid;
        this.fileName = fileName;
    }
}

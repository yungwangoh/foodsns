package sejong.foodsns.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    @Enumerated(value = STRING)
    @Column(name = "member_rank")
    private MemberRank memberRank;

    // 조회수
    @Column(name = "num_check")
    private Long check;

    @Column(name = "recommend_count")
    private int recommCount;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private FoodTag foodTag;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany
    @JoinColumn(name = "file_id")
    private List<BoardFile> boardFiles;

    @Builder
    public Board(Member member, String title, String content, MemberRank memberRank, Long check,
                 int recommCount, FoodTag foodTag) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.memberRank = memberRank;
        this.check = check;
        this.recommCount = recommCount;
        this.foodTag = foodTag;
    }

    // 비즈니스 로직, 연관관계 편의 메서드

}

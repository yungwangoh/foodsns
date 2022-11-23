package sejong.foodsns.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;
//hid
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private FoodTag foodTag;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BoardFile> boardFiles;

    @Builder
    public Board(String title, String content, MemberRank memberRank, Long check, int recommCount,
                 FoodTag foodTag, Member member, List<Comment> comments, List<BoardFile> boardFiles) {
        this.title = title;
        this.content = content;
        this.memberRank = memberRank;
        this.check = check;
        this.recommCount = recommCount;
        this.foodTag = foodTag;
        this.member = member;
        this.comments = comments;
        this.boardFiles = boardFiles;
    }

    // 비즈니스 로직, 연관관계 편의 메서드
    public void setMember(Member member) {
        if(this.member != null) {
            this.member.getBoards().remove(this);
        }
        this.member = member;
        member.getBoards().add(this);
    }
}

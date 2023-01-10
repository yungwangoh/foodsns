package sejong.foodsns.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

//숨겨진커밋(11.23)
//push -> hayoon
//push -> ygo
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

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "food_id")
    private FoodTag foodTag;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
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
                 @Nullable FoodTag foodTag, Member member, @Nullable List<Comment> comments, @Nullable List<BoardFile> boardFiles) {
        this.title = title; // 게시물
        this.content = content; // 요리메뉴
        this.memberRank = memberRank; //멤버랭크
        this.check = check; // 조회수
        this.recommCount = recommCount; // 추천수
        this.foodTag = foodTag; //
        this.member = member; // 게시물작성자
        this.comments = comments; // 댓글
        this.boardFiles = boardFiles; // 첨부파일
    }

    // 비즈니스 로직, 연관관계 편의 메서드
    public void setMember(Member member) {
        if(this.member != null) {
            this.member.getBoards().remove(this);
        }
        this.member = member;
        member.getBoards().add(this);
    }

    public void plusRecommendCount() {
        this.recommCount++;
    }

    /**
     * 게시물 제목 수정
     * @param title
     * @return
     */
    public Board boardTitleUpdate(String title) {
        this.title = title;
        return this;
    }
}

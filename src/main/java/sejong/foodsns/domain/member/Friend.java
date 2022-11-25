package sejong.foodsns.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Friend extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @Builder
    public Friend(Member member) {
        this.member = member;
    }
}

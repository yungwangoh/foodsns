package sejong.foodsns.domain.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FoodTag extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "food_id")
    private Long id;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "food_type")
    private String foodType;

    @Builder
    public FoodTag(String foodName, String foodType) {
        this.foodName = foodName;
        this.foodType = foodType;
    }
}

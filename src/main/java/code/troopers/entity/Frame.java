package code.troopers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Frame {

    private int frameNumber;
    private Integer firstRoll;
    private Integer secondRoll;
    private Integer frameScore;
    boolean isCalculated;

    public Frame(int frameNumber) {
        this.frameNumber = frameNumber;
    }


}

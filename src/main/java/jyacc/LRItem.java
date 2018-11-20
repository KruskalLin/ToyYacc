package jyacc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class LRItem {
    private final RegularExpression regularExpression;
    private final String predictions;

    public LRItem(RegularExpression regularExpression, String first) {
        this.regularExpression = regularExpression;
        this.predictions = first;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRItem lrItem = (LRItem) o;
        return Objects.equals(regularExpression, lrItem.regularExpression) &&
                Objects.equals(predictions, lrItem.predictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regularExpression, predictions);
    }

    public RegularExpression getRegularExpression() {
        return regularExpression;
    }

    public List<String> getPredictions() {
        List<String> pre = regularExpression.getRight().getCurrentRightString();
        pre.add(predictions);
        return pre;
    }

    public String getPrediction() {
        return this.predictions;
    }

    @Override
    public String toString() {
        return "LRItem{" +
                "regularExpression=" + regularExpression.toString() +
                ", predictions='" + predictions + '\'' +
                '}';
    }
}

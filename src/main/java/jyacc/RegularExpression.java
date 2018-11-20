package jyacc;

import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class RegularExpression {

    private LeftSection left;

    private RightSection right;

    public RegularExpression(String left, String right) {
        this.left = new LeftSection(left);
        this.right = new RightSection(right);
    }

    @Override
    public String toString() {
        return "RegularExpression{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegularExpression that = (RegularExpression) o;
        return Objects.equals(left.getSection(), that.left.getSection()) &&
                Objects.equals(right.getSection(), that.right.getSection());
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    public LeftSection getLeft() {
        return left;
    }

    public RightSection getRight() {
        return right;
    }
}

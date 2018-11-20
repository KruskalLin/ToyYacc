package jinter;

import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class Node extends ASTCell {
    public ASTCell former;
    public ASTCell latter;

    public Node(String id, ASTCell former, ASTCell latter) {
        super(id);
        this.former = former;
        this.latter = latter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(former, node.former) &&
                Objects.equals(latter, node.latter) &&
                Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(former, latter);
    }

    @Override
    public String toString() {
        return former.toString() + " " + latter.toString() + " Node := " + id + " ";
    }
}

package jinter;

import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class Leaf extends ASTCell {
    private String entry;

    public Leaf(String id, String entry) {
        super(id);
        this.entry = entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaf leaf = (Leaf) o;
        return Objects.equals(entry, leaf.entry) && Objects.equals(id, leaf.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entry);
    }


    @Override
    public String toString() {
        return "Leaf :=" + id;
    }
}

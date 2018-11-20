package jyacc;

import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class LRDFACell {
    private final Map<String, Integer> map = new HashMap<>();
    private final Set<LRItem> closure = new HashSet<>();

    public LRDFACell(Set<LRItem> lrItemSet) {
        closure.addAll(lrItemSet);
    }


    public Set<LRItem> getClosure() {
        return closure;
    }

    public void addTransition(String input, int next) {
        map.put(input, next);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "LRDFACell{" +
                "map=" + map.toString() +
                ", closure=" + closure.toString() +
                ", size=" + closure.size() +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRDFACell lrdfaCell = (LRDFACell) o;
        return Objects.equals(closure, lrdfaCell.closure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closure);
    }
}

package jlexical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/28
 * @Todo:
 */
public class NFACell {
    public final Set<Integer> etrans = new HashSet<>();
    public final Map<Character, Set<Integer>> strans = new HashMap<>();

    public void addTransition(char input, int next) {
        Set<Integer> set = strans.computeIfAbsent(input, k -> new HashSet<>());
        set.add(next);
    }

    public void addEpsilon(int next) {
        etrans.add(next);
    }


}
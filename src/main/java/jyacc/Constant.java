package jyacc;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class Constant {
    public static final int strLen = 100000;
    public static final String newLine = "\r\n";
    public static List<String> nonTerminals = new ArrayList<>();
    public static List<String> terminals = new ArrayList<>();
    public static List<Integer> priority = new ArrayList<>();

    public static int getPosition(String s) {
        if (terminals.contains(s)) {
            return terminals.indexOf(s);
        } else {
            return terminals.size() + nonTerminals.indexOf(s);
        }
    }

    public static int getPriority(int i) {
        return priority.get(i);
    }


}

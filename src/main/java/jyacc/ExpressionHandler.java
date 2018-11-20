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
public class ExpressionHandler {
    public static List<RegularExpression> preprocess(String expressions) {
        List<RegularExpression> list = new ArrayList<>();
        String[] strs = expressions.split(Constant.newLine);
        for (int i = 0; i < strs.length; i++) {
            String[] re = strs[i].trim().split("(->)");
            String[] rights = re[1].split("_");
            for (int j = 0; j < rights.length; j++) {
                if (i == 0 && j == 0) {
                    list.add(new RegularExpression("START", re[0]));
                }
                list.add(new RegularExpression(re[0], rights[j]));
            }
        }
        return list;
    }
}

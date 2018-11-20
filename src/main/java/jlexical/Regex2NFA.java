package jlexical;

import java.util.Stack;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/28
 * @Todo:
 */
public class Regex2NFA {

    public static RegexTreeCell regex2Tree(String regex) {
        String postfix = RegexUtil.infix2postfix(regex);
        Stack<RegexTreeCell> s = new Stack<>();
        for (int i = 0; i < postfix.length(); i++) {
            switch (postfix.charAt(i)) {
                case '.':
                    RegexTreeCell b1 = s.pop();
                    RegexTreeCell a1 = s.pop();
                    RegexTreeCell cell1 = new BiTreeCell.ConcatenationCell(a1, b1);
                    s.push(cell1);
                    break;
                case '|':
                    RegexTreeCell b2 = s.pop();
                    RegexTreeCell a2 = s.pop();
                    RegexTreeCell cell2 = new BiTreeCell.UnionCell(a2, b2);
                    s.push(cell2);
                    break;
                case '*':
                    RegexTreeCell r1 = s.pop();
                    RegexTreeCell cell3 = new SiTreeCell.KleeneClosureCell(r1);
                    s.push(cell3);
                    break;
                case '~':
                    RegexTreeCell cell4 = new NoTreeCell.EpsilonCell();
                    s.push(cell4);
                    break;
                default:
                    RegexTreeCell cell5 = new NoTreeCell.CharCell(postfix.charAt(i));
                    s.push(cell5);
                    break;
            }
        }
        return s.pop();
    }

    public static NFA regex2NFA(String regex) {
        NFA context = new NFA();
        RegexTreeCell cell = regex2Tree(regex);
        cell.construct(context);
        return context;
    }


}

package jyacc;

import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class LRDFA {
    private final List<RegularExpression> G;
    private final List<RegularExpression> GModify;
    private final List<LRDFACell> cells = new ArrayList<>();

    public LRDFA(List<RegularExpression> g) {
        G = g;
        System.out.println(G.toString());
        GModify = new ArrayList<>(G);
        for (int i = 0; i < Constant.nonTerminals.size(); i++) {
            if (!containEpsilonInG(Constant.nonTerminals.get(i))) {
                String left = Constant.nonTerminals.get(i);
                Stack<String> leftStack = new Stack<>();
                if (canBeEpsilon(left, leftStack)) {
                    GModify.add(new RegularExpression(left, "~"));
                }
            }
        }
        items();
        System.out.println(cells);
    }

    public Set<LRItem> closure(Set<LRItem> I) {
        Set<LRItem> originItems = new HashSet<>();
        Set<LRItem> items = new HashSet<>(I);
        while (originItems.size() != items.size()) {
            originItems = new HashSet<>(items);
            for (LRItem item : originItems) {
                if (Constant.nonTerminals.contains(item.getRegularExpression().getRight().getCurrentString())) {
                    for (int i = 0; i < G.size(); i++) {
                        if (G.get(i).getLeft().getSection().equals(item.getRegularExpression().getRight().getCurrentString())) {
                            List<String> firsts = first(item.getPredictions());
                            for (String s : firsts) {
                                items.add(new LRItem(G.get(i), s));
                            }
                        }
                    }
                }
            }
        }
        return items;
    }


    public Set<LRItem> GOTO(Set<LRItem> I, String X) {
        Set<LRItem> J = new HashSet<>();
        for (LRItem item : I) {
            if (item.getRegularExpression().getRight().getCurrentString().equals(X)) {
                RegularExpression regularExpression = new RegularExpression(item.getRegularExpression().getLeft().getSection(), item.getRegularExpression().getRight().getSection());
                regularExpression.getRight().setReduceCursor(item.getRegularExpression().getRight().getReduceCursor());
                regularExpression.getRight().shift();
                J.add(new LRItem(regularExpression, item.getPrediction()));
            }
        }
        return closure(J);
    }

    public void items() {
        Set<LRItem> set = new HashSet<>();
        set.add(new LRItem(G.get(0), "$"));
        cells.add(new LRDFACell(closure(set)));
        List<LRDFACell> originCells = new ArrayList<>();
        while (originCells.size() != cells.size()) {
            System.out.println(originCells);
            originCells = new ArrayList<>(cells);
            for (LRDFACell cell : originCells) {
                List<String> V = new ArrayList<>(Constant.terminals);
                V.addAll(Constant.nonTerminals);
                for (String X : V) {
                    Set<LRItem> s = GOTO(cell.getClosure(), X);
                    LRDFACell lrdfaCell = new LRDFACell(s);
                    if (s.size() > 0 && !cells.contains(lrdfaCell)) {
                        int num = cells.size();
                        cells.add(lrdfaCell);
                        cell.addTransition(X, num);
                    } else if (cells.contains(lrdfaCell)) {
                        cell.addTransition(X, cells.indexOf(lrdfaCell));
                    }
                }
            }
        }
    }


    public List<String> first(List<String> pre) {
        Stack<String> leftStack = new Stack<>();
        return first(pre, leftStack);
    }


    public List<String> first(List<String> pre, Stack<String> leftStack) {
        List<String> firsts = new ArrayList<>();
        for (int i = 0; i < pre.size(); i++) {
            if (Constant.nonTerminals.contains(pre.get(i))) {
                if (!leftStack.contains(pre.get(i))) {
                    leftStack.push(pre.get(i));
                    if (containEpsilonInGM(pre.get(i))) {
                        for (int j = 0; j < G.size(); j++) {
                            if (G.get(j).getLeft().getSection().equals(pre.get(i))) {
                                if (!G.get(j).getRight().getSection().equals("~")) {
                                    firsts.addAll(first(Arrays.asList(G.get(j).getRight().getSection().split("[ ]+")), leftStack));
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < G.size(); j++) {
                            if (G.get(j).getLeft().getSection().equals(pre.get(i))) {
                                if (!G.get(j).getRight().getSection().equals("~")) {
                                    firsts.addAll(first(Arrays.asList(G.get(j).getRight().getSection().split("[ ]+")), leftStack));
                                }
                            }
                        }
                        break;
                    }
                }
            } else if (pre.get(i).equals("~")) {
                continue;
            } else {
                firsts.add(pre.get(i));
                break;
            }
        }
        return firsts;
    }


    private boolean containEpsilonInG(String left) {
        for (int i = 0; i < G.size(); i++) {
            if (G.get(i).getLeft().getSection().equals(left)) {
                if (G.get(i).getRight().getSection().equals("~")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containEpsilonInGM(String left) {
        for (int i = 0; i < GModify.size(); i++) {
            if (GModify.get(i).getLeft().getSection().equals(left)) {
                if (GModify.get(i).getRight().getSection().equals("~")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canBeEpsilon(String left, Stack<String> leftStack) {
        leftStack.push(left);
        for (int i = 0; i < G.size(); i++) {
            if (G.get(i).getLeft().getSection().equals(left)) {
                if (G.get(i).getRight().getSection().split("[ ]+").length > 1) {
                    continue;
                } else if (G.get(i).getRight().getSection().equals("~")) {
                    return true;
                } else {
                    String right = G.get(i).getRight().getSection();
                    if (Constant.nonTerminals.contains(right)) {
                        if (leftStack.contains(right)) {
                            continue;
                        }
                        if (canBeEpsilon(right, leftStack)) {
                            return true;
                        }
                    }
                }
            }
        }
        leftStack.pop();
        return false;
    }

    public List<RegularExpression> getG() {
        return G;
    }

    public List<LRDFACell> getCells() {
        return cells;
    }
}

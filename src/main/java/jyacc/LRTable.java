package jyacc;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import exception.CursorOutofRangeException;
import exception.LRTableException;
import jinter.ParseTree;

import javax.swing.*;
import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class LRTable {
    private final List<LRDFACell> cells;
    private final List<RegularExpression> G;
    private final Action[][] actions;
    private final Integer[][] ids;

    public LRTable(LRDFA lrdfa) {
        this.cells = lrdfa.getCells();
        this.G = lrdfa.getG();
        actions = new Action[cells.size()][Constant.nonTerminals.size() + Constant.terminals.size()];
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < Constant.nonTerminals.size() + Constant.terminals.size(); j++) {
                actions[i][j] = Action.NONE;
            }
        }

        ids = new Integer[cells.size()][Constant.nonTerminals.size() + Constant.terminals.size()];
        constructTable();
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < Constant.nonTerminals.size() + Constant.terminals.size(); j++) {
                System.out.print(actions[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < Constant.nonTerminals.size() + Constant.terminals.size(); j++) {
                System.out.print(ids[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void constructTable() {
        for (int i = 0; i < cells.size(); i++) {
            LRDFACell lrdfaCell = cells.get(i);
            Map<String, Integer> map = lrdfaCell.getMap();
            Map<LRItem, Integer> reduceMap = new HashMap<>();
            for (LRItem lrItem : lrdfaCell.getClosure()) {
                if (lrItem.getRegularExpression().getRight().reduce()) {
                    reduceMap.put(lrItem, findReduce(lrItem));
                }
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                actions[i][Constant.getPosition(entry.getKey())] = Action.SHIFT;
                ids[i][Constant.getPosition(entry.getKey())] = entry.getValue();
            }

            for (Map.Entry<LRItem, Integer> entry : reduceMap.entrySet()) {
                if (actions[i][Constant.getPosition(entry.getKey().getPrediction())] == Action.SHIFT) {
                    System.out.println("reduce: " + entry.getKey().getRegularExpression().getRight().getReduceString());
                    if (Constant.getPriority(Constant.getPosition(entry.getKey().getRegularExpression().getRight().getReduceString())) >= Constant.getPriority(Constant.getPosition(entry.getKey().getPrediction()))) {
                        actions[i][Constant.getPosition(entry.getKey().getPrediction())] = Action.REDUCE;
                        ids[i][Constant.getPosition(entry.getKey().getPrediction())] = entry.getValue();
                    }
                } else {
                    actions[i][Constant.getPosition(entry.getKey().getPrediction())] = Action.REDUCE;
                    ids[i][Constant.getPosition(entry.getKey().getPrediction())] = entry.getValue();
                }
            }
        }
    }

    private int findReduce(LRItem lrItem) {
        for (int i = 0; i < G.size(); i++) {
            if (lrItem.getRegularExpression().getLeft().equals(G.get(i).getLeft()) && lrItem.getRegularExpression().getRight().getSection().equals(G.get(i).getRight().getSection())) {
                return i;
            }
        }
        return -1;
    }

    public ParseTree parseTrees(String sentences) {
        ParseTree parseTree = new ParseTree(G);
        String[] s = sentences.split("[ ]+");
        int cursor = 0;
        int state = 0;
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> nodes = new Stack<>();
        stack.push(state);
        while (true) {
            state = stack.peek();
            if (actions[state][Constant.getPosition(s[cursor])] == Action.SHIFT) {
                stack.push(ids[state][Constant.getPosition(s[cursor])]);
                cursor++;
                nodes.push(parseTree.addCell(s[cursor]));
                if (cursor > s.length) {
                    throw new CursorOutofRangeException("Current cursor is" + cursor + "\nCurrent state is " + state);
                }
            } else if (actions[state][Constant.getPosition(s[cursor])] == Action.REDUCE) {
                RegularExpression regularExpression = G.get(ids[state][Constant.getPosition(s[cursor])]);
                parseTree.addCell(regularExpression.getLeft().getSection());
                for (int i = regularExpression.getRight().getSize(); i > 0; i--) {
                    int node = nodes.pop();
                    parseTree.addTransition(parseTree.size() - 1, node);
                    stack.pop();
                }
                nodes.push(parseTree.size() - 1);
                parseTree.constructAST(ids[state][Constant.getPosition(s[cursor])]);
                stack.push(ids[stack.peek()][Constant.getPosition(regularExpression.getLeft().getSection())]);
                if (ids[state][Constant.getPosition(s[cursor])] == 0) {
                    break;
                }
            } else {
                throw new LRTableException("Current string is" + s[cursor] + "\nCurrent state is " + state);
            }
        }
        return parseTree;
    }

    public JFrame constructTrees(String sentences) {
        String[] s = sentences.split("[ ]+");
        int cursor = 0;
        int state = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(state);
        final mxGraph graph = new mxGraph();
        Stack<Object> nodes = new Stack<>();
        int width = 80;
        int height = 30;
        int startX = 0;
        int tempY = 0;
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        while (true) {
            state = stack.peek();
            System.out.println(s[cursor]);
            if (actions[state][Constant.getPosition(s[cursor])] == Action.SHIFT) {
                stack.push(ids[state][Constant.getPosition(s[cursor])]);
                startX += 100;
                tempY = 0;
                nodes.push(graph.insertVertex(parent, null, s[cursor], startX, tempY, width, height));
                cursor++;
                if (cursor > s.length) {
                    throw new CursorOutofRangeException("Current cursor is" + cursor + "\nCurrent state is " + state);
                }
            } else if (actions[state][Constant.getPosition(s[cursor])] == Action.REDUCE) {
                RegularExpression regularExpression = G.get(ids[state][Constant.getPosition(s[cursor])]);
                tempY += 100;
                System.out.println(regularExpression);
                Object reduceV = graph.insertVertex(parent, null, regularExpression.getLeft().getSection(), startX, tempY, width, height);
                for (int i = regularExpression.getRight().getSize(); i > 0; i--) {
                    Object V = nodes.pop();
                    stack.pop();
                    graph.insertEdge(parent, null, "", reduceV, V);
                }
                stack.push(ids[stack.peek()][Constant.getPosition(regularExpression.getLeft().getSection())]);
                nodes.push(reduceV);
                if (ids[state][Constant.getPosition(s[cursor])] == 0) {
                    break;
                }
            } else {
                throw new LRTableException("Current string is" + s[cursor] + "\nCurrent state is " + state);
            }
        }
        graph.getModel().endUpdate();
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JFrame jf = new JFrame("Parsing Tree");
        jf.setSize(1200, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.getContentPane().add(graphComponent);
        return jf;
    }


}

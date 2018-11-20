package jlexical;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/28
 * @Todo:
 */
public class NFA {
    public final List<NFACell> cells = new ArrayList<>();
    public final Set<Character> alphabet = new HashSet<>();

    public int addCell() {
        NFACell state = new NFACell();
        cells.add(state);
        return cells.size() - 1;
    }

    public void addTransition(char input, int current, int next) {
        alphabet.add(input);
        cells.get(current).addTransition(input, next);
    }

    public void addEpsilon(int current, int next) {
        cells.get(current).addEpsilon(next);
    }

    public Set<Integer> eclosure(int stateNum) {
        Set<Integer> eclosure = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        eclosure.add(stateNum);
        stack.push(stateNum);
        while (!stack.empty()) {
            int state = stack.pop();
            Set<Integer> etrans = cells.get(state).etrans;
            for (int i : etrans) {
                if (!eclosure.contains(i)) {
                    eclosure.add(i);
                    stack.push(i);
                }
            }
        }
        return eclosure;
    }

    public Set<Integer> eclosure(Set<Integer> states) {
        Set<Integer> eclosures = new HashSet<>();
        for (int state : states) {
            eclosures.addAll(eclosure(state));
        }
        return eclosures;
    }

    public Set<Integer> move(Set<Integer> stateNums, char input) {
        Set<Integer> move = new HashSet<>();
        for (int stateNum : stateNums) {
            Set<Integer> nexts = cells.get(stateNum).strans.get(input);
            if (nexts != null) {
                move.addAll(nexts);
            }
        }
        return move;
    }

    public DFA NFA2DFA() {
        Integer dtran[][] = new Integer[cells.size()][this.alphabet.size()];
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < this.alphabet.size(); j++) {
                dtran[i][j] = -1;
            }
        }
        List<Character> alphabet = new ArrayList<>(this.alphabet);
        List<Set<Integer>> states = new ArrayList<>();
        Set<Integer> e0 = eclosure(0);
        states.add(e0);
        int stateCursor = 0;
        while (stateCursor < states.size()) {
            Set<Integer> notLabeled = states.get(stateCursor);
            int alpahbetCount = -1;
            for (char i : alphabet) {
                alpahbetCount++;
                Set<Integer> u = eclosure(move(notLabeled, i));
                if (!u.isEmpty()) {
                    if (!states.contains(u)) {
                        states.add(u);
                    }
                    dtran[stateCursor][alpahbetCount] = states.indexOf(u);
                }
            }
            stateCursor++;
        }


        return new DFA(states, alphabet, dtran, cells.size() - 1);
    }

    public JFrame toVision() {
        final mxGraph graph = new mxGraph();
        List<Object> objects = new ArrayList<>();
        int width = 80;
        int height = 30;
        int baseX = 50;
        int baseY = 10;
        int increment = 100;

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        for(int i=0;i<cells.size();i++){
            for(int j=0;j<10;j++){
                if(i%10 == j){
                    objects.add(graph.insertVertex(parent, null, i, baseX + 1.5 * j * increment, baseY, width, height));
                    if(j==9){
                        baseY += increment;
                    }
                }
            }
        }
        for(int i=0;i<objects.size();i++){
            NFACell nfaCell = cells.get(i);
            for(Integer node: nfaCell.etrans){
                graph.insertEdge(parent, null, "epsilon", objects.get(i), objects.get(node));
            }
            for (Map.Entry<Character, Set<Integer>> entry : nfaCell.strans.entrySet()) {
                for(Integer node : entry.getValue()) {
                    graph.insertEdge(parent, null, entry.getKey().toString(), objects.get(i), objects.get(node));
                }
            }
        }
        graph.getModel().endUpdate();
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JFrame jf = new JFrame("NFA");
        jf.setSize(1200, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.getContentPane().add(graphComponent);
        return jf;
    }





}
package jlexical;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/4
 * @Todo: the result of DFA optimization
 */
public class DFAo {
    public final List<DFACell> cells = new ArrayList<>();
    public String type;

    public DFAo(String type) {
        this.type = type;
    }

    public void addCell(boolean accept) {
        DFACell state = new DFACell();
        state.accept = accept;
        cells.add(state);
    }

    public void addTransition(char input, int current, int next) {
        cells.get(current).addTransition(input, next);
    }

    public Set<Integer> move(Set<Integer> stateNums, char input) {
        Set<Integer> move = new HashSet<>();
        for(int stateNum : stateNums) {
            Set<Integer> nexts = cells.get(stateNum).strans.get(input);
            if(nexts!=null) {
                move.addAll(nexts);
            }
        }
        return move;
    }

    public boolean search(String regex){
        Set<Integer> start = new HashSet<>();
        start.add(0);
        for(int i = 0;i<regex.length();i++){
            Set<Integer> set = move(start, regex.charAt(i));
            if(set.size()==0){
                return false;
            }else if(i == regex.length() - 1){
                for(int j:set){
                    if(cells.get(j).accept){
                        return true;
                    }
                }
            }
            start = new HashSet<>(set);
        }
        return false;
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
            DFACell nfaCell = cells.get(i);
            for (Map.Entry<Character, Set<Integer>> entry : nfaCell.strans.entrySet()) {
                for(Integer node : entry.getValue()) {
                    graph.insertEdge(parent, null, entry.getKey().toString(), objects.get(i), objects.get(node));
                }
            }
        }
        graph.getModel().endUpdate();
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JFrame jf = new JFrame("DFA");
        jf.setSize(1200, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.getContentPane().add(graphComponent);
        return jf;
    }

    @Override
    public String toString() {
        return "DFAo{" +
                "cells=" + cells +
                ", type='" + type + '\'' +
                '}';
    }
}

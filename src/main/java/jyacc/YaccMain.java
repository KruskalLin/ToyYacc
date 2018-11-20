package jyacc;

import jinter.ParseTree;

import javax.swing.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class YaccMain {
    public static void main(String args[]) {
        // 终结符
        Constant.terminals.add("+");
        Constant.terminals.add("-");
        Constant.terminals.add("#");
        Constant.terminals.add("id");
        Constant.terminals.add("num");
        Constant.terminals.add("(");
        Constant.terminals.add(")");
        Constant.terminals.add("$");
        // 非终结符
        Constant.nonTerminals.add("F");
        Constant.nonTerminals.add("T");
        Constant.nonTerminals.add("E");
        Constant.nonTerminals.add("START");
        // 定义优先级
        for (int i = 0; i < Constant.terminals.size() + Constant.nonTerminals.size(); i++) {
            Constant.priority.add(i);
        }
        // 处理
        LRTable lrTable = new LRTable(new LRDFA(ExpressionHandler.preprocess("E->E + T_E - T_T"+ Constant.newLine + "T->T # F_F" + Constant.newLine + "F->( E )_num_id")));
        JFrame jFrame = lrTable.constructTrees("id + num # ( id - num ) + ( id - num ) # id $");
        jFrame.setVisible(true);
    }
}

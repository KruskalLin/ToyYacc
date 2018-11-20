import jinter.ParseTree;
import jyacc.Constant;
import jyacc.ExpressionHandler;
import jyacc.LRDFA;
import jyacc.LRTable;

import javax.swing.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class JLYI {
    public static void main(String args[]){
        Constant.terminals.add("+");
        Constant.terminals.add("-");
        Constant.terminals.add("#");
        Constant.terminals.add("id");
        Constant.terminals.add("num");
        Constant.terminals.add("(");
        Constant.terminals.add(")");
        Constant.terminals.add("$");
        Constant.nonTerminals.add("F");
        Constant.nonTerminals.add("T");
        Constant.nonTerminals.add("E");
        Constant.nonTerminals.add("START");
        for (int i = 0; i < Constant.terminals.size() + Constant.nonTerminals.size(); i++) {
            Constant.priority.add(i);
        }
        LRTable lrTable = new LRTable(new LRDFA(ExpressionHandler.preprocess("E->E + T_E - T_T"+ Constant.newLine + "T->T # F_F" + Constant.newLine + "F->( E )_num_id")));
        ParseTree parseTree = lrTable.parseTrees("id + num # ( id - num ) + ( id - num ) # id $");
        JFrame jFrame = parseTree.toAST();
        jFrame.setVisible(true);
    }
}

package jinter;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import exception.ASTConstructException;
import jyacc.Constant;
import jyacc.RegularExpression;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class ParseTree {
    private final List<ParseTreeCell> cells = new ArrayList<>();
    private final Map<Integer, List<Integer>> map = new HashMap<>();
    private final List<ActionPair> pairs = new ArrayList<>();
    private final List<RegularExpression> G;

    public int addCell(String c) {
        cells.add(new ParseTreeCell(c));
        return cells.size() - 1;
    }

    public ParseTree(List<RegularExpression> g) {
        this.G = g;
        try {
            File source = new File(ParseTree.class.getResource("/action.i").getFile());
            FileInputStream fis = new FileInputStream(source);
            byte[] y = new byte[Constant.strLen];
            fis.read(y);
            String code = new String(y);
            code = code.replaceAll("\u0000", "");
            String action = getBody("Action", code);
            BufferedReader br = new BufferedReader(new StringReader(action));
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.equals("\r\n") || line.equals("")) {
                    continue;
                } else {
                    line = line.replaceAll(Constant.newLine, "");
                    String[] token = line.split(" ");
                    pairs.add(new ActionPair(ActionType.valueOf(token[0]), token[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void addTransition(int origin, int next) {
        if (map.get(origin) == null) {
            List<Integer> list = new ArrayList<>();
            list.add(next);
            map.put(origin, list);
        } else {
            List<Integer> list = map.get(origin);
            list.add(next);
            map.put(origin, list);
        }
    }

    public void constructAST(int GNum) {
        if (GNum == 0) {
            return;
        }
        RegularExpression regularExpression = G.get(GNum);
        System.out.println(regularExpression);
        ActionPair actionPair = pairs.get(GNum - 1);
        System.out.println(actionPair);
        switch (actionPair.getActionType()) {
            case LEAF:
                cells.get(cells.size() - 1).setASTCell(new Leaf(actionPair.getAction(), regularExpression.getRight().getSection()));
                break;
            case NODE:
                Node node = new Node(actionPair.getAction(), cells.get(map.get(cells.size() - 1).get(2)).getASTCell(), cells.get(map.get(cells.size() - 1).get(0)).getASTCell());
                cells.get(cells.size() - 1).setASTCell(node);
                break;
            case ASSIGN:
                cells.get(cells.size() - 1).setASTCell(cells.get(cells.size() - 2 - Integer.parseInt(actionPair.getAction())).getASTCell());
                break;
            default:
                throw new ASTConstructException("Current number of G is " + GNum);
        }
    }

    public int size() {
        return cells.size();
    }

    public JFrame toAST() {
        final mxGraph graph = new mxGraph();
        int width = 80;
        int height = 30;
        int baseX = 500;
        int baseY = 10;
        int increment = 100;
        int incrementX = 300;
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        ASTCell astCell = cells.get(cells.size() - 2).getASTCell();
        if(astCell instanceof Node){
            Object topV = graph.insertVertex(parent, null, astCell.id, baseX, baseY + increment, width, height);
            insert(astCell, topV, graph, parent, baseX, baseY + increment, width, height, increment, incrementX);
        }else{
            graph.insertVertex(parent, null, astCell.id, baseX, baseY + increment, width, height);
        }
        graph.getModel().endUpdate();
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JFrame jf = new JFrame("NFA");
        jf.setSize(1200, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.getContentPane().add(graphComponent);
        return jf;
    }


    private static String getBody(String tag, String val) {
        String start = "<" + tag + ">";
        String end = "</" + tag + ">";
        int s = val.indexOf(start) + start.length();
        int e = val.indexOf(end);
        return val.substring(s, e);
    }

    private void insert(ASTCell top,Object topV, mxGraph graph, Object parent, int baseX, int baseY, int width, int height, int incrementY, int incrementX){
        if(top instanceof Node){
            Node node = (Node) top;
            Object formerV = graph.insertVertex(parent, null, node.former.id, baseX - incrementX, baseY + incrementY, width, height);
            graph.insertEdge(parent, null, "", topV, formerV);
            insert(node.former, formerV, graph, parent, baseX - incrementX, baseY + incrementY, width, height, incrementY, incrementX /2);
            Object latterV = graph.insertVertex(parent, null, node.latter.id, baseX + incrementX, baseY + incrementY, width, height);
            graph.insertEdge(parent, null, "", topV, latterV);
            insert(node.latter, latterV, graph, parent, baseX + incrementX, baseY + incrementY, width, height, incrementY, incrementX / 2);
        }
    }

}

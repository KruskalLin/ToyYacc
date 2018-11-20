import jyacc.*;

import javax.swing.*;
import java.io.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class LexYaccTest {

    public static String getBody(String tag, String val) {
        String start = "<" + tag + ">";
        String end = "</" + tag + ">";
        int s = val.indexOf(start) + start.length();
        int e = val.indexOf(end);
        return val.substring(s, e);
    }


    public static void main(String args[]) {
        String tokens = "";
        String terminals = "";
        String nonTerminals = "";
        String expression = "";
        LexGenerator lexGenerator = new LexGenerator();
        try {
            tokens = lexGenerator.getTokens(lexGenerator.getDFAos());
            File source = new File(LexYaccTest.class.getResource("/yacc.y").getFile());
            FileInputStream fis = new FileInputStream(source);
            byte[] y = new byte[Constant.strLen];
            fis.read(y);
            String code = new String(y);
            code = code.replaceAll("\u0000", "");
            terminals = getBody("Terminals", code);
            nonTerminals = getBody("NonTerminals", code);
            expression = getBody("Expression", code);


        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new StringReader(terminals));
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("\r\n") || line.equals("")) {
                    continue;
                } else {
                    line = line.replaceAll(Constant.newLine, "");
                    String[] token = line.split(" ");
                    Constant.terminals.add(token[0]);
                    Constant.priority.add(Integer.parseInt(token[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(new StringReader(nonTerminals));
        line = "";
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("\r\n") || line.equals("")) {
                    continue;
                } else {
                    line = line.replaceAll(Constant.newLine, "");
                    Constant.nonTerminals.add(line);
                    Constant.priority.add(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int start = 0;
        int end = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '\r' || expression.charAt(i) == '\n') {
                continue;
            }
            start = i;
            break;
        }
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (expression.charAt(i) == '\r' || expression.charAt(i) == '\n') {
                continue;
            }
            end = i;
            break;
        }
        expression = expression.substring(start, end + 1);
        LRTable lrTable = new LRTable(new LRDFA(ExpressionHandler.preprocess(expression)));
        System.out.println(tokens);
        JFrame jFrame = lrTable.constructTrees(tokens);
        jFrame.setVisible(true);


    }
}

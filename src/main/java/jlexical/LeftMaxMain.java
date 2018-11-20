package jlexical;

import jyacc.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/17
 * @Todo:
 */
public class LeftMaxMain {
    public static void main(String args[]){
        try{
            List<DFAo> list = new ArrayList<>();
            File file = new File(LexMain.class.getResource("/lex.l").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lexs = line.split("[ ]+");
                list.add(Regex2NFA.regex2NFA(lexs[1]).NFA2DFA().DFAOptimize(lexs[0]));
            }
            File source = new File(LexMain.class.getResource("/lex_source.txt").getFile());
            FileInputStream fis = new FileInputStream(source);
            byte[] codes = new byte[Constant.strLen];
            fis.read(codes);
            String code = new String(codes);
            code = code.replaceAll(Constant.newLine, "");  //神坑
            code = code.replaceAll("[ ]+", "^");
            code = code.replaceAll("\u0000", ""); //神坑
            code = code.replace("(", "<%");
            code = code.replace(")", "%>");
            code = code.replace("*", "#");
            System.out.println(code);
            StringBuilder stringBuilder = new StringBuilder("");
            int cursor = 0;
            String lastType = "";
            int lastIndex = 0;
            while(cursor < code.length()) {
                for (int i = cursor + 1; i <= code.length(); i++) {
                    String sub = code.substring(cursor, i);
                    for(int j=0;j<list.size();j++){
                        if(list.get(j).search(sub)){
                            lastType = list.get(j).type;
                            lastIndex = i;
                            break;
                        }
                    }
                }
                if(!lastType.equals("SPACE")) {
                    stringBuilder.append(lastType + " ");
                }
                cursor = lastIndex;
            }
            System.out.println(stringBuilder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

package jyacc;

import com.google.gson.Gson;
import jlexical.DFAo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class LexGenerator {
    public List<DFAo> getDFAos() {
        List<DFAo> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            byte[] dfaoByte = new byte[Constant.strLen];
            FileInputStream fis = new FileInputStream(Object.class.getResource("/dfao.txt").getFile());
            fis.read(dfaoByte);
            String dfaos = new String(dfaoByte);
            String[] dfao = dfaos.split(Constant.newLine);
            for (int i = 0; i < dfao.length - 1; i++) {
                DFAo dfAo = gson.fromJson(dfao[i], DFAo.class);
                list.add(dfAo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getTokens(List<DFAo> list) throws IOException {
        File source = new File(LexGenerator.class.getResource("/lex_source.txt").getFile());
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
        code = code.replace("||", "or");
        code = code.replace("&&", "and");
        System.out.println(code);
        StringBuilder stringBuilder = new StringBuilder("");
        int cursor = 0;
        String lastType = "";
        int lastIndex = 0;
        while (cursor < code.length()) {
            for (int i = cursor + 1; i <= code.length(); i++) {
                String sub = code.substring(cursor, i);
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).search(sub)) {
                        lastType = list.get(j).type;
                        lastIndex = i;
                        break;
                    }
                }
            }
            if (!lastType.equals("SPACE")) {
                stringBuilder.append(lastType + " ");
            }
            cursor = lastIndex;
        }
        stringBuilder.append("$");
        return stringBuilder.toString();
    }

    public static void main(String args[]) {
        LexGenerator lexGenerator = new LexGenerator();
        try {
            System.out.println(lexGenerator.getTokens(lexGenerator.getDFAos()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

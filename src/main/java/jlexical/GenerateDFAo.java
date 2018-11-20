package jlexical;

import com.google.gson.Gson;
import jyacc.Constant;

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
public class GenerateDFAo {
    public static void main(String args[]){
        try {
            Gson gson = new Gson();
            StringBuilder output = new StringBuilder();
            File file = new File(LexMain.class.getResource("/lex.l").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lexs = line.split("[ ]+");
                output.append(gson.toJson(Regex2NFA.regex2NFA(lexs[1]).NFA2DFA().DFAOptimize(lexs[0]), DFAo.class));
                output.append(Constant.newLine);
            }
            System.out.println(Object.class.getResource("/dfao.txt").getFile());
            FileOutputStream fos = new FileOutputStream(Object.class.getResource("/dfao.txt").getFile());
            fos.write(output.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

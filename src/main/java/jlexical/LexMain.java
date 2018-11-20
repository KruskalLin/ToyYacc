package jlexical;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexMain {
    public static void main(String[] args) {
        try{
            List<DFAo> list = new ArrayList<>();
            File file = new File(LexMain.class.getResource("/lex_space.l").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lexs = line.split("[ ]+");
                list.add(Regex2NFA.regex2NFA(lexs[1]).NFA2DFA().DFAOptimize(lexs[0]));
            }
            File source = new File(LexMain.class.getResource("/lex_source_space.txt").getFile());
            br = new BufferedReader(new FileReader(source));
            StringBuilder str = new StringBuilder("");
            while ((line = br.readLine()) != null) {
                String[] codes = line.split("[ ]+");
                for(int j = 0;j<codes.length;j++) {
                    for (int i = 0; i < list.size(); i++) {
                        if(codes[j].equals("")){
                            break;
                        }
                        if(codes[j].equals("(")){
                            codes[j] = "<%";
                        }else if(codes[j].equals(")")){
                            codes[j] = "%>";
                        }else if(codes[j].equals("*")){
                            codes[j] = "#";
                        }
                        if (list.get(i).search(codes[j])){
                            str.append(list.get(i).type + " ");
                            break;
                        }
                        if(i == list.size() - 1){
                            str.append("ERROR ");
                        }
                    }
                }
                str.append("\n");
            }
            System.out.println(str.toString());

        }catch (IOException e){
            e.printStackTrace();
        }




    }
}

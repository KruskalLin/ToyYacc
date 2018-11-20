package jlexical;

import exception.RegexException;

import java.util.Stack;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/28
 * @Todo:  这里的正则表达式符号支持 . * | () []
 */
public class RegexUtil {

    public static String infix2postfix(String infixRegex){
        infixRegex = addDot(transBrackets(infixRegex));
        Stack<Character> operatorStack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        for(int i =0;i<infixRegex.length();i++) {
            Character c = infixRegex.charAt(i);
            // 对于单目运算符直接输出
            if(c == '*' || !isOperator(c)){
                postfix.append(c);
            }else {
                if(c != ')'){
                    // 优先级匹配
                    while(!operatorStack.empty() && priority(c) <= priority(operatorStack.peek()) && operatorStack.peek() != '('){
                        postfix.append(operatorStack.pop());
                    }
                    operatorStack.push(c);
                }else{
                    boolean match = false;
                    while(!operatorStack.empty() && operatorStack.peek() != '('){
                        postfix.append(operatorStack.pop());
                    }
                    if(!operatorStack.empty() && operatorStack.peek() == '('){
                        match = true;
                    }
                    // 括号不匹配直接报错
                    if(!match){
                        error();
                        return "";
                    }else{
                        operatorStack.pop();
                    }
                }
            }
        }
        while(!operatorStack.empty()){
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }


    private static String addDot(String infixRegex){
        int length = infixRegex.length();
        int i = 0;
        StringBuilder dotStr = new StringBuilder();
        while(i < length) {
            dotStr.append(infixRegex.charAt(i));
            if((!isOperator(infixRegex.charAt(i)) && (i + 1 < length)) || ((infixRegex.charAt(i)==')') && (i + 1 < length)) || (infixRegex.charAt(i)=='*') && (i + 1 < length)){
                if(infixRegex.charAt(i+1) == '(' || !isOperator(infixRegex.charAt(i+1))){
                    dotStr.append(".");
                }
            }
            i++;
        }
        return dotStr.toString();
    }

    private static String transBrackets(String regex){
        StringBuilder result = new StringBuilder();
        int start = 0;
        boolean lock = false;
        for(int i=0;i<regex.length();i++){
            Character c = regex.charAt(i);
            if(c=='['){
                result.append("(");
                start = i;
                lock = true;
            }else if(c==']'){
                lock = false;
                for(int j = start + 1;j<i;j++){
                    if(j + 1 < i && regex.charAt(j+1) == '-' && j+2<i && regex.charAt(j+2) > regex.charAt(j)){
                        for(char r = regex.charAt(j) ; r<=regex.charAt(j+2) ; r++){
                            result.append(r);
                            if(r != regex.charAt(j+2)){
                                result.append("|");
                            }
                        }
                        j = j+2;
                        if(j < i - 1){
                            result.append("|");
                        }
                    }else{
                        result.append(regex.charAt(j));
                        if(j < i - 1){
                            result.append("|");
                        }
                    }
                }
                result.append(")");
                continue;
            }
            if(!lock){
                result.append(c);
            }


        }
        return result.toString();

    }


    private static boolean isOperator(Character c) {
        return c.equals('.') || c.equals('(') || c.equals(')') || c.equals('*') || c.equals('|');
    }

    private static int priority(Character c){
        switch (c){
            case '|': return 1;
            case '.': return 2;
            case '(': return 3;
            default: return 0;
        }
    }

    private static void error(){
        throw new RegexException("BracketNotMatch! Please check your regex.");
    }



}

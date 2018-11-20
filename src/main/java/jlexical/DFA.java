package jlexical;

import java.util.*;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/29
 * @Todo:
 */
public class DFA {
    public final List<DFACell> cells;
    public final List<Set<Integer>> states;
    public final List<Character> alphabet;
    Integer[][] dtrans;
    Integer acceptNum;

    public DFA(List<Set<Integer>> states, List<Character> alphabet, Integer[][] dtrans, Integer acceptNum) {
        this.states = states;
        this.alphabet = alphabet;
        this.dtrans = dtrans;
        this.acceptNum = acceptNum;
        this.cells = new ArrayList<>();

        for (Set<Integer> state : states) {
            if (state.contains(acceptNum)) {
                addCell(true);
            } else {
                addCell(false);
            }
        }

        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                if (dtrans[i][j] != -1) {
                    addTransition(alphabet.get(j), i, dtrans[i][j]);
                }
            }
        }


    }

    public DFAo DFAOptimize(String type) {
        Set<Integer> accept = new HashSet<>();
        Set<Integer> nonAccept = new HashSet<>();
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).accept) {
                accept.add(i);
            } else {
                nonAccept.add(i);
            }
        }
        List<Set<Integer>> list = new ArrayList<>();
        if (accept.size() > 0) {
            list.add(accept);
        }
        if (nonAccept.size() > 0) {
            list.add(nonAccept);
        }
        List<Set<Integer>> origin = list;
        List<Set<Integer>> current = move(list);

        while (origin.size() != current.size()) {
            origin = current;
            current = move(current);
        }
        current.sort(new Comparator<Set<Integer>>() {
            @Override
            public int compare(Set<Integer> o1, Set<Integer> o2) {
                List<Integer> l1 = new ArrayList<>(o1);
                List<Integer> l2 = new ArrayList<>(o2);
                int min1 = getMin(l1);
                int min2 = getMin(l2);
                return Integer.compare(min1, min2);
            }
        });

        DFAo dfAo = new DFAo(type);
        for (int i = 0; i < current.size(); i++) {
            Set<Integer> set = current.get(i);
            for (int j : set) {
                if (accept.contains(j)) {
                    dfAo.addCell(true);
                } else {
                    dfAo.addCell(false);
                }
                for (int k = 0; k < alphabet.size(); k++) {
                    for (int l = 0; l < current.size(); l++) {
                        if (current.get(l).contains(dtrans[j][k])) {
                            dfAo.addTransition(alphabet.get(k), i, l);
                            break;
                        }
                    }
                }
                break;
            }
        }
        return dfAo;
    }


    public void toVision() {
        for(int i=0;i<dtrans.length;i++){
            for(int j=0;j<dtrans[i].length;j++){
                System.out.print(dtrans[i][j] + " ");
            }
            System.out.println();
        }
    }


    private List<Set<Integer>> move(List<Set<Integer>> equivalences) {
        List<Set<Integer>> result = listClone(equivalences);
        for (int k = 0; k < alphabet.size(); k++) {
            equivalences = listClone(result);
            result.clear();
            for (int i = 0; i < equivalences.size(); i++) {
                Set<Integer> set = equivalences.get(i);
                Map<Integer, Integer> map = new HashMap<>();
                for (int j : set) {
                    Integer equi = dtrans[j][k];
                    for (int l = 0; l < equivalences.size(); l++) {
                        if (equivalences.get(l).contains(equi)) {
                            map.put(j, l);
                        }
                    }
                    map.putIfAbsent(j, -1);
                }
                List<Integer> values = map.values().stream().distinct().collect(Collectors.toList());
                for (int j : values) {
                    Set<Integer> equi = new HashSet<>();
                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        if (entry.getValue() == j) {
                            equi.add(entry.getKey());
                        }
                    }
                    result.add(equi);
                }
            }
        }
        return result;
    }


    private List<Set<Integer>> listClone(List<Set<Integer>> equivalences) {
        List<Set<Integer>> list = new ArrayList<>();
        list.addAll(equivalences);
        return list;
    }


    private void addCell(boolean accept) {
        DFACell state = new DFACell();
        state.accept = accept;
        cells.add(state);
    }

    private void addTransition(char input, int current, int next) {
        cells.get(current).addTransition(input, next);
    }

    private static int getMin(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).summaryStatistics().getMin();
    }



}

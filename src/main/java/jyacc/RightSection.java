package jyacc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class RightSection {
    private final List<String> section;
    private int reduceCursor = 0;

    public RightSection(String s) {
        section = Arrays.asList(s.split("[ ]+"));
    }

    public boolean shift() {
        if (reduceCursor >= section.size()) {
            return false;
        } else if (section.size() == 1 && section.get(0).equals("~")) {
            return false;
        }
        reduceCursor++;
        return true;
    }

    public boolean reduce() {
        if (reduceCursor == section.size() || section.get(reduceCursor).equals("~")) {
            return true;
        }
        return false;
    }

    public String getCurrentString() {
        if (reduceCursor < section.size() && !section.get(reduceCursor).equals("~")) {
            return section.get(reduceCursor);
        } else {
            return "";
        }
    }

    public int getReduceCursor() {
        return reduceCursor;
    }

    public void setReduceCursor(int reduceCursor) {
        this.reduceCursor = reduceCursor;
    }

    public String getSection() {
        return section.stream().collect(Collectors.joining(" "));
    }

    public int getSize() {
        return section.size();
    }

    public String getReduceString() {
        if (reduceCursor - 2 >= 0) {
            return section.get(reduceCursor - 2);
        }
        return "";
    }


    @Override
    public String toString() {
        return "RightSection{" +
                "section=" + section +
                ", reduceCursor=" + reduceCursor +
                '}';
    }

    public List<String> getCurrentRightString() {
        List<String> right = new ArrayList<>();
        for (int i = reduceCursor + 1; i < section.size(); i++) {
            right.add(section.get(i));
        }
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RightSection that = (RightSection) o;
        return reduceCursor == that.reduceCursor &&
                Objects.equals(getSection(), that.getSection());
    }

    @Override
    public int hashCode() {

        return Objects.hash(section, reduceCursor);
    }
}

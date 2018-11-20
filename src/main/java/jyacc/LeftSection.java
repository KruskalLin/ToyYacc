package jyacc;

import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/12
 * @Todo:
 */
public class LeftSection {
    private String section;

    public LeftSection(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeftSection that = (LeftSection) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {

        return Objects.hash(section);
    }

    @Override
    public String toString() {
        return "LeftSection{" +
                "section=" + section +
                '}';
    }
}

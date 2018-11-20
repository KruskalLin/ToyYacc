package jlexical;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/29
 * @Todo:
 */
public abstract class RegexTreeCell {

    /**
     * @Description: Using recursion to construct NFA graph
     * @author Popping Lim
     * @date 2018/10/29
     */
    public abstract IntegerPair construct(NFA context);
    @Override
    public abstract String toString();
}

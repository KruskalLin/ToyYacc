package jlexical;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/29
 * @Todo:
 */
public abstract class NoTreeCell extends RegexTreeCell {

    @Override
    public abstract IntegerPair construct(NFA context);

    public static class CharCell extends NoTreeCell {
        private Character c;

        public CharCell(Character c) {
            this.c = c;
        }
        @Override
        public IntegerPair construct(NFA context) {
            int start = context.addCell();
            int end = context.addCell();
            context.addTransition(this.c, start, end);
            return new IntegerPair(start, end);
        }

        @Override
        public String toString() {
            return "" + c;
        }
    }

    public static class EpsilonCell extends NoTreeCell {

        public EpsilonCell() {
        }
        @Override
        public IntegerPair construct(NFA context) {
            int start = context.addCell();
            int end = context.addCell();
            context.addEpsilon(start, end);
            return new IntegerPair(start, end);
        }

        @Override
        public String toString() {
            return "~";
        }
    }

}

package jlexical;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/10/29
 * @Todo:
 */
public abstract class SiTreeCell extends RegexTreeCell {

    public RegexTreeCell treeCell;

    public SiTreeCell(RegexTreeCell treeCell) {
        this.treeCell = treeCell;
    }

    @Override
    public abstract IntegerPair construct(NFA context);


    public static class KleeneClosureCell extends SiTreeCell {


        public KleeneClosureCell(RegexTreeCell treeCell) {
            super(treeCell);
        }

        @Override
        public IntegerPair construct(NFA context) {
            int start = context.addCell();
            IntegerPair entry = this.treeCell.construct(context);
            int end = context.addCell();
            context.addEpsilon(start, end);
            context.addEpsilon(entry.getLatter(), entry.getFormer());
            context.addEpsilon(start, entry.getFormer());
            context.addEpsilon(entry.getLatter(), end);
            return new IntegerPair(start, end);
        }

        @Override
        public String toString() {
            return "("+ treeCell.toString() +")*";
        }
    }


}

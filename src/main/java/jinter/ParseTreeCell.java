package jinter;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public class ParseTreeCell {
    private ASTCell ASTCell;
    private String id;

    public ParseTreeCell(String id) {
        this.id = id;
    }

    public ASTCell getASTCell() {
        return ASTCell;
    }

    public void setASTCell(ASTCell ASTCell) {
        this.ASTCell = ASTCell;
    }

    public String getId() {
        return id;
    }
}

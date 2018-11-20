package jinter;

import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/16
 * @Todo:
 */
public abstract class ASTCell {
    protected String id;

    public ASTCell(String id) {
        this.id = id;
    }

    @Override
    public abstract String toString();
}

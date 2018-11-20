package jinter;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/11/17
 * @Todo:
 */
public class ActionPair {
    private ActionType actionType;
    private String action;

    public ActionPair(ActionType actionType, String action) {
        this.actionType = actionType;
        this.action = action;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "ActionPair{" +
                "actionType=" + actionType +
                ", action='" + action + '\'' +
                '}';
    }
}

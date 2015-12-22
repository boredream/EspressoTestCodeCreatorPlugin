package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moyun on 2015/12/22.
 */
public class EspressoAction {

    public static final String TYPE_NONE = "none";
    public static final String TYPE_CLICK = "click";
    public static final String TYPE_DOUBLE_CLICK = "doubleClick";
    public static final String TYPE_LONG_CLICK = "longClick";
    public static final String TYPE_TYPE_TEXT = "typeText";
    public static final String TYPE_PRESS_BACK = "pressBack";

    public static List<String> actions;
    static {
        actions = new ArrayList<>();
        // actions.add(TYPE_NONE);
        actions.add(TYPE_CLICK);
        actions.add(TYPE_DOUBLE_CLICK);
        actions.add(TYPE_LONG_CLICK);
        actions.add(TYPE_TYPE_TEXT);
        actions.add(TYPE_PRESS_BACK);
    }

    public EspressoAction(Element targetElement) {
        this.targetElement = targetElement;

        if (targetElement.name.contains("EditText")) {
            actionName = EspressoAction.TYPE_TYPE_TEXT;
            // TODO auto set
            typeText = "type some text here";
        } else if (targetElement.isClickable()) {
            actionName = EspressoAction.TYPE_CLICK;
        } else {
            actionName = EspressoAction.TYPE_NONE;
        }
    }

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 目标控件信息
     */
    private Element targetElement;

    /**
     * actionName为typeText时可用,为输入内容
     */
    private String typeText;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Element getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(Element targetElement) {
        this.targetElement = targetElement;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }
}

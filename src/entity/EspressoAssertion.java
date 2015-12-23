package entity;

import java.util.ArrayList;
import java.util.List;

public class EspressoAssertion {

    public static final String TYPE_IS_DISPLAYED = "isDisplayed";
    public static final String TYPE_IS_CHECKED = "isChecked";
    public static final String TYPE_IS_SELECTED = "isSelected";
    public static final String TYPE_WITH_TEXT = "withText";

    public static List<String> assertions;
    static {
        assertions = new ArrayList<>();
        assertions.add(TYPE_IS_DISPLAYED);
        assertions.add(TYPE_IS_CHECKED);
        assertions.add(TYPE_IS_SELECTED);
        assertions.add(TYPE_WITH_TEXT);
    }

    public EspressoAssertion(Element targetElement) {
        this.targetElement = targetElement;
        this.assertionName = TYPE_IS_DISPLAYED;
        // TODO auto set with text
    }

    /**
     * 断言反义
     */
    private boolean isNot;

    /**
     * 断言名称
     */
    private String assertionName;

    /**
     * 目标控件信息
     */
    private Element targetElement;

    /**
     * assertionName为withText时可用,为验证文本内容
     */
    private String assertionText;

    public boolean isNot() {
        return isNot;
    }

    public void setNot(boolean not) {
        isNot = not;
    }

    public String getAssertionName() {
        return assertionName;
    }

    public void setAssertionName(String assertionName) {
        this.assertionName = assertionName;
    }

    public Element getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(Element targetElement) {
        this.targetElement = targetElement;
    }

    public String getAssertionText() {
        return assertionText;
    }

    public void setAssertionText(String assertionText) {
        this.assertionText = assertionText;
    }
}

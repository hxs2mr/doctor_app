package microtech.hxswork.com.frame_ui.main.businss;

/**
 * Created by microtech on 2017/12/11.
 */

public class CaseItem {
    String color;
    float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CaseItem(String color, float value) {
        this.color = color;
        this.value = value;
    }
}

package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BodyCreateDuck {

    @JsonProperty("color")
    private String color;

    @JsonProperty("height")
    private double height;

    @JsonProperty("material")
    private String material;

    @JsonProperty("sound")
    private String sound;

    @JsonProperty("wingsState")
    private WingsState wingsState;

    public enum WingsState {
        ACTIVE,
        FIXED,
        UNDEFINED
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public WingsState getWingsState() {
        return wingsState;
    }

    public void setWingsState(WingsState wingsState) {
        this.wingsState = wingsState;
    }
}


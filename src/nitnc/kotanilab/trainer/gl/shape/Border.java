package nitnc.kotanilab.trainer.gl.shape;

import nitnc.kotanilab.trainer.gl.node.Child;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.jogamp.opengl.GL.GL_LINES;

public class Border extends Child {
    private Map<String, BorderSetting> settingMap = new HashMap<>();

    public Map<String, BorderSetting> getSettingMap() {
        return settingMap;
    }

    @Override
    protected void drawingProcess() {
        // 桁落ちして表示がバグる
        double xStart = -1.0 * parent.getPosition().getXScale() + parent.getPosition().getXOffset();
        double yStart = -1.0 * parent.getPosition().getYScale() + parent.getPosition().getYOffset();
        double xEnd = 1.0 * parent.getPosition().getXScale() + parent.getPosition().getXOffset();
        double yEnd = 1.0 * parent.getPosition().getYScale() + parent.getPosition().getYOffset();
        settingMap.keySet().forEach(key -> {
            BorderSetting borderSetting = settingMap.get(key);
            double width = borderSetting.getWidth();
            double xCorrStart = (1.0 - width / Math.floor(getWindowWidth() * parent.getPosition().getXScale())) * parent.getPosition().getXScale() + parent.getPosition().getXOffset();
            double xCorrEnd = -(1.0 - width / Math.floor(getWindowWidth() * parent.getPosition().getXScale())) * parent.getPosition().getXScale() + parent.getPosition().getXOffset();
            double yCorrStart = (1.0 - width / Math.floor(getWindowHeight() * parent.getPosition().getYScale())) * parent.getPosition().getYScale() + parent.getPosition().getYOffset();
            double yCorrEnd = -(1.0 - width / Math.ceil(getWindowHeight() * parent.getPosition().getYScale())) * parent.getPosition().getYScale() + parent.getPosition().getYOffset();
            if (!borderSetting.getStyle().equals("none")) {
                if (borderSetting.getStyle().equals("solid")) {
                    setThickness(width);
                    setMode(GL_LINES);
                }
                super.setColor(borderSetting.getColor());
                switch (key) {
                    case "top":
                        gl.glVertex2d(xStart, yCorrStart);
                        gl.glVertex2d(xEnd, yCorrStart);
                        break;
                    case "left":
                        gl.glVertex2d(xCorrEnd, yStart);
                        gl.glVertex2d(xCorrEnd, yEnd);
                        break;
                    case "bottom":
                        gl.glVertex2d(xStart, yCorrEnd);
                        gl.glVertex2d(xEnd, yCorrEnd);
                        break;
                    case "right":
                        gl.glVertex2d(xCorrStart, yStart);
                        gl.glVertex2d(xCorrStart, yEnd);
                        break;
                }
            }
            end();
        });
    }

    public static class BorderSetting {
        String style;
        double width;
        Color color;

        public BorderSetting(String style, double width, Color color) {
            this.style = style;
            this.width = width;
            this.color = color;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }
}

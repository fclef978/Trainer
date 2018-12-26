package jp.ac.numazu_ct.d14122.gl.pane;

import jp.ac.numazu_ct.d14122.gl.node.Node;
import jp.ac.numazu_ct.d14122.gl.util.Position;

import java.util.ArrayList;
import java.util.List;

public class HEnumPane extends Pane {

    private int count = 0;
    private List<Position> list = new ArrayList<>();

    public HEnumPane() {
    }

    public HEnumPane(String style) {
        super(style);
    }

    @Override
    public void drawingProcess() {
        count = 0;
        list.clear();
        double totalWidth = children.stream().mapToDouble(Node::getWidth).sum();
        final double[] previousWidth = {0};
        children.each(child -> {
            double center = child.getWidth() / 2.0;
            double left = previousWidth[0] + center;
            double right = totalWidth - previousWidth[0] - center;
            Position curr = new Position(getStyle());
            curr.setXOffset(left - right);
            Position position = new Position(parent.getPosition(), curr);
            list.add(position);
            previousWidth[0] += center * 2;
        });
        children.each(Node::draw);

        // this.position = new Position(parent.getPosition(), getStyle());

    }

    @Override
    public Position getPosition() {
        if (count >= list.size()) {
            return position;
        }
        return list.get(count++);
    }
}

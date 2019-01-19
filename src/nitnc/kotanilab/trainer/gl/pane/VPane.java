package nitnc.kotanilab.trainer.gl.pane;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.util.Position;

import java.util.ArrayList;
import java.util.List;

public class VPane extends Pane {

    private int count = 0;
    private List<Position> list = new ArrayList<>();

    public VPane() {
    }

    public VPane(String style) {
        super(style);
    }

    @Override
    public void drawingProcess() {
        count = 0;
        list.clear();
        double totalHeight = children.stream().mapToDouble(Node::getHeight).sum();
        final double[] previousHeight = {0};
        children.each(child -> {
            double center = child.getHeight() / 2.0;
            double bottom = previousHeight[0] + center;
            double top = totalHeight - previousHeight[0] - center;
            Position curr = new Position(getStyle());
            curr.setYOffset(top - bottom);
            Position position = new Position(parent.getPosition(), curr);
            list.add(position);
            previousHeight[0] += center * 2;
        });
        children.each(Node::draw);
    }

    @Override
    public Position getPosition() {
        if (count >= list.size()) {
            return position;
        }
        return list.get(count++);
    }
}

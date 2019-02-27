package nitnc.kotanilab.trainer.gl.pane;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * 単一の水平行に子をレイアウトするPaneです。
 */
public class HPane extends Pane {

    private int count = 0;
    private List<Position> list = new ArrayList<>();

    /**
     * コンストラクタです。
     */
    public HPane() {
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public HPane(String style) {
        super(style);
    }

    @Override
    public void drawingProcess() {
        count = 0;
        list.clear();
        double totalWidth = children.stream().mapToDouble(Node::getWidth).sum();
        final double[] previousWidth = {0};
        children.forEach(child -> {
            double center = child.getWidth() / 2.0;
            double left = previousWidth[0] + center;
            double right = totalWidth - previousWidth[0] - center;
            Position curr = new Position(getStyle());
            curr.setXOffset(left - right);
            curr.scale(parent.getPosition());
            list.add(curr);
            previousWidth[0] += center * 2;
        });
        children.forEach(Node::draw);

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

package nitnc.kotanilab.trainer.gl.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 子ノードの専用のリストです。
 * java.util.Listに似たメソッドをいくつか持ち、同様に扱うことができます。
 * 可能な限りスレッドセーフになるようになっています。
 */
public class Children {

    private final List<Node> nodes = new CopyOnWriteArrayList<>();
    private Parent parent;

    /**
     * コンストラクタです。
     *
     * @param parent このChildrenが所属するParent
     */
    public Children(Parent parent) {
        this.parent = parent;
    }

    /**
     * 指定されたNodeを追加します。
     *
     * @param node 追加するNode
     */
    public void add(Node node) {
        node.setParent(parent);
        nodes.add(node);
    }

    /**
     * 指定された全てのNodeを追加します。
     *
     * @param nodes 追加するNode
     */
    public void addAll(Node... nodes) {
        for (Node node : nodes) {
            this.add(node);
        }
    }

    /**
     * 指定されたコレクション内全てのNodeを追加します。
     *
     * @param nodes 追加するNodeを含むコレクション
     */
    public void addAll(Collection<? extends Node> nodes) {
        for (Node node : nodes) {
            this.add(node);
        }
    }

    /**
     * 指定されたNodeがこのChildrenに存在するかどうかを返します。
     *
     * @param node 判定されるNode
     * @return 指定されたNodeがこのChildrenに存在するかどうか
     */
    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    /**
     * 指定された位置のNodeを指定されたNodeで置き換えます。
     *
     * @param index 指定するインデックス
     * @param node  置き換えるNode
     */
    public void set(int index, Node node) {
        node.setParent(parent);
        nodes.set(index, node);
    }

    /**
     * このChildrenの指定された位置にあるNodeを返します。
     *
     * @param index 指定するインデックス
     * @return 指定された位置にあるNode
     */
    public Node get(int index) {
        return nodes.get(index);
    }

    /**
     * このChildrenの指定された位置にあるNodeを削除します。
     *
     * @param index 削除する位置
     */
    public void remove(int index) {
        synchronized (parent.getLock()) {
            nodes.remove(index).setParent(null);
        }
    }

    /**
     * 指定したNodeすべてをこのChildrenに存在すればその最初のものを削除します。
     *
     * @param nodes 削除するNode
     */
    public void removeAll(Node... nodes) {
        synchronized (parent.getLock()) {
            for (Node node : nodes) {
                if (node != null && this.nodes.remove(node)) {
                    node.setParent(null);
                }
            }
        }
    }

    /**
     * 指定したコレクション内の要素全てをこのChildrenに存在すれば削除します。
     *
     * @param nodes 削除するNodeを含むコレクション
     */
    public void removeAll(Collection<? extends Node> nodes) {
        synchronized (parent.getLock()) {
            for (Node node : nodes) {
                if (node != null && this.nodes.remove(node)) {
                    node.setParent(null);
                }
            }
        }
    }

    /**
     * 全てのNodeを削除します。
     */
    public void clear() {
        nodes.forEach(Node::removeParent);
        nodes.clear();
    }

    /**
     * このChildren内のNodeの数を返します。
     *
     * @return Nodeの数
     */
    public int size() {
        return nodes.size();
    }

    /**
     * このChildren内の全てのNodeに対して指定されたアクションを、すべてのNodeが処理されるか、アクションが例外をスローするまで実行します。
     *
     * @param action 各ノードに対して実行されるアクション
     */
    public void forEach(Consumer<? super Node> action) {
        nodes.forEach(action);
    }

    /**
     * このChildrenをソースとして使用して、逐次的なStreamを返します。
     *
     * @return このChildrenのStream
     */
    public Stream<Node> stream() {
        return nodes.stream();
    }
}

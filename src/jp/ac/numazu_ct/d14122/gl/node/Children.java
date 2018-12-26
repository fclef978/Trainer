package jp.ac.numazu_ct.d14122.gl.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Children {

    private final List<Node> nodes = new CopyOnWriteArrayList<>();
    private Parent parent;

    public Children(Parent parent) {
        this.parent = parent;
    }

    public void add(Node node) {
        node.setParent(parent);
        nodes.add(node);
    }

    public void addAll(Node... nodes) {
        for (Node node : nodes) {
            this.add(node);
        }
    }

    public void addAll(Collection<? extends Node> nodes) {
        for (Node node : nodes) {
            this.add(node);
        }
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    public void set(int index, Node node) {
        node.setParent(parent);
        nodes.set(index, node);
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    public void remove(int index) {
        synchronized (parent.getLock()) {
            nodes.remove(index).setParent(null);
        }
    }

    public void removeAll(Node... nodes) {
        synchronized (parent.getLock()) {
            for (Node node : nodes) {
                if (node != null && this.nodes.remove(node)) {
                    node.setParent(null);
                }
            }
        }
    }

    public void removeAll(Collection<? extends Node> nodes) {
        synchronized (parent.getLock()) {
            for (Node node : nodes) {
                if (node != null && this.nodes.remove(node)) {
                    node.setParent(null);
                }
            }
        }
    }

    public void clear() {
        nodes.forEach(Node::removeParent);
        nodes.clear();
    }

    public int size() {
        return nodes.size();
    }

    public void each(Consumer<? super Node> action) {
    nodes.forEach(action);
    }

    public Stream<Node> stream() {
        return nodes.stream();
    }

}

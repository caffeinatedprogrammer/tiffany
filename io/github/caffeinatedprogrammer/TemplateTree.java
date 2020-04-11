package io.github.caffeinatedprogrammer;

import java.util.*;

public class TemplateTree {
    public static abstract class Node<T> {
        private Map<Node<?>, Node<?>> children = new HashMap<>();
        protected T value;
        public Node(T value) {
            super();
            this.value = value;
        }
        
        public abstract boolean isValueFixed();
        @Override
        public boolean equals(Object o) {
            if (o instanceof Node<?>) {
                Node<?> another = (Node<?>)o;
                if (another.isValueFixed() == this.isValueFixed()) {
                    return false;
                } else {
                    Object anotherValue = another.getValue();
                    T thisValue = this.getValue();
                    if (anotherValue == null && thisValue == null) {
                        return true;
                    } else {
                        return anotherValue.equals(thisValue);
                    }
                }
            } else {
                return false;
            }
        }
        
        @Override
        public int hashCode() {
            return this.getValue().hashCode() * (this.isValueFixed() ? 1 : 2);
        }
        
        Map<Node<?>, Node<?>> getChildren() {
            return this.children;
        }
        
        @Override
        public String toString() {
            return this.getValue().toString();
        }
        
        public T getValue() {
            return this.value;
        }
    }
    
    public static class CommandNode extends Node<String> {
        public CommandNode(String value) {
            super(value);
        }
        
        @Override
        public boolean isValueFixed() {
            return true;
        }
    }
    
    public static class ArgumentNode extends Node<String> {
        public ArgumentNode() {
            super(null);
        }
        
        @Override
        public boolean isValueFixed() {
            return false;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
    }
    
    private Node<?> root = new CommandNode("Tiffany");
    public TemplateTree() {
        super();
    }
    
    static List<String> getPathAsString(Node<?> node) {
        if (node.getChildren().isEmpty()) {
            return Arrays.asList(node.toString());
        }
        List<String> paths = new ArrayList<>();
        for (Map.Entry<Node<?>, Node<?>> entry: node.getChildren().entrySet()) {
            for (String path: getPathAsString(entry.getKey())) {
                paths.add(node + "-" + path);
            }
        }
        return paths;
    }
    
    @Override
    public String toString() {
        List<String> stringList = getPathAsString(this.root);
        String result = "";
        for (String str: stringList) {
            result += str;
            result += "\n";
        }
        return result;
    }
    
    void insertPath(List<Node<?>> children) {
        Node<?> current = this.root;
        for (Node<?> path: children) {
            Node<?> key = current.getChildren().get(path);
            if (key != null) {
                current = key;
            } else {
                current.getChildren().put(path, path);
                current = path;
            }
        }
    }
    
    void insertPathByString(List<String> children) {
        List<Node<?>> nodes = new ArrayList<>();
        for (String child: children) {
            nodes.add(child == null ? new ArgumentNode() : new CommandNode(child));
        }
        insertPath(nodes);
    }
}
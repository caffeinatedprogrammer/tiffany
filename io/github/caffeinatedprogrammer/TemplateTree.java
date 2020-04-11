package io.github.caffeinatedprogrammer;

import java.util.*;

public class TemplateTree {
    public static abstract class Node<T> implements Cloneable{
        private Map<Node<?>, Node<?>> children = new HashMap<>();
        protected T value;
        public Node(T value) {
            super();
            this.value = value;
        }
        
        @Override
        public Node<?> clone() {
            try {
                Node<?> cloned = (Node<?>)(super.clone());
                return cloned;
            } catch (CloneNotSupportedException ex) {
                throw new InternalError();
            }
        }
        
        public abstract boolean isValueFixed();
        @Override
        public boolean equals(Object o) {
            if (o instanceof Node<?>) {
                Node<?> another = (Node<?>)o;
                if (another.isValueFixed() != this.isValueFixed()) {
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
        
        public String getCommand(List<Node<?>> nodes) {
            return null;
        }
        
        @Override
        public int hashCode() {
            T value = this.getValue();
            if (value == null) {
                return 0;
            }
            return value.hashCode() * (this.isValueFixed() ? 1 : 2);
        }
        
        Map<Node<?>, Node<?>> getChildren() {
            return this.children;
        }
        
        @Override
        public String toString() {
            return this.getValue().toString();
        }
        
        public abstract void setValue(T value);
        public T getValue() {
            return this.value;
        }
    }
    
    public static class CommandNode extends Node<String> {
        public CommandNode(String value) {
            super(value);
        }
        
        @Override
        public void setValue(String value) {
            throw new UnsupportedOperationException();
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
    
    List<Node<?>> matchPath(List<String> givenPath) {
        List<Node<?>> result = new ArrayList<>();
        Node<?> current = this.root;
        for (String step: givenPath) {
            Map<Node<?>, Node<?>> children = current.getChildren();
            Node<?> next;
            if ((next = children.get(new CommandNode(step))) != null) {
                result.add(next);
            } else if ((next = children.get(new ArgumentNode())) != null) {
                result.add(next);
            } else {
                return new ArrayList<>();
            }
            current = next;
        }
        return result;
    }
    
    String getCommandFromPath(List<String> givenPath) {
        List<Node<?>> nodes = matchPath(givenPath);
        List<Node<?>> arguments = new ArrayList<>();
        for (int i=0; i<nodes.size(); i++) {
            if (nodes.get(i) instanceof ArgumentNode) {
                ArgumentNode node = (ArgumentNode)nodes.get(i).clone();
                node.setValue(givenPath.get(i));
                arguments.add(node);
            }
        }
        return nodes.get(nodes.size() - 1).getCommand(arguments);
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
    
    void insertPathByString(List<String> children, CommandGenerator generator) {
        List<Node<?>> nodes = new ArrayList<>();
        for (String child: children.subList(0, children.size() - 1)) {
            nodes.add(child == null ? new ArgumentNode() : new CommandNode(child));
        }
        String lastChildren = children.get(children.size() - 1);
        if (lastChildren != null) {
            nodes.add(new CommandNode(lastChildren) {
                @Override
                public String getCommand(List<Node<?>> args) {
                    return generator.getCommand(args);
                }
            });
        } else {
            nodes.add(new ArgumentNode() {
                @Override
                public String getCommand(List<Node<?>> args) {
                    return generator.getCommand(args);
                }
            });
        }
        insertPath(nodes);
    }
}
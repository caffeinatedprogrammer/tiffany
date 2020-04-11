package io.github.caffeinatedprogrammer;

import java.util.*;

public class Tiffany {
    public static void main(String[] args) {
        Tiffany tiffany = new Tiffany();
        System.out.println(tiffany.tree.getCommandFromPath(Arrays.asList(args)));
    }
    
    private TemplateTree tree = new TemplateTree();
    Tiffany() {
        super();
        
        this.tree.insertPathByString(Arrays.asList("hello", "world"), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "echo hello world!";
            }
        });
        this.tree.insertPathByString(Arrays.asList(null, "print"), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "echo " + nodes.get(0).getValue().toString();
            }
        });
    }
}
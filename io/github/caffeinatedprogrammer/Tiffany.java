package io.github.caffeinatedprogrammer;

import java.util.*;

public class Tiffany {
    public static void main(String[] args) {
        Tiffany tiffany = new Tiffany();
        System.out.println(tiffany.tree.toString());
    }
    
    private TemplateTree tree = new TemplateTree();
    Tiffany() {
        super();
        // TODO: Insert correct command
        this.tree.insertPathByString(Arrays.asList("hello", "world"));
        this.tree.insertPathByString(Arrays.asList("hello", "tiffany"));
        this.tree.insertPathByString(Arrays.asList("a", "tiffany", "e"));
        this.tree.insertPathByString(Arrays.asList("a", "tiffany", "f"));
        this.tree.insertPathByString(Arrays.asList("c", "tiffany"));
    }
}
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
        this.tree.insertPathByString(Arrays.asList("dummy"), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "git add -A && git commit -m \"dummy\"";
            }
        });
        this.tree.insertPathByString(Arrays.asList("store"), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "git add -A && git commit --amend --no-edit";
            }
        });
        this.tree.insertPathByString(Arrays.asList("rebase", null), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "git rebase --interactive HEAD~" + nodes.get(0);
            }
        });
        this.tree.insertPathByString(Arrays.asList("eslint"), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "eslint $(git diff HEAD~1 --name-only --relative) --fix";
            }
        });
        this.tree.insertPathByString(Arrays.asList("good", "morning"), new CommandGenerator() {
            @Override
            public String getCommand(List<TemplateTree.Node<?>> nodes) {
                return "echo Good morning!";
            }
        });
    }
}
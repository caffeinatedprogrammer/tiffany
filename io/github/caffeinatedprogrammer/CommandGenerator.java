package io.github.caffeinatedprogrammer;

import java.util.List;

public interface CommandGenerator {
    public abstract String getCommand(List<TemplateTree.Node<?>> args);
}
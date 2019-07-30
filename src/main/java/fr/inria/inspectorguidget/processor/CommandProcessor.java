package fr.inria.inspectorguidget.processor;

import org.jetbrains.annotations.NotNull;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;

import java.util.Set;

public class CommandProcessor {

    private final @NotNull Set<CtInvocation<?>> binders;
    private final @NotNull Set<CtClass> setClass;

    public CommandProcessor(@NotNull Set<CtInvocation<?>> binders, @NotNull Set<CtClass> setClass) {
        this.binders = binders;
        this.setClass = setClass;
    }


}

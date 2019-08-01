package fr.inria.inspectorguidget.processor;


import org.jetbrains.annotations.NotNull;
import org.malai.command.CommandImpl;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

import java.util.HashSet;
import java.util.Set;

public class CommandProcessor extends AbstractProcessor<CtClass> {

    private final @NotNull Set<CtClass> commandClass;
    private int nb = 0;

    public CommandProcessor() {
        commandClass = new HashSet<>();
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate){
        return candidate.isSubtypeOf(getFactory().Type().createReference(CommandImpl.class));
    }

    @Override
    public void process(CtClass clazz){
        nb++;
        System.out.println(nb);
        System.out.println(clazz.getSimpleName());
        commandClass.add(clazz);
    }

    public Set<CtClass> getCommandClass() {
        return commandClass;
    }
}

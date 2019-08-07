package fr.inria.inspectorguidget.processor;

import io.github.interacto.command.Command;
import org.jetbrains.annotations.NotNull;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

import java.util.HashSet;
import java.util.Set;

public class CommandProcessor extends AbstractProcessor<CtClass> {

    private final @NotNull Set<CtClass> commandClass;

    public CommandProcessor() {
        commandClass = new HashSet<>();
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate){
        return candidate.isSubtypeOf(getFactory().Type().createReference(Command.class));
    }

    @Override
    public void process(CtClass clazz){
        commandClass.add(clazz);
    }

    public Set<CtClass> getCommandClass() {
        return commandClass;
    }
}

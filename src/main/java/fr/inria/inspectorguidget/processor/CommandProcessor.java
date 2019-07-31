package fr.inria.inspectorguidget.processor;

import org.jetbrains.annotations.NotNull;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

import java.util.HashSet;
import java.util.Set;

public class CommandProcessor extends AbstractProcessor<CtClass> {

    private final @NotNull Set<CtClass> commandClass;

    public CommandProcessor() {
        commandClass = new HashSet<>();
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate){

        CtTypeReference extendClass = candidate.getSuperclass();

        CtType superClass;

        while(extendClass != null && extendClass.getSimpleName().compareTo("CommandImpl")!=0){
            superClass = extendClass.getTypeDeclaration();

            if(superClass != null) {
                extendClass = superClass.getSuperclass();
            } else
                extendClass = null;
        }

        return(extendClass != null);
    }

    @Override
    public void process(CtClass clazz){
        System.out.println(clazz.getSimpleName());
        commandClass.add(clazz);
    }

    public Set<CtClass> getCommandClass() {
        return commandClass;
    }
}

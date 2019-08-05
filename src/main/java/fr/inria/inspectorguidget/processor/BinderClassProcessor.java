package fr.inria.inspectorguidget.processor;

import org.jetbrains.annotations.NotNull;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

import java.util.HashSet;
import java.util.Set;

public class BinderClassProcessor extends AbstractProcessor<CtClass> {

    private final @NotNull Set<CtClass> setClass;

    public BinderClassProcessor() {
        this.setClass = new HashSet<>();
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate){

        CtTypeReference extend = candidate.getSuperclass();
        if(extend != null && extend.getSimpleName().compareTo("JfXWidgetBinding") == 0){
            return true;
        }

        return false;

    }

    @Override
    public void process(CtClass clazz){ setClass.add(clazz); }

    public Set<CtClass> getListClass() {
        return setClass;
    }
}

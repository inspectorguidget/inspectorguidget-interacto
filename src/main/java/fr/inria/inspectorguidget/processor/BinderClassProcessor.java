package fr.inria.inspectorguidget.processor;

import org.jetbrains.annotations.NotNull;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinderClassProcessor extends AbstractProcessor<CtClass> {

    private final @NotNull Set<CtClass> listClass;

    public BinderClassProcessor() {
        this.listClass = new HashSet<>();
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
    public void process(CtClass clazz){
        listClass.add(clazz);
    }

    public Set<CtClass> getListClass() {
        return listClass;
    }
}

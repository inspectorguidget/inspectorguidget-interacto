package fr.inria.inspectorguidget.processor;

import org.jetbrains.annotations.NotNull;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.AbstractFilter;

import java.util.HashSet;
import java.util.Set;

public class BinderInvocationProcessor extends AbstractProcessor<CtInvocation> {

    private final @NotNull Set<CtInvocation<?>> binders;

    public BinderInvocationProcessor() {
        binders = new HashSet<>();
    }

    @Override
    public boolean isToBeProcessed(final CtInvocation candidate){

        //check if the invocation finish with bind()
        String nameInvoc = candidate.toString();
        if(!nameInvoc.endsWith("bind()"))
            return false;

        //check if invocation is in configureBindings
        CtMethod meth = candidate.getParent(new AbstractFilter<CtMethod>() {
            @Override
            public boolean matches(final CtMethod element) {
                return true;
            }
        });

        // or if invocation is in method called in configure bindings
        try {
            if (meth.getSimpleName().compareTo("configureBindings") != 0) {
                CtExecutableReference ref = meth.getReference(); // ref de la méthode,

                //get configurebindings method
                CtMethod cbMethod = meth.getParent(new AbstractFilter<CtClass>() {
                    @Override
                    public boolean matches(final CtClass elt) {
                        return true;
                    }
                }).getElements(new AbstractFilter<CtMethod>() {
                    @Override
                    public boolean matches(final CtMethod elt) {
                        return (elt.getSimpleName().compareTo("configureBindings") == 0);
                    }
                }).get(0);

                //try to get invocation with the ref
                CtInvocation cbInvocation = cbMethod.getElements(new AbstractFilter<CtInvocation>() {
                    @Override
                    public boolean matches(final CtInvocation invoc) {
                        return invoc.toString().compareTo(ref.toString()) == 0;
                    }
                }).get(0);
            }
        } catch (Exception e){
            //if exception because method or invocation doesn't exist not processed
            return false;
        }
        return true;
    }

    @Override
    public void process(final CtInvocation invoc) {
        binders.add(invoc);
    }

    public Set<CtInvocation<?>> getNodeBinders() {
        return binders;
    }
}

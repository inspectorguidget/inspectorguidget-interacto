package fr.inria.inspectorguidget.extractor;

import io.github.interacto.jfx.instrument.JfxInstrument;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InteractionExtractor {

    private final Logger logr = Logger.getLogger(InteractionExtractor.class.getName());

    public InteractionExtractor(){}

    public CtElement extractInteraction(CtInvocation invocation){

        // get access to invocation nodeBinder() or friend
        List<CtElement> children = invocation.getDirectChildren();
        CtInvocation invoc = null;

        while(children.get(0) instanceof CtInvocation){
            invoc = (CtInvocation) children.get(0);
            children = invoc.getDirectChildren();
        }

        CtElement interaction;

        if(invoc.toString().startsWith("nodeBinder"))
            interaction = extractInteractionFromNodeBinder(invoc);
        else
            interaction = extractInteractionfromBinder(invoc);

        return interaction;
    }

    public CtElement extractInteraction(CtClass clazz){
        CtTypeReference interaction = extractArguments(clazz.getReference(), "JfXWidgetBinding", 1);
        return interaction;
    }

    public CtElement extractInteractionFromNodeBinder(CtInvocation invocation){

        List<CtElement> args = new ArrayList<>();
        for (CtElement child : invocation.getDirectChildren()){
            if(child.getRoleInParent().equals(CtRole.ARGUMENT)){
                args.add(child);
            }
        }

        CtElement interaction = args.get(0); //first arg is interaction
        return interaction;
    }

    public CtElement extractInteractionfromBinder(CtInvocation invocation){

        String invocationName = invocation.getExecutable().getSimpleName();
        CtClass ctClass = invocation.getParent(new AbstractFilter<CtClass>() {
            @Override
            public boolean matches(CtClass element) {
                return true;
            }
        });

        CtTypeReference superClass = ctClass.getSuperclass();
        while (superClass.getSimpleName().compareTo("JfxInstrument") != 0) {
            superClass = superClass.getSuperclass();
        }
        CtExecutableReference method = null;
        for (CtExecutableReference<?> execRef : superClass.getAllExecutables()) {
            if(execRef.getSimpleName().compareTo(invocationName) == 0)
                method = execRef;
        }

        if(method == null){
            return null;
        }

        CtType binder = method.getType().getTypeDeclaration();
        CtTypeReference interaction = extractArguments(binder.getReference(), "Binder", 2);
        return interaction;
    }

    private CtTypeReference extractArguments(CtTypeReference refClass, String className, int position){

        while (refClass != null && refClass.getSimpleName().compareTo(className) != 0) {
            refClass = refClass.getSuperclass();
        }
        return refClass.getActualTypeArguments().get(position);
    }


}

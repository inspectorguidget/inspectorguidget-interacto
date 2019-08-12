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

    public void extractInteraction(CtInvocation invocation){

        // get access to invocation nodeBinder() or friend
        List<CtElement> children = invocation.getDirectChildren();
        CtInvocation invoc = null;

        while(children.get(0) instanceof CtInvocation){
            invoc = (CtInvocation) children.get(0);
            children = invoc.getDirectChildren();
        }

        if(invoc.toString().startsWith("nodeBinder"))
            extractInteractionFromNodeBinder(invoc);
        else
            extractInteractionfromBinder(invoc);

    }

    public void extractInteraction(CtClass clazz){
        CtTypeReference superClass  = clazz.getSuperclass();
        CtTypeReference interaction = superClass.getActualTypeArguments().get(1); // the second arg is the interaction
        System.out.println(interaction);
    }

    public void extractInteractionFromNodeBinder(CtInvocation invocation){

        List<CtElement> args = new ArrayList<>();
        for (CtElement child : invocation.getDirectChildren()){
            if(child.getRoleInParent().equals(CtRole.ARGUMENT)){
                args.add(child);
            }
        }

        CtElement interaction = args.get(0); //first arg is interaction
        System.out.println("Inter : " + interaction);
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
        CtTypeReference binderClass  = binder.getSuperclass();
        while (binderClass != null && binderClass.getSimpleName().compareTo("Binder") != 0) {
            binderClass = binderClass.getSuperclass();
        }
        CtTypeReference interaction = binderClass.getActualTypeArguments().get(2); // the third arg is the interaction
        System.out.println(interaction);

        return interaction;
    }


}

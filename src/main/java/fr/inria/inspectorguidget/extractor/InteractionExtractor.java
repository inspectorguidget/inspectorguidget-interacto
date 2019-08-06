package fr.inria.inspectorguidget.extractor;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.path.CtRole;

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
            System.out.println("not a nodeBinder, not done for the moment");

    }

    public void extractInteraction(CtClass clazz){
        System.out.println("Inter : not done for the moment");
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

}

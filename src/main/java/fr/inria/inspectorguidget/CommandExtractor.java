package fr.inria.inspectorguidget;


import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLambda;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommandExtractor {

    private final Logger logr = Logger.getLogger(CommandExtractor.class.getName());
    private Set<CtClass> commandClass;

    public CommandExtractor(Set<CtClass> commandClass){
        this.commandClass = commandClass;
    }

    public void extractCommand(CtInvocation invocation){

        CtInvocation invoc = invocation.getElements(new AbstractFilter<CtInvocation>() {
            @Override
            public boolean matches(final CtInvocation invo) {
                System.out.println(invo.getShortRepresentation());
                return true;
            }}).get(0);

        // afficher les arguments de invoc
        /*List<CtTypeReference> args = invocation.getActualTypeArguments();
        for(CtTypeReference arg : args){
            System.out.println(arg);
        }*/
        System.out.println("---------------");

        /* pb ici: on peut cacher la fonction dans une variable et faire simplement un CtVariableRead : nodeBinder(new Press(), creation) */
       /* List<CtConstructorCall> commands;
        try {

            commands = invocation.getElements(new AbstractFilter<CtConstructorCall>() {
                @Override
                public boolean matches(final CtConstructorCall constructorCall) {

                    Set<CtTypeReference<?>> typeReferences = constructorCall.getExecutable().getReferencedTypes();

                    for(CtTypeReference<?> typeRef :typeReferences){

                        boolean isIn = isInCommand(typeRef.getSimpleName());
                        System.out.println(typeRef.getSimpleName() + " : " + isIn);
                    }
                    return false;
                }
            });

            for(CtConstructorCall command : commands) {
                System.out.println(command);
            }

        } catch (Exception e){
            logr.log(Level.WARNING, "Problem identifying command of widget" );
        }
*/
        //récupère classe
        // 1 - on l'affiche
        // 2 - on créer commande et on la renvoie?
        //System.out.println(invocation.toString());
    }

    private boolean isInCommand(String className){

        for(CtClass myClass: commandClass){
            if(myClass.getSimpleName().compareTo(className) == 0)
                return true;
        }
        return false;
    }

}

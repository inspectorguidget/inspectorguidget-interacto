package fr.inria.inspectorguidget;

import fr.inria.inspectorguidget.extractor.CommandExtractor;
import fr.inria.inspectorguidget.extractor.InteractionExtractor;
import fr.inria.inspectorguidget.processor.*;
import io.github.interacto.jfx.instrument.JfxInstrument;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;

public class Main {

    public static void main(String[] args) {

        SpoonAPI spoon = new Launcher();

        if (args.length != 1)
            return;

        spoon.addInputResource(args[0]);

        BinderInvocationProcessor binderInvocationProcessor = new BinderInvocationProcessor();
        BinderClassProcessor binderClassProcessor = new BinderClassProcessor();
        CommandProcessor commandProcessor = new CommandProcessor();

        spoon.addProcessor(binderInvocationProcessor);
        spoon.addProcessor(binderClassProcessor);
        spoon.addProcessor(commandProcessor);

        spoon.run();

        CommandExtractor commandExtractor = new CommandExtractor();
        InteractionExtractor interactionExtractor = new InteractionExtractor();

        //extractCommand & interaction of a binder interaction
        CtElement interaction, command;
        for(CtInvocation invocation: binderInvocationProcessor.getNodeBinders()){
            interaction = interactionExtractor.extractInteraction(invocation);
            command = commandExtractor.extractCommand(invocation);
            affiche(command, interaction);
        }

        //extractCommand, widget & interaction of a binder class
        for(CtClass clazz : binderClassProcessor.getListClass()){
            interaction = interactionExtractor.extractInteraction(clazz);
            command = commandExtractor.extractCommand(clazz);
            affiche(command, interaction);
        }

        return;
    }

    private static void affiche(CtElement command, CtElement interaction){
        System.out.println("Command : " + command);
        System.out.println("Interaction : " + interaction);
        System.out.println("------------------------------");
    }
}

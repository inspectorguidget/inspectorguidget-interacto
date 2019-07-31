package fr.inria.inspectorguidget;

import fr.inria.inspectorguidget.processor.*;
import spoon.Launcher;
import spoon.SpoonAPI;


public class Main {

//    private final static String PATH_TO_SOURCES = "/home/<user>/workspace/latexdraw-4.0-beta2/src/main/java/net/sf/latexdraw";
//  /home/lrichoux/Documents/inspectorguidget/Resources/latexdraw-4.0-beta2/src/main/java/net/sf/latexdraw/instrument
    public static void main(String[] args) {

        SpoonAPI spoon = new Launcher();

        if (args.length != 1)
            return;

        spoon.addInputResource(args[0]);

        BinderInvocationProcessor binderInvocationProcessor = new BinderInvocationProcessor();
        BinderClassProcessor binderClassProcessor = new BinderClassProcessor();
        CommandProcessor commandProcessor = new CommandProcessor();

        //spoon.addProcessor(binderInvocationProcessor);
        //spoon.addProcessor(binderClassProcessor);
        spoon.addProcessor(commandProcessor);

        spoon.run();

        return;
    }
}

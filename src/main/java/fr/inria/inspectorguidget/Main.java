package fr.inria.inspectorguidget;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;

public class Main {

//    private final static String PATH_TO_SOURCES = "/home/<user>/workspace/latexdraw-4.0-beta2/src/main/java/net/sf/latexdraw";

    public static void main(String[] args) {

        if (args.length != 1)
            return;

        CtModel model = load(args[0]);

        // list all packages of the model
        for(CtPackage p : model.getAllPackages()) {
            System.out.println("package: "+p.getQualifiedName());
        }
        // list all classes of the model
        for(CtType<?> s : model.getAllTypes()) {
            System.out.println("class: "+s.getQualifiedName());
        }

        return;
    }

    public static CtModel load(String pathToSource){
        Launcher launcher = new Launcher();
        launcher.addInputResource(pathToSource);
        launcher.buildModel();
        return launcher.getModel();
    }


}

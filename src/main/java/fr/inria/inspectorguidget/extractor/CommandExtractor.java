/*
 * This file is part of InspectorGuidget.
 * InspectorGuidget is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * InspectorGuidget is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with InspectorGuidget.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.inria.inspectorguidget.extractor;

import io.github.interacto.command.Command;

import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.support.reflect.code.CtVariableReadImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandExtractor {

    private final Logger logr = Logger.getLogger(CommandExtractor.class.getName());

    public CommandExtractor(){}

    public CtElement extractCommand(CtInvocation invocation){

        CtElement command = null; // command to be returned

        // get access to invocation nodeBinder()
        List<CtElement> children = invocation.getDirectChildren();
        CtInvocation invoc = null;

        while(children.get(0) instanceof CtInvocation){
            invoc = (CtInvocation) children.get(0);
            children = invoc.getDirectChildren();
        }

        // Warning if the binder is anonCmdBinder, must extract the whole block in lambda
        assert invoc != null;
        if(invoc.getExecutable().getSimpleName().compareTo("anonCmdBinder") == 0){
            command = extractAnonCmd(invoc);
        }
        else {
            // get Arguments of binder:
            List<CtElement> args = new ArrayList<>();
            for (CtElement child : children){
                if(child.getRoleInParent().equals(CtRole.ARGUMENT)){
                    args.add(child);
                }
            }

            //get supplier of command (2 arg in nodeBinder, first in others)
            CtElement supplier = args.size()==2? args.get(1): args.get(0);

            // CtLabmda or CtFieldRead (variable) or CtExecutableReferenceExpression
            if(supplier instanceof CtLambda)
                command = extractCommandLambda((CtLambda) supplier);
            else if (supplier instanceof CtVariableReadImpl)
                command = extractCommandVariable((CtVariableReadImpl) supplier);
            else if(supplier instanceof  CtExecutableReferenceExpression)
                command = extractCommandExecutable((CtExecutableReferenceExpression) supplier);
            else{
                logr.log(Level.WARNING, "not able to identify command");
            }
        }

        return command;
    }

    private CtElement extractCommandExecutable(CtExecutableReferenceExpression expression){
        CtElement command = null;

        boolean isCommand = false;
        for(CtTypeReference<?> typeRef : expression.getReferencedTypes())
            if (isACommand(typeRef)) {
                isCommand = true;
                break;
            }

        if(isCommand)
            command = expression;
        else
            logr.log(Level.WARNING, "can't find command in expression");
        return command;
    }

    private CtElement extractCommandLambda(CtLambda lambda){

        CtElement command = null;
        List<CtConstructorCall> commands;
        try {
            commands = lambda.getElements(new AbstractFilter<CtConstructorCall>() {
                @Override
                public boolean matches(final CtConstructorCall constructorCall) {
                    Set<CtTypeReference<?>> typeReferences = constructorCall.getReferencedTypes();
                    for(CtTypeReference<?> typeRef :typeReferences){
                        if(isACommand(typeRef))
                            return true;
                    }
                    return false;

                }
            });

            command = commands.get(0);

        } catch (Exception e) {
            logr.log(Level.WARNING, "Cannot find command in lambda");
        }

        return command;
    }

    private CtElement extractCommandVariable(CtVariableReadImpl variable){
        CtMethod method = variable.getParent(CtMethod.class);
        CtElement command = null;
        CtLambda lambda;
        try {
            lambda = method.getElements(new AbstractFilter<CtLambda>() {
                @Override
                public boolean matches(final CtLambda elt) {
                    CtLocalVariable varDef = elt.getParent(CtLocalVariable.class);
                    if(varDef == null)
                        return false;

                    return varDef.getSimpleName().compareTo(variable.toString()) == 0;
                }
            }).get(0);
            command = extractCommandLambda(lambda);
        } catch (Exception e){
            logr.log(Level.WARNING,"Impossible to identify command");
        }

        return command;
    }

    public CtElement extractCommand(CtClass clazz){

        CtElement myCommand = null;
        //get all lambda within constructor
        List<CtLambda> lambdaList = clazz.getElements(new AbstractFilter<CtLambda>() {
            @Override
            public boolean matches(CtLambda element) {
                boolean construct = false;
                CtConstructor constructor = element.getParent(CtConstructor.class);

                if(constructor != null)
                    construct =  true;

                //check if lambda return a command
                boolean command = false;

                for(CtTypeReference<?> typeReference: element.getReferencedTypes()) {
                    if (isACommand(typeReference)){
                        command = true;
                        break;
                    }
                }
                return construct && command ;
            }
        });

        if(lambdaList.size() == 1)
            myCommand = extractCommandLambda(lambdaList.get(0));
        else
            logr.log(Level.WARNING, "unable to identify command in class");

        return myCommand;
    }

    private CtElement extractAnonCmd(CtInvocation invocation){

        List<CtLambda> lambdaList = invocation.getElements(new AbstractFilter<CtLambda>() {
            @Override
            public boolean matches(CtLambda element) {
                return true;
            }
        });

        CtLambda lambda = lambdaList.get(0);
        CtBlock command = null;

        List<CtElement> children = lambda.getDirectChildren();
        for(CtElement child: children){
            if (child instanceof CtBlock){
                command = (CtBlock) child;
                break;
            }
        }
        return command;
    }

    private boolean isACommand(CtTypeReference typeReference){
        return typeReference.isSubtypeOf(typeReference.getFactory().Type().createReference(Command.class));
    }
}
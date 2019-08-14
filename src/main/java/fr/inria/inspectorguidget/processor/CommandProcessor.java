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

package fr.inria.inspectorguidget.processor;

import io.github.interacto.command.Command;
import org.jetbrains.annotations.NotNull;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

import java.util.HashSet;
import java.util.Set;

public class CommandProcessor extends AbstractProcessor<CtClass> {

    private final @NotNull Set<CtClass> commandClass;

    public CommandProcessor() {
        commandClass = new HashSet<>();
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate){
        return candidate.isSubtypeOf(getFactory().Type().createReference(Command.class));
    }

    @Override
    public void process(CtClass clazz){
        commandClass.add(clazz);
    }

    public Set<CtClass> getCommandClass() {
        return commandClass;
    }
}

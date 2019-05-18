/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.aesh.console.command.invocation;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class AeshCommandInvocationProvider implements CommandInvocationProvider<CommandInvocation> {
    @Override
    public CommandInvocation enhanceCommandInvocation(CommandInvocation commandInvocation) {
        return commandInvocation;
    }
}

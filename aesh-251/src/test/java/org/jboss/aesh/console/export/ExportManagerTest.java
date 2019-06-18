/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.aesh.console.export;

import org.jboss.aesh.console.Config;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class ExportManagerTest {


    @Test
    public void testVariableNotExist() {
        ExportManager exportManager =
            new ExportManager(new File(Config.getTmpDir()+Config.getPathSeparator()+"aesh_variable_test"));
        assertEquals("", exportManager.getValue("$FOO3"));
        assertEquals("", exportManager.getValue("FOO3"));
    }
}

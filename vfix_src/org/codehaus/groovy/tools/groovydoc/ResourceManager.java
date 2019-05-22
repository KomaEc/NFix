package org.codehaus.groovy.tools.groovydoc;

import java.io.IOException;
import java.io.Reader;

public interface ResourceManager {
   Reader getReader(String var1) throws IOException;
}

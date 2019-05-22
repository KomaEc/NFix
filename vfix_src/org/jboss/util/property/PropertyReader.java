package org.jboss.util.property;

import java.io.IOException;
import java.util.Map;

public interface PropertyReader {
   Map readProperties() throws PropertyException, IOException;
}

package org.jboss.util.stream;

import java.io.IOException;

public interface IMarshalledValue {
   Object get() throws IOException, ClassNotFoundException;
}

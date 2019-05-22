package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;

public interface Tokenizer {
   String getToken(Reader var1) throws IOException;

   String getPostToken();
}

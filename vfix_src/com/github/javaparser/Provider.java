package com.github.javaparser;

import java.io.IOException;

public interface Provider {
   int read(char[] buffer, int offset, int len) throws IOException;

   void close() throws IOException;
}

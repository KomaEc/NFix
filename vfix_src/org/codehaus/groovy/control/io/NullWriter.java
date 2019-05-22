package org.codehaus.groovy.control.io;

import java.io.Writer;

public class NullWriter extends Writer {
   public static final NullWriter DEFAULT = new NullWriter();

   public void close() {
   }

   public void flush() {
   }

   public void write(char[] cbuf, int off, int len) {
   }
}

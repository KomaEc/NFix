package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import java.io.PrintWriter;

public interface WriterFactory {
   PrintWriter create();

   void close();
}

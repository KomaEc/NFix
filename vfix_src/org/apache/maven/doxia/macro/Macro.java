package org.apache.maven.doxia.macro;

import org.apache.maven.doxia.sink.Sink;

public interface Macro {
   String ROLE = Macro.class.getName();

   void execute(Sink var1, MacroRequest var2) throws MacroExecutionException;
}

package org.apache.maven.doxia.parser;

import java.io.File;
import org.apache.maven.doxia.macro.Macro;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.macro.manager.MacroManager;
import org.apache.maven.doxia.macro.manager.MacroNotFoundException;
import org.apache.maven.doxia.sink.Sink;

public abstract class AbstractParser implements Parser {
   protected boolean secondParsing = false;
   protected MacroManager macroManager;

   public int getType() {
      return 0;
   }

   public void executeMacro(String macroId, MacroRequest request, Sink sink) throws MacroExecutionException, MacroNotFoundException {
      Macro macro = this.macroManager.getMacro(macroId);
      macro.execute(sink, request);
   }

   protected File getBasedir() {
      String basedir = System.getProperty("basedir");
      return basedir != null ? new File(basedir) : new File((new File("")).getAbsolutePath());
   }

   public void setSecondParsing(boolean second) {
      this.secondParsing = second;
   }
}

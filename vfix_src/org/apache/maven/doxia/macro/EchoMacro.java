package org.apache.maven.doxia.macro;

import java.util.Iterator;
import org.apache.maven.doxia.sink.Sink;

public class EchoMacro extends AbstractMacro {
   private static final String EOL = System.getProperty("line.separator");

   public void execute(Sink sink, MacroRequest request) {
      sink.verbatim(true);
      sink.text("echo" + EOL);
      Iterator i = request.getParameters().keySet().iterator();

      while(i.hasNext()) {
         String key = (String)i.next();
         sink.text(key + " ---> " + request.getParameter(key) + EOL);
      }

      sink.verbatim_();
   }
}

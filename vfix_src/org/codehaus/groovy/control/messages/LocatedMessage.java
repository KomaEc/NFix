package org.codehaus.groovy.control.messages;

import java.io.PrintWriter;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.CSTNode;

public class LocatedMessage extends SimpleMessage {
   protected CSTNode context;

   public LocatedMessage(String message, CSTNode context, SourceUnit source) {
      super(message, source);
      this.context = context;
   }

   public LocatedMessage(String message, Object data, CSTNode context, SourceUnit source) {
      super(message, data, source);
      this.context = context;
   }

   public void write(PrintWriter writer, Janitor janitor) {
      if (this.owner instanceof SourceUnit) {
         SourceUnit source = (SourceUnit)this.owner;
         String name = source.getName();
         int line = this.context.getStartLine();
         int column = this.context.getStartColumn();
         String sample = source.getSample(line, column, janitor);
         if (sample != null) {
            writer.println(source.getSample(line, column, janitor));
         }

         writer.println(name + ": " + line + ": " + this.message);
         writer.println("");
      } else {
         writer.println("<No Relevant Source>: " + this.message);
         writer.println("");
      }

   }
}

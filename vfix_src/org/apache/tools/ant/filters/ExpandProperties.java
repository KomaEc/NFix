package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.Project;

public final class ExpandProperties extends BaseFilterReader implements ChainableReader {
   private String queuedData = null;

   public ExpandProperties() {
   }

   public ExpandProperties(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      int ch = true;
      if (this.queuedData != null && this.queuedData.length() == 0) {
         this.queuedData = null;
      }

      int ch;
      if (this.queuedData != null) {
         ch = this.queuedData.charAt(0);
         this.queuedData = this.queuedData.substring(1);
         if (this.queuedData.length() == 0) {
            this.queuedData = null;
         }
      } else {
         this.queuedData = this.readFully();
         if (this.queuedData != null) {
            Project project = this.getProject();
            this.queuedData = project.replaceProperties(this.queuedData);
            return this.read();
         }

         ch = -1;
      }

      return ch;
   }

   public Reader chain(Reader rdr) {
      ExpandProperties newFilter = new ExpandProperties(rdr);
      newFilter.setProject(this.getProject());
      return newFilter;
   }
}

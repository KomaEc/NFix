package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;

public final class LineContains extends BaseParamFilterReader implements ChainableReader {
   private static final String CONTAINS_KEY = "contains";
   private static final String NEGATE_KEY = "negate";
   private Vector contains = new Vector();
   private String line = null;
   private boolean negate = false;

   public LineContains() {
   }

   public LineContains(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      int ch = -1;
      if (this.line != null) {
         ch = this.line.charAt(0);
         if (this.line.length() == 1) {
            this.line = null;
         } else {
            this.line = this.line.substring(1);
         }
      } else {
         int containsSize = this.contains.size();

         for(this.line = this.readLine(); this.line != null; this.line = this.readLine()) {
            boolean matches = true;

            for(int i = 0; matches && i < containsSize; ++i) {
               String containsStr = (String)this.contains.elementAt(i);
               matches = this.line.indexOf(containsStr) >= 0;
            }

            if (matches ^ this.isNegated()) {
               break;
            }
         }

         if (this.line != null) {
            return this.read();
         }
      }

      return ch;
   }

   public void addConfiguredContains(LineContains.Contains contains) {
      this.contains.addElement(contains.getValue());
   }

   public void setNegate(boolean b) {
      this.negate = b;
   }

   public boolean isNegated() {
      return this.negate;
   }

   private void setContains(Vector contains) {
      this.contains = contains;
   }

   private Vector getContains() {
      return this.contains;
   }

   public Reader chain(Reader rdr) {
      LineContains newFilter = new LineContains(rdr);
      newFilter.setContains(this.getContains());
      newFilter.setNegate(this.isNegated());
      return newFilter;
   }

   private void initialize() {
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if ("contains".equals(params[i].getType())) {
               this.contains.addElement(params[i].getValue());
            } else if ("negate".equals(params[i].getType())) {
               this.setNegate(Project.toBoolean(params[i].getValue()));
            }
         }
      }

   }

   public static class Contains {
      private String value;

      public final void setValue(String contains) {
         this.value = contains;
      }

      public final String getValue() {
         return this.value;
      }
   }
}

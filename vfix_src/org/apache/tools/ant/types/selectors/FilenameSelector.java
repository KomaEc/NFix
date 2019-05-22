package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;

public class FilenameSelector extends BaseExtendSelector {
   private String pattern = null;
   private boolean casesensitive = true;
   private boolean negated = false;
   public static final String NAME_KEY = "name";
   public static final String CASE_KEY = "casesensitive";
   public static final String NEGATE_KEY = "negate";

   public String toString() {
      StringBuffer buf = new StringBuffer("{filenameselector name: ");
      buf.append(this.pattern);
      buf.append(" negate: ");
      if (this.negated) {
         buf.append("true");
      } else {
         buf.append("false");
      }

      buf.append(" casesensitive: ");
      if (this.casesensitive) {
         buf.append("true");
      } else {
         buf.append("false");
      }

      buf.append("}");
      return buf.toString();
   }

   public void setName(String pattern) {
      pattern = pattern.replace('/', File.separatorChar).replace('\\', File.separatorChar);
      if (pattern.endsWith(File.separator)) {
         pattern = pattern + "**";
      }

      this.pattern = pattern;
   }

   public void setCasesensitive(boolean casesensitive) {
      this.casesensitive = casesensitive;
   }

   public void setNegate(boolean negated) {
      this.negated = negated;
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("name".equalsIgnoreCase(paramname)) {
               this.setName(parameters[i].getValue());
            } else if ("casesensitive".equalsIgnoreCase(paramname)) {
               this.setCasesensitive(Project.toBoolean(parameters[i].getValue()));
            } else if ("negate".equalsIgnoreCase(paramname)) {
               this.setNegate(Project.toBoolean(parameters[i].getValue()));
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.pattern == null) {
         this.setError("The name attribute is required");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      return SelectorUtils.matchPath(this.pattern, filename, this.casesensitive) == !this.negated;
   }
}

package org.apache.tools.ant.types.selectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.FileUtils;

public class ContainsSelector extends BaseExtendSelector implements ResourceSelector {
   private String contains = null;
   private boolean casesensitive = true;
   private boolean ignorewhitespace = false;
   public static final String EXPRESSION_KEY = "expression";
   public static final String CONTAINS_KEY = "text";
   public static final String CASE_KEY = "casesensitive";
   public static final String WHITESPACE_KEY = "ignorewhitespace";

   public String toString() {
      StringBuffer buf = new StringBuffer("{containsselector text: ");
      buf.append('"').append(this.contains).append('"');
      buf.append(" casesensitive: ");
      buf.append(this.casesensitive ? "true" : "false");
      buf.append(" ignorewhitespace: ");
      buf.append(this.ignorewhitespace ? "true" : "false");
      buf.append("}");
      return buf.toString();
   }

   public void setText(String contains) {
      this.contains = contains;
   }

   public void setCasesensitive(boolean casesensitive) {
      this.casesensitive = casesensitive;
   }

   public void setIgnorewhitespace(boolean ignorewhitespace) {
      this.ignorewhitespace = ignorewhitespace;
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("text".equalsIgnoreCase(paramname)) {
               this.setText(parameters[i].getValue());
            } else if ("casesensitive".equalsIgnoreCase(paramname)) {
               this.setCasesensitive(Project.toBoolean(parameters[i].getValue()));
            } else if ("ignorewhitespace".equalsIgnoreCase(paramname)) {
               this.setIgnorewhitespace(Project.toBoolean(parameters[i].getValue()));
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.contains == null) {
         this.setError("The text attribute is required");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      return this.isSelected(new FileResource(file));
   }

   public boolean isSelected(Resource r) {
      this.validate();
      if (r.isDirectory()) {
         return true;
      } else {
         String userstr = this.contains;
         if (!this.casesensitive) {
            userstr = this.contains.toLowerCase();
         }

         if (this.ignorewhitespace) {
            userstr = SelectorUtils.removeWhitespace(userstr);
         }

         BufferedReader in = null;

         try {
            in = new BufferedReader(new InputStreamReader(r.getInputStream()));
         } catch (Exception var10) {
            throw new BuildException("Could not get InputStream from " + r.toLongString(), var10);
         }

         try {
            boolean var5;
            for(String teststr = in.readLine(); teststr != null; teststr = in.readLine()) {
               if (!this.casesensitive) {
                  teststr = teststr.toLowerCase();
               }

               if (this.ignorewhitespace) {
                  teststr = SelectorUtils.removeWhitespace(teststr);
               }

               if (teststr.indexOf(userstr) > -1) {
                  var5 = true;
                  return var5;
               }
            }

            var5 = false;
            return var5;
         } catch (IOException var11) {
            throw new BuildException("Could not read " + r.toLongString());
         } finally {
            FileUtils.close((Reader)in);
         }
      }
   }
}

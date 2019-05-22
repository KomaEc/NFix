package org.apache.tools.ant.types.selectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.regexp.Regexp;

public class ContainsRegexpSelector extends BaseExtendSelector implements ResourceSelector {
   private String userProvidedExpression = null;
   private RegularExpression myRegExp = null;
   private Regexp myExpression = null;
   public static final String EXPRESSION_KEY = "expression";

   public String toString() {
      StringBuffer buf = new StringBuffer("{containsregexpselector expression: ");
      buf.append(this.userProvidedExpression);
      buf.append("}");
      return buf.toString();
   }

   public void setExpression(String theexpression) {
      this.userProvidedExpression = theexpression;
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("expression".equalsIgnoreCase(paramname)) {
               this.setExpression(parameters[i].getValue());
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.userProvidedExpression == null) {
         this.setError("The expression attribute is required");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      return this.isSelected(new FileResource(file));
   }

   public boolean isSelected(Resource r) {
      String teststr = null;
      BufferedReader in = null;
      this.validate();
      if (r.isDirectory()) {
         return true;
      } else {
         if (this.myRegExp == null) {
            this.myRegExp = new RegularExpression();
            this.myRegExp.setPattern(this.userProvidedExpression);
            this.myExpression = this.myRegExp.getRegexp(this.getProject());
         }

         try {
            in = new BufferedReader(new InputStreamReader(r.getInputStream()));
         } catch (Exception var15) {
            throw new BuildException("Could not get InputStream from " + r.toLongString(), var15);
         }

         boolean var4;
         try {
            for(teststr = in.readLine(); teststr != null; teststr = in.readLine()) {
               if (this.myExpression.matches(teststr)) {
                  var4 = true;
                  return var4;
               }
            }

            var4 = false;
         } catch (IOException var16) {
            throw new BuildException("Could not read " + r.toLongString());
         } finally {
            if (in != null) {
               try {
                  in.close();
               } catch (Exception var14) {
                  throw new BuildException("Could not close " + r.toLongString());
               }
            }

         }

         return var4;
      }
   }
}

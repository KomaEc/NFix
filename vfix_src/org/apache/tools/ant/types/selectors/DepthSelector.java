package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;

public class DepthSelector extends BaseExtendSelector {
   public int min = -1;
   public int max = -1;
   public static final String MIN_KEY = "min";
   public static final String MAX_KEY = "max";

   public String toString() {
      StringBuffer buf = new StringBuffer("{depthselector min: ");
      buf.append(this.min);
      buf.append(" max: ");
      buf.append(this.max);
      buf.append("}");
      return buf.toString();
   }

   public void setMin(int min) {
      this.min = min;
   }

   public void setMax(int max) {
      this.max = max;
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("min".equalsIgnoreCase(paramname)) {
               try {
                  this.setMin(Integer.parseInt(parameters[i].getValue()));
               } catch (NumberFormatException var6) {
                  this.setError("Invalid minimum value " + parameters[i].getValue());
               }
            } else if ("max".equalsIgnoreCase(paramname)) {
               try {
                  this.setMax(Integer.parseInt(parameters[i].getValue()));
               } catch (NumberFormatException var5) {
                  this.setError("Invalid maximum value " + parameters[i].getValue());
               }
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.min < 0 && this.max < 0) {
         this.setError("You must set at least one of the min or the max levels.");
      }

      if (this.max < this.min && this.max > -1) {
         this.setError("The maximum depth is lower than the minimum.");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      int depth = -1;
      String absBase = basedir.getAbsolutePath();
      String absFile = file.getAbsolutePath();
      StringTokenizer tokBase = new StringTokenizer(absBase, File.separator);
      StringTokenizer tokFile = new StringTokenizer(absFile, File.separator);

      while(tokFile.hasMoreTokens()) {
         String filetoken = tokFile.nextToken();
         if (tokBase.hasMoreTokens()) {
            String basetoken = tokBase.nextToken();
            if (!basetoken.equals(filetoken)) {
               throw new BuildException("File " + filename + " does not appear within " + absBase + "directory");
            }
         } else {
            ++depth;
            if (this.max > -1 && depth > this.max) {
               return false;
            }
         }
      }

      if (tokBase.hasMoreTokens()) {
         throw new BuildException("File " + filename + " is outside of " + absBase + "directory tree");
      } else if (this.min > -1 && depth < this.min) {
         return false;
      } else {
         return true;
      }
   }
}

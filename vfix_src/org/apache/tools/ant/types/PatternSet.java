package org.apache.tools.ant.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class PatternSet extends DataType implements Cloneable {
   private Vector includeList = new Vector();
   private Vector excludeList = new Vector();
   private Vector includesFileList = new Vector();
   private Vector excludesFileList = new Vector();

   public void setRefid(Reference r) throws BuildException {
      if (this.includeList.isEmpty() && this.excludeList.isEmpty()) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public void addConfiguredPatternset(PatternSet p) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         String[] nestedIncludes = p.getIncludePatterns(this.getProject());
         String[] nestedExcludes = p.getExcludePatterns(this.getProject());
         int i;
         if (nestedIncludes != null) {
            for(i = 0; i < nestedIncludes.length; ++i) {
               this.createInclude().setName(nestedIncludes[i]);
            }
         }

         if (nestedExcludes != null) {
            for(i = 0; i < nestedExcludes.length; ++i) {
               this.createExclude().setName(nestedExcludes[i]);
            }
         }

      }
   }

   public PatternSet.NameEntry createInclude() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         return this.addPatternToList(this.includeList);
      }
   }

   public PatternSet.NameEntry createIncludesFile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         return this.addPatternToList(this.includesFileList);
      }
   }

   public PatternSet.NameEntry createExclude() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         return this.addPatternToList(this.excludeList);
      }
   }

   public PatternSet.NameEntry createExcludesFile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         return this.addPatternToList(this.excludesFileList);
      }
   }

   public void setIncludes(String includes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (includes != null && includes.length() > 0) {
            StringTokenizer tok = new StringTokenizer(includes, ", ", false);

            while(tok.hasMoreTokens()) {
               this.createInclude().setName(tok.nextToken());
            }
         }

      }
   }

   public void setExcludes(String excludes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (excludes != null && excludes.length() > 0) {
            StringTokenizer tok = new StringTokenizer(excludes, ", ", false);

            while(tok.hasMoreTokens()) {
               this.createExclude().setName(tok.nextToken());
            }
         }

      }
   }

   private PatternSet.NameEntry addPatternToList(Vector list) {
      PatternSet.NameEntry result = new PatternSet.NameEntry();
      list.addElement(result);
      return result;
   }

   public void setIncludesfile(File includesFile) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createIncludesFile().setName(includesFile.getAbsolutePath());
      }
   }

   public void setExcludesfile(File excludesFile) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createExcludesFile().setName(excludesFile.getAbsolutePath());
      }
   }

   private void readPatterns(File patternfile, Vector patternlist, Project p) throws BuildException {
      BufferedReader patternReader = null;

      try {
         patternReader = new BufferedReader(new FileReader(patternfile));

         for(String line = patternReader.readLine(); line != null; line = patternReader.readLine()) {
            if (line.length() > 0) {
               line = p.replaceProperties(line);
               this.addPatternToList(patternlist).setName(line);
            }
         }
      } catch (IOException var14) {
         String msg = "An error occurred while reading from pattern file: " + patternfile;
         throw new BuildException(msg, var14);
      } finally {
         if (null != patternReader) {
            try {
               patternReader.close();
            } catch (IOException var13) {
            }
         }

      }

   }

   public void append(PatternSet other, Project p) {
      if (this.isReference()) {
         throw new BuildException("Cannot append to a reference");
      } else {
         String[] incl = other.getIncludePatterns(p);
         if (incl != null) {
            for(int i = 0; i < incl.length; ++i) {
               this.createInclude().setName(incl[i]);
            }
         }

         String[] excl = other.getExcludePatterns(p);
         if (excl != null) {
            for(int i = 0; i < excl.length; ++i) {
               this.createExclude().setName(excl[i]);
            }
         }

      }
   }

   public String[] getIncludePatterns(Project p) {
      if (this.isReference()) {
         return this.getRef(p).getIncludePatterns(p);
      } else {
         this.readFiles(p);
         return this.makeArray(this.includeList, p);
      }
   }

   public String[] getExcludePatterns(Project p) {
      if (this.isReference()) {
         return this.getRef(p).getExcludePatterns(p);
      } else {
         this.readFiles(p);
         return this.makeArray(this.excludeList, p);
      }
   }

   public boolean hasPatterns(Project p) {
      if (this.isReference()) {
         return this.getRef(p).hasPatterns(p);
      } else {
         return this.includesFileList.size() > 0 || this.excludesFileList.size() > 0 || this.includeList.size() > 0 || this.excludeList.size() > 0;
      }
   }

   private PatternSet getRef(Project p) {
      return (PatternSet)this.getCheckedRef(p);
   }

   private String[] makeArray(Vector list, Project p) {
      if (list.size() == 0) {
         return null;
      } else {
         Vector tmpNames = new Vector();
         Enumeration e = list.elements();

         while(e.hasMoreElements()) {
            PatternSet.NameEntry ne = (PatternSet.NameEntry)e.nextElement();
            String pattern = ne.evalName(p);
            if (pattern != null && pattern.length() > 0) {
               tmpNames.addElement(pattern);
            }
         }

         String[] result = new String[tmpNames.size()];
         tmpNames.copyInto(result);
         return result;
      }
   }

   private void readFiles(Project p) {
      Enumeration e;
      PatternSet.NameEntry ne;
      String fileName;
      File exclFile;
      if (this.includesFileList.size() > 0) {
         e = this.includesFileList.elements();

         while(e.hasMoreElements()) {
            ne = (PatternSet.NameEntry)e.nextElement();
            fileName = ne.evalName(p);
            if (fileName != null) {
               exclFile = p.resolveFile(fileName);
               if (!exclFile.exists()) {
                  throw new BuildException("Includesfile " + exclFile.getAbsolutePath() + " not found.");
               }

               this.readPatterns(exclFile, this.includeList, p);
            }
         }

         this.includesFileList.removeAllElements();
      }

      if (this.excludesFileList.size() > 0) {
         e = this.excludesFileList.elements();

         while(e.hasMoreElements()) {
            ne = (PatternSet.NameEntry)e.nextElement();
            fileName = ne.evalName(p);
            if (fileName != null) {
               exclFile = p.resolveFile(fileName);
               if (!exclFile.exists()) {
                  throw new BuildException("Excludesfile " + exclFile.getAbsolutePath() + " not found.");
               }

               this.readPatterns(exclFile, this.excludeList, p);
            }
         }

         this.excludesFileList.removeAllElements();
      }

   }

   public String toString() {
      return "patternSet{ includes: " + this.includeList + " excludes: " + this.excludeList + " }";
   }

   public Object clone() {
      try {
         PatternSet ps = (PatternSet)super.clone();
         ps.includeList = (Vector)this.includeList.clone();
         ps.excludeList = (Vector)this.excludeList.clone();
         ps.includesFileList = (Vector)this.includesFileList.clone();
         ps.excludesFileList = (Vector)this.excludesFileList.clone();
         return ps;
      } catch (CloneNotSupportedException var2) {
         throw new BuildException(var2);
      }
   }

   public class NameEntry {
      private String name;
      private String ifCond;
      private String unlessCond;

      public void setName(String name) {
         this.name = name;
      }

      public void setIf(String cond) {
         this.ifCond = cond;
      }

      public void setUnless(String cond) {
         this.unlessCond = cond;
      }

      public String getName() {
         return this.name;
      }

      public String evalName(Project p) {
         return this.valid(p) ? this.name : null;
      }

      private boolean valid(Project p) {
         if (this.ifCond != null && p.getProperty(this.ifCond) == null) {
            return false;
         } else {
            return this.unlessCond == null || p.getProperty(this.unlessCond) == null;
         }
      }

      public String toString() {
         StringBuffer buf = new StringBuffer();
         if (this.name == null) {
            buf.append("noname");
         } else {
            buf.append(this.name);
         }

         if (this.ifCond != null || this.unlessCond != null) {
            buf.append(":");
            String connector = "";
            if (this.ifCond != null) {
               buf.append("if->");
               buf.append(this.ifCond);
               connector = ";";
            }

            if (this.unlessCond != null) {
               buf.append(connector);
               buf.append("unless->");
               buf.append(this.unlessCond);
            }
         }

         return buf.toString();
      }
   }
}

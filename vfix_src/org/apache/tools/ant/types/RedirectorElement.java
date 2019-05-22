package org.apache.tools.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Redirector;

public class RedirectorElement extends DataType {
   private boolean usingInput = false;
   private boolean usingOutput = false;
   private boolean usingError = false;
   private Boolean logError;
   private String outputProperty;
   private String errorProperty;
   private String inputString;
   private Boolean append;
   private Boolean alwaysLog;
   private Boolean createEmptyFiles;
   private Mapper inputMapper;
   private Mapper outputMapper;
   private Mapper errorMapper;
   private Vector inputFilterChains = new Vector();
   private Vector outputFilterChains = new Vector();
   private Vector errorFilterChains = new Vector();
   private String outputEncoding;
   private String errorEncoding;
   private String inputEncoding;
   private Boolean logInputString;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$util$MergingMapper;

   public void addConfiguredInputMapper(Mapper inputMapper) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (this.inputMapper != null) {
         if (this.usingInput) {
            throw new BuildException("attribute \"input\" cannot coexist with a nested <inputmapper>");
         } else {
            throw new BuildException("Cannot have > 1 <inputmapper>");
         }
      } else {
         this.inputMapper = inputMapper;
      }
   }

   public void addConfiguredOutputMapper(Mapper outputMapper) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (this.outputMapper != null) {
         if (this.usingOutput) {
            throw new BuildException("attribute \"output\" cannot coexist with a nested <outputmapper>");
         } else {
            throw new BuildException("Cannot have > 1 <outputmapper>");
         }
      } else {
         this.outputMapper = outputMapper;
      }
   }

   public void addConfiguredErrorMapper(Mapper errorMapper) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (this.errorMapper != null) {
         if (this.usingError) {
            throw new BuildException("attribute \"error\" cannot coexist with a nested <errormapper>");
         } else {
            throw new BuildException("Cannot have > 1 <errormapper>");
         }
      } else {
         this.errorMapper = errorMapper;
      }
   }

   public void setRefid(Reference r) throws BuildException {
      if (!this.usingInput && !this.usingOutput && !this.usingError && this.inputString == null && this.logError == null && this.append == null && this.createEmptyFiles == null && this.inputEncoding == null && this.outputEncoding == null && this.errorEncoding == null && this.outputProperty == null && this.errorProperty == null && this.logInputString == null) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public void setInput(File input) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else if (this.inputString != null) {
         throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
      } else {
         this.usingInput = true;
         this.inputMapper = this.createMergeMapper(input);
      }
   }

   public void setInputString(String inputString) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else if (this.usingInput) {
         throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
      } else {
         this.inputString = inputString;
      }
   }

   public void setLogInputString(boolean logInputString) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.logInputString = logInputString ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void setOutput(File out) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else if (out == null) {
         throw new IllegalArgumentException("output file specified as null");
      } else {
         this.usingOutput = true;
         this.outputMapper = this.createMergeMapper(out);
      }
   }

   public void setOutputEncoding(String outputEncoding) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.outputEncoding = outputEncoding;
      }
   }

   public void setErrorEncoding(String errorEncoding) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.errorEncoding = errorEncoding;
      }
   }

   public void setInputEncoding(String inputEncoding) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.inputEncoding = inputEncoding;
      }
   }

   public void setLogError(boolean logError) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.logError = logError ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void setError(File error) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else if (error == null) {
         throw new IllegalArgumentException("error file specified as null");
      } else {
         this.usingError = true;
         this.errorMapper = this.createMergeMapper(error);
      }
   }

   public void setOutputProperty(String outputProperty) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.outputProperty = outputProperty;
      }
   }

   public void setAppend(boolean append) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.append = append ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void setAlwaysLog(boolean alwaysLog) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.alwaysLog = alwaysLog ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void setCreateEmptyFiles(boolean createEmptyFiles) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createEmptyFiles = createEmptyFiles ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void setErrorProperty(String errorProperty) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.errorProperty = errorProperty;
      }
   }

   public FilterChain createInputFilterChain() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         FilterChain result = new FilterChain();
         result.setProject(this.getProject());
         this.inputFilterChains.add(result);
         return result;
      }
   }

   public FilterChain createOutputFilterChain() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         FilterChain result = new FilterChain();
         result.setProject(this.getProject());
         this.outputFilterChains.add(result);
         return result;
      }
   }

   public FilterChain createErrorFilterChain() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         FilterChain result = new FilterChain();
         result.setProject(this.getProject());
         this.errorFilterChains.add(result);
         return result;
      }
   }

   public void configure(Redirector redirector) {
      this.configure(redirector, (String)null);
   }

   public void configure(Redirector redirector, String sourcefile) {
      if (this.isReference()) {
         this.getRef().configure(redirector, sourcefile);
      } else {
         if (this.alwaysLog != null) {
            redirector.setAlwaysLog(this.alwaysLog);
         }

         if (this.logError != null) {
            redirector.setLogError(this.logError);
         }

         if (this.append != null) {
            redirector.setAppend(this.append);
         }

         if (this.createEmptyFiles != null) {
            redirector.setCreateEmptyFiles(this.createEmptyFiles);
         }

         if (this.outputProperty != null) {
            redirector.setOutputProperty(this.outputProperty);
         }

         if (this.errorProperty != null) {
            redirector.setErrorProperty(this.errorProperty);
         }

         if (this.inputString != null) {
            redirector.setInputString(this.inputString);
         }

         if (this.logInputString != null) {
            redirector.setLogInputString(this.logInputString);
         }

         String[] errorTargets;
         if (this.inputMapper != null) {
            errorTargets = null;

            try {
               errorTargets = this.inputMapper.getImplementation().mapFileName(sourcefile);
            } catch (NullPointerException var7) {
               if (sourcefile != null) {
                  throw var7;
               }
            }

            if (errorTargets != null && errorTargets.length > 0) {
               redirector.setInput(this.toFileArray(errorTargets));
            }
         }

         if (this.outputMapper != null) {
            errorTargets = null;

            try {
               errorTargets = this.outputMapper.getImplementation().mapFileName(sourcefile);
            } catch (NullPointerException var6) {
               if (sourcefile != null) {
                  throw var6;
               }
            }

            if (errorTargets != null && errorTargets.length > 0) {
               redirector.setOutput(this.toFileArray(errorTargets));
            }
         }

         if (this.errorMapper != null) {
            errorTargets = null;

            try {
               errorTargets = this.errorMapper.getImplementation().mapFileName(sourcefile);
            } catch (NullPointerException var5) {
               if (sourcefile != null) {
                  throw var5;
               }
            }

            if (errorTargets != null && errorTargets.length > 0) {
               redirector.setError(this.toFileArray(errorTargets));
            }
         }

         if (this.inputFilterChains.size() > 0) {
            redirector.setInputFilterChains(this.inputFilterChains);
         }

         if (this.outputFilterChains.size() > 0) {
            redirector.setOutputFilterChains(this.outputFilterChains);
         }

         if (this.errorFilterChains.size() > 0) {
            redirector.setErrorFilterChains(this.errorFilterChains);
         }

         if (this.inputEncoding != null) {
            redirector.setInputEncoding(this.inputEncoding);
         }

         if (this.outputEncoding != null) {
            redirector.setOutputEncoding(this.outputEncoding);
         }

         if (this.errorEncoding != null) {
            redirector.setErrorEncoding(this.errorEncoding);
         }

      }
   }

   protected Mapper createMergeMapper(File destfile) {
      Mapper result = new Mapper(this.getProject());
      result.setClassname((class$org$apache$tools$ant$util$MergingMapper == null ? (class$org$apache$tools$ant$util$MergingMapper = class$("org.apache.tools.ant.util.MergingMapper")) : class$org$apache$tools$ant$util$MergingMapper).getName());
      result.setTo(destfile.getAbsolutePath());
      return result;
   }

   protected File[] toFileArray(String[] name) {
      if (name == null) {
         return null;
      } else {
         ArrayList list = new ArrayList(name.length);

         for(int i = 0; i < name.length; ++i) {
            if (name[i] != null) {
               list.add(this.getProject().resolveFile(name[i]));
            }
         }

         return (File[])((File[])list.toArray(new File[list.size()]));
      }
   }

   protected void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            Mapper[] m = new Mapper[]{this.inputMapper, this.outputMapper, this.errorMapper};

            for(int i = 0; i < m.length; ++i) {
               if (m[i] != null) {
                  stk.push(m[i]);
                  m[i].dieOnCircularReference(stk, p);
                  stk.pop();
               }
            }

            Vector[] v = new Vector[]{this.inputFilterChains, this.outputFilterChains, this.errorFilterChains};

            for(int i = 0; i < v.length; ++i) {
               if (v[i] != null) {
                  Iterator fci = v[i].iterator();

                  while(fci.hasNext()) {
                     FilterChain fc = (FilterChain)fci.next();
                     stk.push(fc);
                     fc.dieOnCircularReference(stk, p);
                     stk.pop();
                  }
               }
            }

            this.setChecked(true);
         }

      }
   }

   private RedirectorElement getRef() {
      return (RedirectorElement)this.getCheckedRef();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

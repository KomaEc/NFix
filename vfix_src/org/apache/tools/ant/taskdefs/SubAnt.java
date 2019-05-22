package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;

public class SubAnt extends Task {
   private Path buildpath;
   private Ant ant = null;
   private String subTarget = null;
   private String antfile = "build.xml";
   private File genericantfile = null;
   private boolean verbose = false;
   private boolean inheritAll = false;
   private boolean inheritRefs = false;
   private boolean failOnError = true;
   private String output = null;
   private Vector properties = new Vector();
   private Vector references = new Vector();
   private Vector propertySets = new Vector();
   private Vector targets = new Vector();

   public void handleOutput(String output) {
      if (this.ant != null) {
         this.ant.handleOutput(output);
      } else {
         super.handleOutput(output);
      }

   }

   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
      return this.ant != null ? this.ant.handleInput(buffer, offset, length) : super.handleInput(buffer, offset, length);
   }

   public void handleFlush(String output) {
      if (this.ant != null) {
         this.ant.handleFlush(output);
      } else {
         super.handleFlush(output);
      }

   }

   public void handleErrorOutput(String output) {
      if (this.ant != null) {
         this.ant.handleErrorOutput(output);
      } else {
         super.handleErrorOutput(output);
      }

   }

   public void handleErrorFlush(String output) {
      if (this.ant != null) {
         this.ant.handleErrorFlush(output);
      } else {
         super.handleErrorFlush(output);
      }

   }

   public void execute() {
      if (this.buildpath == null) {
         throw new BuildException("No buildpath specified");
      } else {
         String[] filenames = this.buildpath.list();
         int count = filenames.length;
         if (count < 1) {
            this.log("No sub-builds to iterate on", 1);
         } else {
            BuildException buildException = null;

            for(int i = 0; i < count; ++i) {
               File file = null;
               String subdirPath = null;
               Object thrownException = null;

               try {
                  File directory = null;
                  file = new File(filenames[i]);
                  if (file.isDirectory()) {
                     if (this.verbose) {
                        subdirPath = file.getPath();
                        this.log("Entering directory: " + subdirPath + "\n", 2);
                     }

                     if (this.genericantfile != null) {
                        directory = file;
                        file = this.genericantfile;
                     } else {
                        file = new File(file, this.antfile);
                     }
                  }

                  this.execute(file, directory);
                  if (this.verbose && subdirPath != null) {
                     this.log("Leaving directory: " + subdirPath + "\n", 2);
                  }
               } catch (RuntimeException var9) {
                  if (!this.getProject().isKeepGoingMode()) {
                     if (this.verbose && subdirPath != null) {
                        this.log("Leaving directory: " + subdirPath + "\n", 2);
                     }

                     throw var9;
                  }

                  thrownException = var9;
               } catch (Throwable var10) {
                  if (!this.getProject().isKeepGoingMode()) {
                     if (this.verbose && subdirPath != null) {
                        this.log("Leaving directory: " + subdirPath + "\n", 2);
                     }

                     throw new BuildException(var10);
                  }

                  thrownException = var10;
               }

               if (thrownException != null) {
                  if (thrownException instanceof BuildException) {
                     this.log("File '" + file + "' failed with message '" + ((Throwable)thrownException).getMessage() + "'.", 0);
                     if (buildException == null) {
                        buildException = (BuildException)thrownException;
                     }
                  } else {
                     this.log("Target '" + file + "' failed with message '" + ((Throwable)thrownException).getMessage() + "'.", 0);
                     ((Throwable)thrownException).printStackTrace(System.err);
                     if (buildException == null) {
                        buildException = new BuildException((Throwable)thrownException);
                     }
                  }

                  if (this.verbose && subdirPath != null) {
                     this.log("Leaving directory: " + subdirPath + "\n", 2);
                  }
               }
            }

            if (buildException != null) {
               throw buildException;
            }
         }
      }
   }

   private void execute(File file, File directory) throws BuildException {
      String antfilename;
      if (file.exists() && !file.isDirectory() && file.canRead()) {
         this.ant = this.createAntTask(directory);
         antfilename = file.getAbsolutePath();
         this.ant.setAntfile(antfilename);

         for(int i = 0; i < this.targets.size(); ++i) {
            Ant.TargetElement targetElement = (Ant.TargetElement)this.targets.get(i);
            this.ant.addConfiguredTarget(targetElement);
         }

         try {
            this.ant.execute();
         } catch (BuildException var10) {
            if (this.failOnError) {
               throw var10;
            }

            this.log("Failure for target '" + this.subTarget + "' of: " + antfilename + "\n" + var10.getMessage(), 1);
         } catch (Throwable var11) {
            if (this.failOnError) {
               throw new BuildException(var11);
            }

            this.log("Failure for target '" + this.subTarget + "' of: " + antfilename + "\n" + var11.toString(), 1);
         } finally {
            this.ant = null;
         }

      } else {
         antfilename = "Invalid file: " + file;
         if (this.failOnError) {
            throw new BuildException(antfilename);
         } else {
            this.log(antfilename, 1);
         }
      }
   }

   public void setAntfile(String antfile) {
      this.antfile = antfile;
   }

   public void setGenericAntfile(File afile) {
      this.genericantfile = afile;
   }

   public void setFailonerror(boolean failOnError) {
      this.failOnError = failOnError;
   }

   public void setTarget(String target) {
      this.subTarget = target;
   }

   public void addConfiguredTarget(Ant.TargetElement t) {
      String name = t.getName();
      if ("".equals(name)) {
         throw new BuildException("target name must not be empty");
      } else {
         this.targets.add(t);
      }
   }

   public void setVerbose(boolean on) {
      this.verbose = on;
   }

   public void setOutput(String s) {
      this.output = s;
   }

   public void setInheritall(boolean b) {
      this.inheritAll = b;
   }

   public void setInheritrefs(boolean b) {
      this.inheritRefs = b;
   }

   public void addProperty(Property p) {
      this.properties.addElement(p);
   }

   public void addReference(Ant.Reference r) {
      this.references.addElement(r);
   }

   public void addPropertyset(PropertySet ps) {
      this.propertySets.addElement(ps);
   }

   public void addDirset(DirSet set) {
      this.add(set);
   }

   public void addFileset(FileSet set) {
      this.add(set);
   }

   public void addFilelist(FileList list) {
      this.add(list);
   }

   public void add(ResourceCollection rc) {
      this.getBuildpath().add(rc);
   }

   public void setBuildpath(Path s) {
      this.getBuildpath().append(s);
   }

   public Path createBuildpath() {
      return this.getBuildpath().createPath();
   }

   public Path.PathElement createBuildpathElement() {
      return this.getBuildpath().createPathElement();
   }

   private Path getBuildpath() {
      if (this.buildpath == null) {
         this.buildpath = new Path(this.getProject());
      }

      return this.buildpath;
   }

   public void setBuildpathRef(Reference r) {
      this.createBuildpath().setRefid(r);
   }

   private Ant createAntTask(File directory) {
      Ant antTask = new Ant(this);
      antTask.init();
      if (this.subTarget != null && this.subTarget.length() > 0) {
         antTask.setTarget(this.subTarget);
      }

      if (this.output != null) {
         antTask.setOutput(this.output);
      }

      if (directory != null) {
         antTask.setDir(directory);
      }

      antTask.setInheritAll(this.inheritAll);
      Enumeration i = this.properties.elements();

      while(i.hasMoreElements()) {
         copyProperty(antTask.createProperty(), (Property)i.nextElement());
      }

      i = this.propertySets.elements();

      while(i.hasMoreElements()) {
         antTask.addPropertyset((PropertySet)i.nextElement());
      }

      antTask.setInheritRefs(this.inheritRefs);
      i = this.references.elements();

      while(i.hasMoreElements()) {
         antTask.addReference((Ant.Reference)i.nextElement());
      }

      return antTask;
   }

   private static void copyProperty(Property to, Property from) {
      to.setName(from.getName());
      if (from.getValue() != null) {
         to.setValue(from.getValue());
      }

      if (from.getFile() != null) {
         to.setFile(from.getFile());
      }

      if (from.getResource() != null) {
         to.setResource(from.getResource());
      }

      if (from.getPrefix() != null) {
         to.setPrefix(from.getPrefix());
      }

      if (from.getRefid() != null) {
         to.setRefid(from.getRefid());
      }

      if (from.getEnvironment() != null) {
         to.setEnvironment(from.getEnvironment());
      }

      if (from.getClasspath() != null) {
         to.setClasspath(from.getClasspath());
      }

   }
}

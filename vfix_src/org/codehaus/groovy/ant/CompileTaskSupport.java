package org.codehaus.groovy.ant;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.ErrorReporter;

public abstract class CompileTaskSupport extends MatchingTask {
   protected final LoggingHelper log = new LoggingHelper(this);
   protected Path src;
   protected File destdir;
   protected Path classpath;
   protected CompilerConfiguration config = new CompilerConfiguration();
   protected boolean failOnError = true;

   public void setFailonerror(boolean fail) {
      this.failOnError = fail;
   }

   public boolean getFailonerror() {
      return this.failOnError;
   }

   public Path createSrc() {
      if (this.src == null) {
         this.src = new Path(this.getProject());
      }

      return this.src.createPath();
   }

   public void setSrcdir(Path dir) {
      assert dir != null;

      if (this.src == null) {
         this.src = dir;
      } else {
         this.src.append(dir);
      }

   }

   public Path getSrcdir() {
      return this.src;
   }

   public void setDestdir(File dir) {
      assert dir != null;

      this.destdir = dir;
   }

   public void setClasspath(Path path) {
      assert path != null;

      if (this.classpath == null) {
         this.classpath = path;
      } else {
         this.classpath.append(path);
      }

   }

   public Path getClasspath() {
      return this.classpath;
   }

   public Path createClasspath() {
      if (this.classpath == null) {
         this.classpath = new Path(this.getProject());
      }

      return this.classpath.createPath();
   }

   public void setClasspathRef(Reference r) {
      assert r != null;

      this.createClasspath().setRefid(r);
   }

   public CompilerConfiguration createConfiguration() {
      return this.config;
   }

   protected void validate() throws BuildException {
      if (this.src == null) {
         throw new BuildException("Missing attribute: srcdir (or one or more nested <src> elements).", this.getLocation());
      } else if (this.destdir == null) {
         throw new BuildException("Missing attribute: destdir", this.getLocation());
      } else if (!this.destdir.exists()) {
         throw new BuildException("Destination directory does not exist: " + this.destdir, this.getLocation());
      }
   }

   protected GroovyClassLoader createClassLoader() {
      ClassLoader parent = ClassLoader.getSystemClassLoader();
      GroovyClassLoader gcl = new GroovyClassLoader(parent, this.config);
      Path path = this.getClasspath();
      if (path != null) {
         String[] filePaths = path.list();

         for(int i = 0; i < filePaths.length; ++i) {
            String filePath = filePaths[i];
            gcl.addClasspath(filePath);
         }
      }

      return gcl;
   }

   protected void handleException(Exception e) throws BuildException {
      assert e != null;

      StringWriter writer = new StringWriter();
      (new ErrorReporter(e, false)).write(new PrintWriter(writer));
      String message = writer.toString();
      if (this.failOnError) {
         throw new BuildException(message, e, this.getLocation());
      } else {
         this.log.error(message);
      }
   }

   public void execute() throws BuildException {
      this.validate();

      try {
         this.compile();
      } catch (Exception var2) {
         this.handleException(var2);
      }

   }

   protected abstract void compile() throws Exception;
}

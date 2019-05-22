package org.codehaus.groovy.ant;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class UberCompileTask extends Task {
   private final LoggingHelper log = new LoggingHelper(this);
   private Path src;
   private File destdir;
   private Path classpath;
   private UberCompileTask.GenStubsAdapter genStubsTask;
   private UberCompileTask.GroovycAdapter groovycTask;
   private UberCompileTask.JavacAdapter javacTask;

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

   public UberCompileTask.GenStubsAdapter createGeneratestubs() {
      if (this.genStubsTask == null) {
         this.genStubsTask = new UberCompileTask.GenStubsAdapter();
         this.genStubsTask.setProject(this.getProject());
      }

      return this.genStubsTask;
   }

   public UberCompileTask.GroovycAdapter createGroovyc() {
      if (this.groovycTask == null) {
         this.groovycTask = new UberCompileTask.GroovycAdapter();
         this.groovycTask.setProject(this.getProject());
      }

      return this.groovycTask;
   }

   public UberCompileTask.JavacAdapter createJavac() {
      if (this.javacTask == null) {
         this.javacTask = new UberCompileTask.JavacAdapter();
         this.javacTask.setProject(this.getProject());
      }

      return this.javacTask;
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

   public void execute() throws BuildException {
      this.validate();
      UberCompileTask.GenStubsAdapter genstubs = this.createGeneratestubs();
      genstubs.classpath = this.classpath;
      genstubs.src = this.src;
      if (genstubs.destdir == null) {
         genstubs.destdir = this.createTempDir();
      }

      FileSet fileset = genstubs.getFileSet();
      if (!fileset.hasPatterns()) {
         genstubs.createInclude().setName("**/*.java");
         genstubs.createInclude().setName("**/*.groovy");
      }

      UberCompileTask.JavacAdapter javac = this.createJavac();
      javac.setSrcdir(this.src);
      javac.setDestdir(this.destdir);
      javac.setClasspath(this.classpath);
      fileset = javac.getFileSet();
      if (!fileset.hasPatterns()) {
         javac.createInclude().setName("**/*.java");
      }

      javac.createSrc().createPathElement().setLocation(genstubs.destdir);
      UberCompileTask.GroovycAdapter groovyc = this.createGroovyc();
      groovyc.classpath = this.classpath;
      groovyc.src = this.src;
      groovyc.destdir = this.destdir;
      groovyc.force = true;
      fileset = groovyc.getFileSet();
      if (!fileset.hasPatterns()) {
         groovyc.createInclude().setName("**/*.groovy");
      }

      genstubs.execute();
      javac.execute();
      groovyc.execute();
   }

   private File createTempDir() {
      try {
         File dir = File.createTempFile("groovy-", "stubs");
         dir.delete();
         dir.mkdirs();
         return dir;
      } catch (IOException var2) {
         throw new BuildException(var2, this.getLocation());
      }
   }

   private class GroovycAdapter extends GroovycTask {
      private GroovycAdapter() {
      }

      public FileSet getFileSet() {
         return super.getImplicitFileSet();
      }

      public String getTaskName() {
         return UberCompileTask.this.getTaskName() + ":groovyc";
      }

      // $FF: synthetic method
      GroovycAdapter(Object x1) {
         this();
      }
   }

   private class JavacAdapter extends Javac {
      private JavacAdapter() {
      }

      public FileSet getFileSet() {
         return super.getImplicitFileSet();
      }

      public String getTaskName() {
         return UberCompileTask.this.getTaskName() + ":javac";
      }

      // $FF: synthetic method
      JavacAdapter(Object x1) {
         this();
      }
   }

   private class GenStubsAdapter extends GenerateStubsTask {
      private GenStubsAdapter() {
      }

      public FileSet getFileSet() {
         return super.getImplicitFileSet();
      }

      public String getTaskName() {
         return UberCompileTask.this.getTaskName() + ":genstubs";
      }

      // $FF: synthetic method
      GenStubsAdapter(Object x1) {
         this();
      }
   }
}

package org.codehaus.groovy.ant;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.security.CodeSource;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;
import org.codehaus.groovy.control.CompilationUnit;

public class GroovycTask extends CompileTaskSupport {
   protected boolean force;

   public void setForce(boolean flag) {
      this.force = flag;
   }

   protected void compile() {
      Path path = this.getClasspath();
      if (path != null) {
         this.config.setClasspath(path.toString());
      }

      this.config.setTargetDirectory(this.destdir);
      GroovyClassLoader gcl = this.createClassLoader();
      CompilationUnit compilation = new CompilationUnit(this.config, (CodeSource)null, gcl);
      GlobPatternMapper mapper = new GlobPatternMapper();
      mapper.setFrom("*.groovy");
      mapper.setTo("*.class");
      int count = 0;
      String[] list = this.src.list();

      for(int i = 0; i < list.length; ++i) {
         File basedir = this.getProject().resolveFile(list[i]);
         if (!basedir.exists()) {
            throw new BuildException("Source directory does not exist: " + basedir, this.getLocation());
         }

         DirectoryScanner scanner = this.getDirectoryScanner(basedir);
         String[] includes = scanner.getIncludedFiles();
         if (this.force) {
            this.log.debug("Forcefully including all files from: " + basedir);

            for(int j = 0; j < includes.length; ++j) {
               File file = new File(basedir, includes[j]);
               this.log.debug("    " + file);
               compilation.addSource(file);
               ++count;
            }
         } else {
            this.log.debug("Including changed files from: " + basedir);
            SourceFileScanner sourceScanner = new SourceFileScanner(this);
            File[] files = sourceScanner.restrictAsFiles(includes, basedir, this.destdir, mapper);

            for(int j = 0; j < files.length; ++j) {
               this.log.debug("    " + files[j]);
               compilation.addSource(files[j]);
               ++count;
            }
         }
      }

      if (count > 0) {
         this.log.info("Compiling " + count + " source file" + (count > 1 ? "s" : "") + " to " + this.destdir);
         compilation.compile();
      } else {
         this.log.info("No sources found to compile");
      }

   }
}

package org.codehaus.groovy.ant;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.codehaus.groovy.tools.javac.JavaStubCompilationUnit;

public class GenerateStubsTask extends CompileTaskSupport {
   protected void compile() {
      GroovyClassLoader gcl = this.createClassLoader();
      JavaStubCompilationUnit cu = new JavaStubCompilationUnit(this.config, gcl, this.destdir);
      int count = 0;
      String[] list = this.src.list();

      for(int i = 0; i < list.length; ++i) {
         File basedir = this.getProject().resolveFile(list[i]);
         if (!basedir.exists()) {
            throw new BuildException("Source directory does not exist: " + basedir, this.getLocation());
         }

         DirectoryScanner scanner = this.getDirectoryScanner(basedir);
         String[] includes = scanner.getIncludedFiles();
         this.log.debug("Including files from: " + basedir);

         for(int j = 0; j < includes.length; ++j) {
            this.log.debug("    " + includes[j]);
            File file = new File(basedir, includes[j]);
            cu.addSource(file);
            if (!includes[j].endsWith(".java")) {
               ++count;
            }
         }
      }

      if (count > 0) {
         this.log.info("Generating " + count + " Java stub" + (count > 1 ? "s" : "") + " to " + this.destdir);
         cu.compile();
         this.log.info("Generated " + cu.getStubCount() + " Java stub(s)");
      } else {
         this.log.info("No sources found for stub generation");
      }

   }
}

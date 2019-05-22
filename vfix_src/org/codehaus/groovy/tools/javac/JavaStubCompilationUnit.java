package org.codehaus.groovy.tools.javac;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Map;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;

public class JavaStubCompilationUnit extends CompilationUnit {
   private static final String DOT_GROOVY = ".groovy";
   private final JavaStubGenerator stubGenerator;
   private int stubCount;

   public JavaStubCompilationUnit(CompilerConfiguration config, GroovyClassLoader gcl, File destDir) {
      super(config, (CodeSource)null, gcl);

      assert config != null;

      Map options = config.getJointCompilationOptions();
      if (destDir == null) {
         destDir = (File)options.get("stubDir");
      }

      boolean useJava5 = config.getTargetBytecode().equals("1.5");
      this.stubGenerator = new JavaStubGenerator(destDir, false, useJava5);
      this.addPhaseOperation(new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode node) throws CompilationFailedException {
            try {
               JavaStubCompilationUnit.this.stubGenerator.generateClass(node);
               JavaStubCompilationUnit.this.stubCount++;
            } catch (FileNotFoundException var5) {
               source.addException(var5);
            }

         }
      }, 4);
   }

   public JavaStubCompilationUnit(CompilerConfiguration config, GroovyClassLoader gcl) {
      this(config, gcl, (File)null);
   }

   public int getStubCount() {
      return this.stubCount;
   }

   public void compile() throws CompilationFailedException {
      this.stubCount = 0;
      super.compile(4);
   }

   public void configure(CompilerConfiguration config) {
      super.configure(config);
      File targetDir = config.getTargetDirectory();
      if (targetDir != null) {
         String classOutput = targetDir.getAbsolutePath();
         this.getClassLoader().addClasspath(classOutput);
      }

   }

   public SourceUnit addSource(File file) {
      return file.getName().toLowerCase().endsWith(".groovy") ? super.addSource(file) : null;
   }

   public SourceUnit addSource(URL url) {
      return url.getPath().toLowerCase().endsWith(".groovy") ? super.addSource(url) : null;
   }

   /** @deprecated */
   @Deprecated
   public void addSourceFile(File file) {
      this.addSource(file);
   }
}

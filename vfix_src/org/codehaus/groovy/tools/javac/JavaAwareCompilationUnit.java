package org.codehaus.groovy.tools.javac;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.CodeSource;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.classgen.VariableScopeVisitor;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;

public class JavaAwareCompilationUnit extends CompilationUnit {
   private LinkedList javaSources;
   private JavaStubGenerator stubGenerator;
   private JavaCompilerFactory compilerFactory;
   private File generationGoal;
   private boolean keepStubs;

   public JavaAwareCompilationUnit(CompilerConfiguration configuration) {
      this(configuration, (GroovyClassLoader)null);
   }

   public JavaAwareCompilationUnit(CompilerConfiguration configuration, GroovyClassLoader groovyClassLoader) {
      super(configuration, (CodeSource)null, groovyClassLoader);
      this.compilerFactory = new JavacCompilerFactory();
      this.javaSources = new LinkedList();
      Map options = configuration.getJointCompilationOptions();
      this.generationGoal = (File)options.get("stubDir");
      boolean useJava5 = configuration.getTargetBytecode().equals("1.5");
      this.stubGenerator = new JavaStubGenerator(this.generationGoal, false, useJava5);
      this.keepStubs = Boolean.TRUE.equals(options.get("keepStubs"));
      this.addPhaseOperation(new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode node) throws CompilationFailedException {
            if (JavaAwareCompilationUnit.this.javaSources.size() != 0) {
               VariableScopeVisitor scopeVisitor = new VariableScopeVisitor(source);
               scopeVisitor.visitClass(node);
               (new JavaAwareResolveVisitor(JavaAwareCompilationUnit.this)).startResolving(node, source);
            }

         }
      }, 3);
      this.addPhaseOperation(new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
            try {
               if (JavaAwareCompilationUnit.this.javaSources.size() != 0) {
                  JavaAwareCompilationUnit.this.stubGenerator.generateClass(classNode);
               }
            } catch (FileNotFoundException var5) {
               source.addException(var5);
            }

         }
      }, 3);
   }

   public void gotoPhase(int phase) throws CompilationFailedException {
      super.gotoPhase(phase);
      if (phase == 4 && this.javaSources.size() > 0) {
         Iterator modules = this.getAST().getModules().iterator();

         while(modules.hasNext()) {
            ModuleNode module = (ModuleNode)modules.next();
            module.setImportsResolved(false);
         }

         try {
            JavaCompiler compiler = this.compilerFactory.createCompiler(this.getConfiguration());
            compiler.compile(this.javaSources, this);
         } finally {
            if (!this.keepStubs) {
               this.stubGenerator.clean();
            }

            this.javaSources.clear();
         }
      }

   }

   public void configure(CompilerConfiguration configuration) {
      super.configure(configuration);
      File targetDir = configuration.getTargetDirectory();
      if (targetDir != null) {
         String classOutput = targetDir.getAbsolutePath();
         this.getClassLoader().addClasspath(classOutput);
      }

   }

   private void addJavaSource(File file) {
      String path = file.getAbsolutePath();
      Iterator iter = this.javaSources.iterator();

      String su;
      do {
         if (!iter.hasNext()) {
            this.javaSources.add(path);
            return;
         }

         su = (String)iter.next();
      } while(!path.equals(su));

   }

   public void addSources(String[] paths) {
      for(int i = 0; i < paths.length; ++i) {
         File file = new File(paths[i]);
         if (file.getName().endsWith(".java")) {
            this.addJavaSource(file);
         } else {
            this.addSource(file);
         }
      }

   }

   public void addSources(File[] files) {
      for(int i = 0; i < files.length; ++i) {
         if (files[i].getName().endsWith(".java")) {
            this.addJavaSource(files[i]);
         } else {
            this.addSource(files[i]);
         }
      }

   }

   public JavaCompilerFactory getCompilerFactory() {
      return this.compilerFactory;
   }

   public void setCompilerFactory(JavaCompilerFactory compilerFactory) {
      this.compilerFactory = compilerFactory;
   }
}

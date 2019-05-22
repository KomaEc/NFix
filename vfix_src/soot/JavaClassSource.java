package soot;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.Node;
import soot.javaToJimple.IInitialResolver;
import soot.javaToJimple.InitialResolver;
import soot.options.Options;
import soot.toolkits.astmetrics.ComputeASTMetrics;

public class JavaClassSource extends ClassSource {
   private static final Logger logger = LoggerFactory.getLogger(JavaClassSource.class);
   private File fullPath;

   public JavaClassSource(String className, File fullPath) {
      super(className);
      this.fullPath = fullPath;
   }

   public JavaClassSource(String className) {
      super(className);
   }

   public IInitialResolver.Dependencies resolve(SootClass sc) {
      if (Options.v().verbose()) {
         logger.debug("resolving [from .java]: " + this.className);
      }

      Object resolver;
      if (Options.v().polyglot()) {
         resolver = InitialResolver.v();
      } else {
         resolver = JastAddInitialResolver.v();
      }

      if (this.fullPath != null) {
         ((IInitialResolver)resolver).formAst(this.fullPath.getPath(), SourceLocator.v().sourcePath(), this.className);
      }

      IInitialResolver.Dependencies references = ((IInitialResolver)resolver).resolveFromJavaFile(sc);
      if (Options.v().ast_metrics()) {
         Node ast = InitialResolver.v().getAst();
         if (ast == null) {
            logger.debug("No compatible AST available for AST metrics. Skipping. Try -polyglot option.");
         } else {
            ComputeASTMetrics metrics = new ComputeASTMetrics(ast);
            metrics.apply();
         }
      }

      return references;
   }
}

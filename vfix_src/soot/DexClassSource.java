package soot;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dexpler.DexResolver;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;

public class DexClassSource extends ClassSource {
   private static final Logger logger = LoggerFactory.getLogger(DexClassSource.class);
   protected File path;

   public DexClassSource(String className, File path) {
      super(className);
      this.path = path;
   }

   public IInitialResolver.Dependencies resolve(SootClass sc) {
      if (Options.v().verbose()) {
         logger.debug("resolving " + this.className + " from file " + this.path.getPath());
      }

      return DexResolver.v().resolveFromFile(this.path, this.className, sc);
   }
}

package soot;

import soot.options.Options;

public class JimpleClassProvider implements ClassProvider {
   public ClassSource find(String className) {
      String fileName = className + ".jimple";
      FoundFile file = SourceLocator.v().lookupInClassPath(fileName);
      if (file == null) {
         if (Options.v().permissive_resolving()) {
            fileName = className.replace('.', '/') + ".jimple";
            file = SourceLocator.v().lookupInClassPath(fileName);
         }

         if (file == null) {
            return null;
         }
      }

      return new JimpleClassSource(className, file);
   }
}

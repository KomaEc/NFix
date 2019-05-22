package soot;

import soot.javaToJimple.InitialResolver;
import soot.options.Options;

public class JavaClassProvider implements ClassProvider {
   public ClassSource find(String className) {
      if (Options.v().polyglot() && InitialResolver.v().hasASTForSootName(className)) {
         InitialResolver.v().setASTForSootName(className);
         return new JavaClassSource(className);
      } else {
         boolean checkAgain = className.indexOf(36) >= 0;
         FoundFile file = null;

         JavaClassSource var6;
         try {
            String javaClassName = SourceLocator.v().getSourceForClass(className);
            String fileName = javaClassName.replace('.', '/') + ".java";
            file = SourceLocator.v().lookupInClassPath(fileName);
            if (file == null && checkAgain) {
               fileName = className.replace('.', '/') + ".java";
               file = SourceLocator.v().lookupInClassPath(fileName);
            }

            if (file == null) {
               var6 = null;
               return var6;
            }

            if (file.isZipFile()) {
               throw new JavaClassProvider.JarException(className);
            }

            var6 = new JavaClassSource(className, file.getFile());
         } finally {
            if (file != null) {
               file.close();
            }

         }

         return var6;
      }
   }

   public static class JarException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      public JarException(String className) {
         super("Class " + className + " was found in an archive, but Soot doesn't support reading source files out of an archive");
      }
   }
}

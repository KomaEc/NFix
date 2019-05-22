package soot;

public class CoffiClassProvider implements ClassProvider {
   public ClassSource find(String className) {
      String fileName = className.replace('.', '/') + ".class";
      FoundFile file = SourceLocator.v().lookupInClassPath(fileName);
      return file == null ? null : new CoffiClassSource(className, file);
   }
}

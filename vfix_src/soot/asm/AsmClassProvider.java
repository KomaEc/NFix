package soot.asm;

import soot.ClassProvider;
import soot.ClassSource;
import soot.FoundFile;
import soot.SourceLocator;

public class AsmClassProvider implements ClassProvider {
   public ClassSource find(String cls) {
      String clsFile = cls.replace('.', '/') + ".class";
      FoundFile file = SourceLocator.v().lookupInClassPath(clsFile);
      return file == null ? null : new AsmClassSource(cls, file);
   }
}

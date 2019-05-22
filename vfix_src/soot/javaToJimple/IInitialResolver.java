package soot.javaToJimple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.SootClass;
import soot.Type;

public interface IInitialResolver {
   void formAst(String var1, List<String> var2, String var3);

   IInitialResolver.Dependencies resolveFromJavaFile(SootClass var1);

   public static class Dependencies {
      public final Set<Type> typesToHierarchy = new HashSet();
      public final Set<Type> typesToSignature = new HashSet();
   }
}

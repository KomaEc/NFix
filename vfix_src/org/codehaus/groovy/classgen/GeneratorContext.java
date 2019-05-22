package org.codehaus.groovy.classgen;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.MethodNode;

public class GeneratorContext {
   private int innerClassIdx = 1;
   private CompileUnit compileUnit;

   public GeneratorContext(CompileUnit compileUnit) {
      this.compileUnit = compileUnit;
   }

   public int getNextInnerClassIdx() {
      return this.innerClassIdx++;
   }

   public CompileUnit getCompileUnit() {
      return this.compileUnit;
   }

   public String getNextClosureInnerName(ClassNode owner, ClassNode enclosingClass, MethodNode enclosingMethod) {
      String ownerShortName = owner.getNameWithoutPackage();
      String classShortName = enclosingClass.getNameWithoutPackage();
      if (classShortName.equals(ownerShortName)) {
         classShortName = "";
      } else {
         classShortName = classShortName + "_";
      }

      int dp = classShortName.lastIndexOf("$");
      if (dp >= 0) {
         ++dp;
         classShortName = classShortName.substring(dp);
      }

      if (classShortName.startsWith("_")) {
         classShortName = classShortName.substring(1);
      }

      String methodName = "";
      if (enclosingMethod != null) {
         methodName = enclosingMethod.getName() + "_";
         if (enclosingClass.isDerivedFrom(ClassHelper.CLOSURE_TYPE)) {
            methodName = "";
         }

         methodName = methodName.replace('<', '_');
         methodName = methodName.replace('>', '_');
         methodName = methodName.replaceAll(" ", "_");
      }

      return "_" + classShortName + methodName + "closure" + this.getNextInnerClassIdx();
   }
}

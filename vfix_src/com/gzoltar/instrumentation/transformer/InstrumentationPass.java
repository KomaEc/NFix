package com.gzoltar.instrumentation.transformer;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.CtBehavior;
import com.gzoltar.shaded.javassist.CtClass;
import com.gzoltar.shaded.javassist.CtConstructor;
import com.gzoltar.shaded.javassist.CtField;
import com.gzoltar.shaded.javassist.CtMethod;
import com.gzoltar.shaded.javassist.bytecode.Bytecode;
import com.gzoltar.shaded.javassist.bytecode.CodeAttribute;
import com.gzoltar.shaded.javassist.bytecode.CodeIterator;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import com.gzoltar.shaded.javassist.bytecode.MethodInfo;
import java.util.HashSet;
import java.util.Set;

public class InstrumentationPass {
   private Set<Integer> uniqueLines = new HashSet();

   public void transform(ClassPool var1, CtClass var2, boolean var3, boolean var4) throws Exception {
      ProbeInserter var5 = ProbeRegistry.newProbeInserter(var2.getName());
      boolean var6 = false;
      CtBehavior[] var7;
      int var8 = (var7 = var2.getDeclaredBehaviors()).length;

      for(int var9 = 0; var9 < var8; ++var9) {
         CtBehavior var10 = var7[var9];
         if (!var3 && var10.hasAnnotation(Deprecated.class)) {
            Logger.getInstance().debug("Skipping deprecated method '" + var10.getName() + "'");
         } else {
            MethodInfo var11;
            CodeAttribute var12;
            if ((var12 = (var11 = var10.getMethodInfo()).getCodeAttribute()) == null) {
               Logger.getInstance().debug("Skipping method '" + var10.getName() + "' as it does not have any code");
            } else if (var11.isMethod() && Spectra.getInstance().getGranularity() == Component.Granularity.CLASS) {
               Logger.getInstance().debug("Skipping method '" + var10.getName() + "'");
            } else if (!AccessFlags.isBridge(var11.getAccessFlags()) && !AccessFlags.isSynthetic(var11.getAccessFlags()) && var11.getAttribute("Synthetic") == null) {
               if (!var2.isEnum() || !var10.getName().equals("values") && !var10.getName().equals("valueOf")) {
                  if (this.isAnonymousClass(var2) && var11.getAccessFlags() == 0) {
                     Logger.getInstance().debug("Skipping method '" + var10.getName() + "' of anonymous class '" + var2.getName() + " as it does not have any access flag");
                  } else {
                     boolean var13 = this.transformMethod(var2, var10, var5, var4);
                     var6 = var6 || var13;
                     var12.setMaxStack(var12.computeMaxStack());
                     var11.rebuildStackMapIf6(var1, var2.getClassFile());
                  }
               } else {
                  Logger.getInstance().debug("Skipping method '" + var10.getName() + "' as (likely) it only exists in bytecode");
               }
            } else {
               Logger.getInstance().debug("Skipping method '" + var10.getName() + "' as it is a bridge or synthetic method");
            }
         }
      }

      if (var6) {
         CtField var14;
         (var14 = CtField.make(var5.getHitArrayDeclaration(), var2)).setModifiers(4121);
         var2.addField(var14);
         if (var2.isInterface()) {
            var2.makeClassInitializer().insertBefore(var5.getInitFieldCall());
            return;
         }

         CtMethod var15;
         (var15 = new CtMethod(CtClass.voidType, "$__gz_init", (CtClass[])null, var2)).setModifiers(4106);
         var15.setBody(var5.getBodyInitMethod());
         var2.addMethod(var15);
         var2.makeClassInitializer().insertBefore(var5.getInitMethodCall());
         CtConstructor[] var16;
         int var17 = (var16 = var2.getConstructors()).length;

         for(int var18 = 0; var18 < var17; ++var18) {
            var16[var18].insertBefore(var5.getInitMethodCall());
         }
      }

   }

   private boolean transformMethod(CtClass var1, CtBehavior var2, ProbeInserter var3, boolean var4) throws Exception {
      String var5 = var2.getName() + var2.getSignature();
      Logger.getInstance().debug("        METHOD: " + var5);
      MethodInfo var12;
      CodeIterator var6 = (var12 = var2.getMethodInfo()).getCodeAttribute().iterator();
      boolean var7 = false;

      int var10;
      for(HashSet var8 = new HashSet(); var6.hasNext(); this.uniqueLines.add(var10)) {
         int var9 = var6.next();
         if ((var10 = var12.getLineNumber(var9)) != -1 && (!var4 || var10 != 1 && !this.uniqueLines.contains(var10))) {
            int var11 = var3.newStatementProbe(var1, var5, var10);
            if (var8.add(var11)) {
               Bytecode var13 = this.getInstrumentationCode(var1, var11, var12.getConstPool());
               var6.insert(var9, var13.get());
               var7 = true;
            }

            if (Spectra.getInstance().getGranularity() != Component.Granularity.STATEMENT) {
               break;
            }
         }
      }

      return var7;
   }

   private Bytecode getInstrumentationCode(CtClass var1, int var2, ConstPool var3) {
      Bytecode var4;
      (var4 = new Bytecode(var3)).addGetstatic(var1, "$__gz_counters", "[I");
      var4.addIconst(var2);
      var4.addOpcode(92);
      var4.addOpcode(46);
      var4.addOpcode(4);
      var4.addOpcode(96);
      var4.addOpcode(79);
      return var4;
   }

   private boolean isAnonymousClass(CtClass var1) {
      int var2;
      return (var2 = var1.getName().lastIndexOf(36)) < 0 ? false : Character.isDigit(var1.getName().charAt(var2 + 1));
   }
}

package org.jf.dexlib2.analysis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.instruction.InlineIndexInstruction;
import org.jf.dexlib2.iface.instruction.VariableRegisterInstruction;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.jf.dexlib2.immutable.ImmutableMethodImplementation;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.util.ParamUtil;

public abstract class InlineMethodResolver {
   public static final int STATIC = 8;
   public static final int VIRTUAL = 1;
   public static final int DIRECT = 2;

   @Nonnull
   public static InlineMethodResolver createInlineMethodResolver(int odexVersion) {
      if (odexVersion == 35) {
         return new InlineMethodResolver.InlineMethodResolver_version35();
      } else if (odexVersion == 36) {
         return new InlineMethodResolver.InlineMethodResolver_version36();
      } else {
         throw new RuntimeException(String.format("odex version %d is not supported yet", odexVersion));
      }
   }

   protected InlineMethodResolver() {
   }

   @Nonnull
   private static Method inlineMethod(int accessFlags, @Nonnull String cls, @Nonnull String name, @Nonnull String params, @Nonnull String returnType) {
      ImmutableList<ImmutableMethodParameter> paramList = ImmutableList.copyOf(ParamUtil.parseParamString(params));
      return new ImmutableMethod(cls, name, paramList, returnType, accessFlags, (ImmutableSet)null, (ImmutableMethodImplementation)null);
   }

   @Nonnull
   public abstract Method resolveExecuteInline(@Nonnull AnalyzedInstruction var1);

   private static class InlineMethodResolver_version36 extends InlineMethodResolver {
      private final Method[] inlineMethods = new Method[]{InlineMethodResolver.inlineMethod(8, "Lorg/apache/harmony/dalvik/NativeTestTarget;", "emptyInlineMethod", "", "V"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "charAt", "I", "C"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "compareTo", "Ljava/lang/String;", "I"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "equals", "Ljava/lang/Object;", "Z"), null, null, InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "length", "", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "I", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "J", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "F", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "min", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "max", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sqrt", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "cos", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sin", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Float;", "floatToIntBits", "F", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Float;", "floatToRawIntBits", "F", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Float;", "intBitsToFloat", "I", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Double;", "doubleToLongBits", "D", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Double;", "doubleToRawLongBits", "D", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Double;", "longBitsToDouble", "J", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "I", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "J", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "F", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "min", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "max", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "sqrt", "D", "D")};
      private final Method indexOfIMethod = InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "indexOf", "I", "I");
      private final Method indexOfIIMethod = InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "indexOf", "II", "I");
      private final Method fastIndexOfMethod = InlineMethodResolver.inlineMethod(2, "Ljava/lang/String;", "fastIndexOf", "II", "I");
      private final Method isEmptyMethod = InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "isEmpty", "", "Z");

      public InlineMethodResolver_version36() {
      }

      @Nonnull
      public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
         InlineIndexInstruction instruction = (InlineIndexInstruction)analyzedInstruction.instruction;
         int inlineIndex = instruction.getInlineIndex();
         if (inlineIndex >= 0 && inlineIndex < this.inlineMethods.length) {
            int parameterCount;
            if (inlineIndex == 4) {
               parameterCount = ((VariableRegisterInstruction)instruction).getRegisterCount();
               if (parameterCount == 2) {
                  return this.indexOfIMethod;
               } else if (parameterCount == 3) {
                  return this.fastIndexOfMethod;
               } else {
                  throw new RuntimeException("Could not determine the correct inline method to use");
               }
            } else if (inlineIndex == 5) {
               parameterCount = ((VariableRegisterInstruction)instruction).getRegisterCount();
               if (parameterCount == 3) {
                  return this.indexOfIIMethod;
               } else if (parameterCount == 1) {
                  return this.isEmptyMethod;
               } else {
                  throw new RuntimeException("Could not determine the correct inline method to use");
               }
            } else {
               return this.inlineMethods[inlineIndex];
            }
         } else {
            throw new RuntimeException("Invalid method index: " + inlineIndex);
         }
      }
   }

   private static class InlineMethodResolver_version35 extends InlineMethodResolver {
      private final Method[] inlineMethods = new Method[]{InlineMethodResolver.inlineMethod(8, "Lorg/apache/harmony/dalvik/NativeTestTarget;", "emptyInlineMethod", "", "V"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "charAt", "I", "C"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "compareTo", "Ljava/lang/String;", "I"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "equals", "Ljava/lang/Object;", "Z"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "length", "", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "I", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "J", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "F", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "min", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "max", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sqrt", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "cos", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sin", "D", "D")};

      public InlineMethodResolver_version35() {
      }

      @Nonnull
      public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
         InlineIndexInstruction instruction = (InlineIndexInstruction)analyzedInstruction.instruction;
         int inlineIndex = instruction.getInlineIndex();
         if (inlineIndex >= 0 && inlineIndex < this.inlineMethods.length) {
            return this.inlineMethods[inlineIndex];
         } else {
            throw new RuntimeException("Invalid inline index: " + inlineIndex);
         }
      }
   }
}

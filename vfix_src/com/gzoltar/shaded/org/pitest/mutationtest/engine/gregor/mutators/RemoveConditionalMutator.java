package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.List;

public class RemoveConditionalMutator implements MethodMutatorFactory {
   private final RemoveConditionalMutator.Choice kind;
   private final boolean replaceWith;

   public RemoveConditionalMutator(RemoveConditionalMutator.Choice c, boolean rc) {
      this.kind = c;
      this.replaceWith = rc;
   }

   public static Iterable<MethodMutatorFactory> makeMutators() {
      List<MethodMutatorFactory> variations = new ArrayList();
      RemoveConditionalMutator.Choice[] allChoices = new RemoveConditionalMutator.Choice[]{RemoveConditionalMutator.Choice.EQUAL, RemoveConditionalMutator.Choice.ORDER};
      boolean[] arrWith = new boolean[]{true, false};
      RemoveConditionalMutator.Choice[] arr$ = allChoices;
      int len$ = allChoices.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         RemoveConditionalMutator.Choice c = arr$[i$];
         boolean[] arr$ = arrWith;
         int len$ = arrWith.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            boolean b = arr$[i$];
            variations.add(new RemoveConditionalMutator(c, b));
         }
      }

      return variations;
   }

   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new RemoveConditionalMutator.RemoveConditionalMethodVisitor(this, context, methodVisitor, "removed conditional - replaced " + this.kind.description() + " check with " + this.replaceWith);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName() + "_" + this.kind + "_" + (this.replaceWith ? "IF" : "ELSE");
   }

   public String getName() {
      return "REMOVE_CONDITIONALS_" + this.kind + "_" + (this.replaceWith ? "IF" : "ELSE") + "_MUTATOR";
   }

   private final class RemoveConditionalMethodVisitor extends MethodVisitor {
      private final String description;
      private final MutationContext context;
      private final MethodMutatorFactory factory;

      public RemoveConditionalMethodVisitor(MethodMutatorFactory factory, MutationContext context, MethodVisitor delegateMethodVisitor, String description) {
         super(327680, delegateMethodVisitor);
         this.context = context;
         this.factory = factory;
         this.description = description;
      }

      public void visitJumpInsn(int opcode, Label label) {
         if (this.canMutate(opcode)) {
            MutationIdentifier newId = this.context.registerMutation(this.factory, this.description);
            if (this.context.shouldMutate(newId)) {
               this.emptyStack(opcode);
               if (!RemoveConditionalMutator.this.replaceWith) {
                  super.visitJumpInsn(167, label);
               }
            } else {
               this.mv.visitJumpInsn(opcode, label);
            }
         } else {
            this.mv.visitJumpInsn(opcode, label);
         }

      }

      private void emptyStack(int opcode) {
         switch(opcode) {
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
            super.visitInsn(88);
            break;
         default:
            super.visitInsn(87);
         }

      }

      private boolean canMutate(int opcode) {
         switch(opcode) {
         case 153:
         case 154:
         case 159:
         case 160:
         case 165:
         case 166:
         case 198:
         case 199:
            return RemoveConditionalMutator.this.kind == RemoveConditionalMutator.Choice.EQUAL;
         case 155:
         case 156:
         case 157:
         case 158:
         case 161:
         case 162:
         case 163:
         case 164:
            return RemoveConditionalMutator.this.kind == RemoveConditionalMutator.Choice.ORDER;
         case 167:
         case 168:
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         case 192:
         case 193:
         case 194:
         case 195:
         case 196:
         case 197:
         default:
            return false;
         }
      }
   }

   public static enum Choice {
      EQUAL("equality"),
      ORDER("comparison");

      private String desc;

      private Choice(String desc) {
         this.desc = desc;
      }

      String description() {
         return this.desc;
      }
   }
}

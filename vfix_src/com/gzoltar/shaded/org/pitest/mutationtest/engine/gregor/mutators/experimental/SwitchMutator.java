package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class SwitchMutator implements MethodMutatorFactory {
   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new SwitchMutator.SwitchMethodVisitor(context, methodVisitor);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   public String getName() {
      return "EXPERIMENTAL_SWITCH_MUTATOR";
   }

   private final class SwitchMethodVisitor extends MethodVisitor {
      private final MutationContext context;

      public SwitchMethodVisitor(MutationContext context, MethodVisitor methodVisitor) {
         super(327680, methodVisitor);
         this.context = context;
      }

      public void visitTableSwitchInsn(int i, int i1, Label defaultLabel, Label... labels) {
         Label newDefault = this.firstDifferentLabel(labels, defaultLabel);
         if (newDefault != null && this.shouldMutate()) {
            Label[] newLabels = this.swapLabels(labels, defaultLabel, newDefault);
            super.visitTableSwitchInsn(i, i1, newDefault, newLabels);
         } else {
            super.visitTableSwitchInsn(i, i1, defaultLabel, labels);
         }

      }

      public void visitLookupSwitchInsn(Label defaultLabel, int[] ints, Label[] labels) {
         Label newDefault = this.firstDifferentLabel(labels, defaultLabel);
         if (newDefault != null && this.shouldMutate()) {
            Label[] newLabels = this.swapLabels(labels, defaultLabel, newDefault);
            super.visitLookupSwitchInsn(newDefault, ints, newLabels);
         } else {
            super.visitLookupSwitchInsn(defaultLabel, ints, labels);
         }

      }

      private Label[] swapLabels(Label[] labels, Label defaultLabel, Label newDefault) {
         Label[] swapped = new Label[labels.length];

         for(int i = 0; i < labels.length; ++i) {
            Label candidate = labels[i];
            if (candidate == defaultLabel) {
               swapped[i] = newDefault;
            } else {
               swapped[i] = defaultLabel;
            }
         }

         return swapped;
      }

      private Label firstDifferentLabel(Label[] labels, Label label) {
         Label[] arr$ = labels;
         int len$ = labels.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Label candidate = arr$[i$];
            if (candidate != label) {
               return candidate;
            }
         }

         return null;
      }

      private boolean shouldMutate() {
         MutationIdentifier mutationId = this.context.registerMutation(SwitchMutator.this, "Switch mutation");
         return this.context.shouldMutate(mutationId);
      }
   }
}

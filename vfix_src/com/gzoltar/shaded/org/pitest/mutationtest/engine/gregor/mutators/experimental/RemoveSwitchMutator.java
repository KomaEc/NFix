package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.List;

public class RemoveSwitchMutator implements MethodMutatorFactory {
   private final int key;

   public RemoveSwitchMutator(int i) {
      this.key = i;
   }

   public static Iterable<MethodMutatorFactory> makeMutators() {
      List<MethodMutatorFactory> variations = new ArrayList();

      for(int i = 0; i != 100; ++i) {
         variations.add(new RemoveSwitchMutator(i));
      }

      return variations;
   }

   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new RemoveSwitchMutator.RemoveSwitchMethodVisitor(context, methodVisitor);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName() + "_" + this.key;
   }

   public String getName() {
      return this.toString();
   }

   public String toString() {
      return "EXPERIMENTAL_REMOVE_SWITCH_MUTATOR_" + this.key;
   }

   private final class RemoveSwitchMethodVisitor extends MethodVisitor {
      private final MutationContext context;

      public RemoveSwitchMethodVisitor(MutationContext context, MethodVisitor methodVisitor) {
         super(327680, methodVisitor);
         this.context = context;
      }

      public void visitTableSwitchInsn(int i, int i1, Label defaultLabel, Label... labels) {
         if (labels.length > RemoveSwitchMutator.this.key && this.shouldMutate()) {
            Label[] newLabels = (Label[])labels.clone();
            newLabels[RemoveSwitchMutator.this.key] = defaultLabel;
            super.visitTableSwitchInsn(i, i1, defaultLabel, newLabels);
         } else {
            super.visitTableSwitchInsn(i, i1, defaultLabel, labels);
         }

      }

      public void visitLookupSwitchInsn(Label defaultLabel, int[] ints, Label[] labels) {
         if (labels.length > RemoveSwitchMutator.this.key && this.shouldMutate()) {
            Label[] newLabels = (Label[])labels.clone();
            newLabels[RemoveSwitchMutator.this.key] = defaultLabel;
            super.visitLookupSwitchInsn(defaultLabel, ints, newLabels);
         } else {
            super.visitLookupSwitchInsn(defaultLabel, ints, labels);
         }

      }

      private boolean shouldMutate() {
         MutationIdentifier mutationId = this.context.registerMutation(RemoveSwitchMutator.this, "RemoveSwitch " + RemoveSwitchMutator.this.key + " mutation");
         return this.context.shouldMutate(mutationId);
      }
   }
}

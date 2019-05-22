package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.Location;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.InstructionCounter;
import java.util.HashSet;
import java.util.Set;

class MethodMutationContext implements MutationContext, InstructionCounter {
   private final ClassContext classContext;
   private final Location location;
   private int instructionIndex;
   private int lastLineNumber;
   private final Set<String> mutationFindingDisabledReasons = new HashSet();

   MethodMutationContext(ClassContext classContext, Location location) {
      this.classContext = classContext;
      this.location = location;
   }

   public MutationIdentifier registerMutation(MethodMutatorFactory factory, String description) {
      MutationIdentifier newId = this.getNextMutationIdentifer(factory, this.classContext.getJavaClassName());
      MutationDetails details = new MutationDetails(newId, this.classContext.getFileName(), description, this.lastLineNumber, this.classContext.getCurrentBlock(), this.classContext.isWithinFinallyBlock(), false);
      this.registerMutation(details);
      return newId;
   }

   private MutationIdentifier getNextMutationIdentifer(MethodMutatorFactory factory, String className) {
      return new MutationIdentifier(this.location, this.instructionIndex, factory.getGloballyUniqueId());
   }

   private void registerMutation(MutationDetails details) {
      if (!this.isMutationFindingDisabled()) {
         this.classContext.addMutation(details);
      }

   }

   private boolean isMutationFindingDisabled() {
      return !this.mutationFindingDisabledReasons.isEmpty();
   }

   public void registerCurrentLine(int line) {
      this.lastLineNumber = line;
   }

   public void registerNewBlock() {
      this.classContext.registerNewBlock();
   }

   public void registerFinallyBlockStart() {
      this.classContext.registerFinallyBlockStart();
   }

   public void registerFinallyBlockEnd() {
      this.classContext.registerFinallyBlockEnd();
   }

   public ClassInfo getClassInfo() {
      return this.classContext.getClassInfo();
   }

   public boolean shouldMutate(MutationIdentifier newId) {
      return this.classContext.shouldMutate(newId);
   }

   public void disableMutations(String reason) {
      this.mutationFindingDisabledReasons.add(reason);
   }

   public void enableMutatations(String reason) {
      this.mutationFindingDisabledReasons.remove(reason);
   }

   public void increment() {
      ++this.instructionIndex;
   }

   public int currentInstructionCount() {
      return this.instructionIndex;
   }
}

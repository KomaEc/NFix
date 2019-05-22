package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.blocks.BlockCounter;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.blocks.ConcreteBlockCounter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ClassContext implements BlockCounter {
   private ClassInfo classInfo;
   private String sourceFile;
   private Option<MutationIdentifier> target = Option.none();
   private final List<MutationDetails> mutations = new ArrayList();
   private final ConcreteBlockCounter blockCounter = new ConcreteBlockCounter();

   public Option<MutationIdentifier> getTargetMutation() {
      return this.target;
   }

   public ClassInfo getClassInfo() {
      return this.classInfo;
   }

   public String getJavaClassName() {
      return this.classInfo.getName().replace("/", ".");
   }

   public String getFileName() {
      return this.sourceFile;
   }

   public void setTargetMutation(Option<MutationIdentifier> target) {
      this.target = target;
   }

   public List<MutationDetails> getMutationDetails(MutationIdentifier id) {
      return FCollection.filter(this.mutations, hasId(id));
   }

   private static F<MutationDetails, Boolean> hasId(final MutationIdentifier id) {
      return new F<MutationDetails, Boolean>() {
         public Boolean apply(MutationDetails a) {
            return a.matchesId(id);
         }
      };
   }

   public void registerClass(ClassInfo classInfo) {
      this.classInfo = classInfo;
   }

   public void registerSourceFile(String source) {
      this.sourceFile = source;
   }

   public boolean shouldMutate(MutationIdentifier newId) {
      return this.getTargetMutation().contains(idMatches(newId));
   }

   private static F<MutationIdentifier, Boolean> idMatches(final MutationIdentifier newId) {
      return new F<MutationIdentifier, Boolean>() {
         public Boolean apply(MutationIdentifier a) {
            return a.matches(newId);
         }
      };
   }

   public Collection<MutationDetails> getCollectedMutations() {
      return this.mutations;
   }

   public void addMutation(MutationDetails details) {
      this.mutations.add(details);
   }

   public void registerNewBlock() {
      this.blockCounter.registerNewBlock();
   }

   public void registerFinallyBlockStart() {
      this.blockCounter.registerFinallyBlockStart();
   }

   public void registerFinallyBlockEnd() {
      this.blockCounter.registerFinallyBlockEnd();
   }

   public int getCurrentBlock() {
      return this.blockCounter.getCurrentBlock();
   }

   public boolean isWithinFinallyBlock() {
      return this.blockCounter.isWithinFinallyBlock();
   }
}

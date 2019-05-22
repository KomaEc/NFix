package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.bytecode.FrameOptions;
import com.gzoltar.shaded.org.pitest.bytecode.NullVisitor;
import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.ComputeClassWriter;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.FunctionalList;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutant;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutater;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.InlinedCodeFilter;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassWriter;
import com.gzoltar.shaded.org.pitest.util.Functions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GregorMutater implements Mutater {
   private final Map<String, String> computeCache = new HashMap();
   private final Predicate<MethodInfo> filter;
   private final ClassByteArraySource byteSource;
   private final Set<MethodMutatorFactory> mutators = new HashSet();
   private final Set<String> loggingClasses = new HashSet();
   private final InlinedCodeFilter inlinedCodeDetector;

   public GregorMutater(ClassByteArraySource byteSource, Predicate<MethodInfo> filter, Collection<MethodMutatorFactory> mutators, Collection<String> loggingClasses, InlinedCodeFilter inlinedCodeDetector) {
      this.filter = filter;
      this.mutators.addAll(mutators);
      this.byteSource = byteSource;
      this.loggingClasses.addAll(FCollection.map(loggingClasses, Functions.classNameToJVMClassName()));
      this.inlinedCodeDetector = inlinedCodeDetector;
   }

   public FunctionalList<MutationDetails> findMutations(ClassName classToMutate) {
      ClassContext context = new ClassContext();
      context.setTargetMutation(Option.none());
      return this.byteSource.getBytes(classToMutate.asInternalName()).flatMap(this.findMutations(context));
   }

   private F<byte[], Iterable<MutationDetails>> findMutations(final ClassContext context) {
      return new F<byte[], Iterable<MutationDetails>>() {
         public Iterable<MutationDetails> apply(byte[] bytes) {
            return GregorMutater.this.findMutationsForBytes(context, bytes);
         }
      };
   }

   private Collection<MutationDetails> findMutationsForBytes(ClassContext context, byte[] classToMutate) {
      PremutationClassInfo classInfo = this.performPreScan(classToMutate);
      ClassReader first = new ClassReader(classToMutate);
      NullVisitor nv = new NullVisitor();
      MutatingClassVisitor mca = new MutatingClassVisitor(nv, context, this.filterMethods(), classInfo, this.mutators);
      first.accept(mca, 8);
      return this.inlinedCodeDetector.process(context.getCollectedMutations());
   }

   private PremutationClassInfo performPreScan(byte[] classToMutate) {
      ClassReader reader = new ClassReader(classToMutate);
      PreMutationAnalyser an = new PreMutationAnalyser(this.loggingClasses);
      reader.accept(an, 0);
      return an.getClassInfo();
   }

   public Mutant getMutation(MutationIdentifier id) {
      ClassContext context = new ClassContext();
      context.setTargetMutation(Option.some(id));
      Option<byte[]> bytes = this.byteSource.getBytes(id.getClassName().asJavaName());
      PremutationClassInfo classInfo = this.performPreScan((byte[])bytes.value());
      ClassReader reader = new ClassReader((byte[])bytes.value());
      ClassWriter w = new ComputeClassWriter(this.byteSource, this.computeCache, FrameOptions.pickFlags((byte[])bytes.value()));
      MutatingClassVisitor mca = new MutatingClassVisitor(w, context, this.filterMethods(), classInfo, FCollection.filter(this.mutators, isMutatorFor(id)));
      reader.accept(mca, 8);
      List<MutationDetails> details = context.getMutationDetails((MutationIdentifier)context.getTargetMutation().value());
      return new Mutant((MutationDetails)details.get(0), w.toByteArray());
   }

   private static Predicate<MethodMutatorFactory> isMutatorFor(final MutationIdentifier id) {
      return new Predicate<MethodMutatorFactory>() {
         public Boolean apply(MethodMutatorFactory a) {
            return id.getMutator().equals(a.getGloballyUniqueId());
         }
      };
   }

   private Predicate<MethodInfo> filterMethods() {
      return Prelude.and(this.filter, filterSyntheticMethods(), Prelude.not(isGeneratedEnumMethod()), Prelude.not(isGroovyClass()));
   }

   private static F<MethodInfo, Boolean> isGroovyClass() {
      return new Predicate<MethodInfo>() {
         public Boolean apply(MethodInfo a) {
            return a.isInGroovyClass();
         }
      };
   }

   private static Predicate<MethodInfo> filterSyntheticMethods() {
      return new Predicate<MethodInfo>() {
         public Boolean apply(MethodInfo a) {
            return !a.isSynthetic() || a.getName().startsWith("lambda$");
         }
      };
   }

   private static Predicate<MethodInfo> isGeneratedEnumMethod() {
      return new Predicate<MethodInfo>() {
         public Boolean apply(MethodInfo a) {
            return a.isGeneratedEnumMethod();
         }
      };
   }
}

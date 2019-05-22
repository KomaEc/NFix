package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.config;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.ArgumentPropagationMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.ConditionalsBoundaryMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.ConstructorCallMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.IncrementsMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.InlineConstantMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.InvertNegsMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.MathMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.NegateConditionalsMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.NonVoidMethodCallMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.RemoveConditionalMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.ReturnValsMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.VoidMethodCallMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental.MemberVariableMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental.RemoveIncrementsMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental.RemoveSwitchMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental.SwitchMutator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class Mutator {
   private static final Map<String, Iterable<MethodMutatorFactory>> MUTATORS = new LinkedHashMap();

   public static Collection<MethodMutatorFactory> all() {
      return fromStrings(MUTATORS.keySet());
   }

   private static Collection<MethodMutatorFactory> stronger() {
      return combine(defaults(), group(new RemoveConditionalMutator(RemoveConditionalMutator.Choice.EQUAL, false), new SwitchMutator()));
   }

   private static Collection<MethodMutatorFactory> combine(Collection<MethodMutatorFactory> a, Collection<MethodMutatorFactory> b) {
      List<MethodMutatorFactory> l = new ArrayList(a);
      l.addAll(b);
      return l;
   }

   public static Collection<MethodMutatorFactory> defaults() {
      return group(InvertNegsMutator.INVERT_NEGS_MUTATOR, ReturnValsMutator.RETURN_VALS_MUTATOR, MathMutator.MATH_MUTATOR, VoidMethodCallMutator.VOID_METHOD_CALL_MUTATOR, NegateConditionalsMutator.NEGATE_CONDITIONALS_MUTATOR, ConditionalsBoundaryMutator.CONDITIONALS_BOUNDARY_MUTATOR, IncrementsMutator.INCREMENTS_MUTATOR);
   }

   private static Collection<MethodMutatorFactory> group(MethodMutatorFactory... ms) {
      return Arrays.asList(ms);
   }

   public static Collection<MethodMutatorFactory> byName(String name) {
      return FCollection.map((Iterable)MUTATORS.get(name), Prelude.id(MethodMutatorFactory.class));
   }

   private static void add(String key, MethodMutatorFactory value) {
      MUTATORS.put(key, Collections.singleton(value));
   }

   private static void addGroup(String key, Iterable<MethodMutatorFactory> value) {
      MUTATORS.put(key, value);
   }

   public static Collection<MethodMutatorFactory> fromStrings(Collection<String> names) {
      Set<MethodMutatorFactory> unique = new TreeSet(compareId());
      FCollection.flatMapTo(names, fromString(), unique);
      return unique;
   }

   private static Comparator<? super MethodMutatorFactory> compareId() {
      return new Comparator<MethodMutatorFactory>() {
         public int compare(MethodMutatorFactory o1, MethodMutatorFactory o2) {
            return o1.getGloballyUniqueId().compareTo(o2.getGloballyUniqueId());
         }
      };
   }

   private static F<String, Iterable<MethodMutatorFactory>> fromString() {
      return new F<String, Iterable<MethodMutatorFactory>>() {
         public Iterable<MethodMutatorFactory> apply(String a) {
            Iterable<MethodMutatorFactory> i = (Iterable)Mutator.MUTATORS.get(a);
            if (i == null) {
               throw new PitHelpError(Help.UNKNOWN_MUTATOR, new Object[]{a});
            } else {
               return i;
            }
         }
      };
   }

   static {
      add("INVERT_NEGS", InvertNegsMutator.INVERT_NEGS_MUTATOR);
      add("RETURN_VALS", ReturnValsMutator.RETURN_VALS_MUTATOR);
      add("INLINE_CONSTS", new InlineConstantMutator());
      add("MATH", MathMutator.MATH_MUTATOR);
      add("VOID_METHOD_CALLS", VoidMethodCallMutator.VOID_METHOD_CALL_MUTATOR);
      add("NEGATE_CONDITIONALS", NegateConditionalsMutator.NEGATE_CONDITIONALS_MUTATOR);
      add("CONDITIONALS_BOUNDARY", ConditionalsBoundaryMutator.CONDITIONALS_BOUNDARY_MUTATOR);
      add("INCREMENTS", IncrementsMutator.INCREMENTS_MUTATOR);
      add("REMOVE_INCREMENTS", RemoveIncrementsMutator.REMOVE_INCREMENTS_MUTATOR);
      add("NON_VOID_METHOD_CALLS", NonVoidMethodCallMutator.NON_VOID_METHOD_CALL_MUTATOR);
      add("CONSTRUCTOR_CALLS", ConstructorCallMutator.CONSTRUCTOR_CALL_MUTATOR);
      add("REMOVE_CONDITIONALS_EQ_IF", new RemoveConditionalMutator(RemoveConditionalMutator.Choice.EQUAL, true));
      add("REMOVE_CONDITIONALS_EQ_ELSE", new RemoveConditionalMutator(RemoveConditionalMutator.Choice.EQUAL, false));
      add("REMOVE_CONDITIONALS_ORD_IF", new RemoveConditionalMutator(RemoveConditionalMutator.Choice.ORDER, true));
      add("REMOVE_CONDITIONALS_ORD_ELSE", new RemoveConditionalMutator(RemoveConditionalMutator.Choice.ORDER, false));
      addGroup("REMOVE_CONDITIONALS", RemoveConditionalMutator.makeMutators());
      add("EXPERIMENTAL_MEMBER_VARIABLE", new MemberVariableMutator());
      add("EXPERIMENTAL_SWITCH", new SwitchMutator());
      add("EXPERIMENTAL_ARGUMENT_PROPAGATION", ArgumentPropagationMutator.ARGUMENT_PROPAGATION_MUTATOR);
      addGroup("REMOVE_SWITCH", RemoveSwitchMutator.makeMutators());
      addGroup("DEFAULTS", defaults());
      addGroup("STRONGER", stronger());
      addGroup("ALL", all());
   }
}

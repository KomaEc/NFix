package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.FunctionalList;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class InlinedFinallyBlockDetector implements InlinedCodeFilter {
   private static final Logger LOG = Log.getLogger();

   public Collection<MutationDetails> process(Collection<MutationDetails> mutations) {
      List<MutationDetails> combined = new ArrayList(mutations.size());
      Map<LineMutatorPair, Collection<MutationDetails>> mutatorLinebuckets = FCollection.bucket(mutations, toLineMutatorPair());
      Iterator i$ = mutatorLinebuckets.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<LineMutatorPair, Collection<MutationDetails>> each = (Entry)i$.next();
         if (((Collection)each.getValue()).size() > 1) {
            this.checkForInlinedCode(combined, each);
         } else {
            combined.addAll((Collection)each.getValue());
         }
      }

      Collections.sort(combined, compareLineNumbers());
      return combined;
   }

   private static Comparator<MutationDetails> compareLineNumbers() {
      return new Comparator<MutationDetails>() {
         public int compare(MutationDetails arg0, MutationDetails arg1) {
            return arg0.getLineNumber() - arg1.getLineNumber();
         }
      };
   }

   private void checkForInlinedCode(Collection<MutationDetails> combined, Entry<LineMutatorPair, Collection<MutationDetails>> each) {
      FunctionalList<MutationDetails> mutationsInHandlerBlock = FCollection.filter((Iterable)each.getValue(), isInFinallyHandler());
      if (!this.isPossibleToCorrectInlining(mutationsInHandlerBlock)) {
         combined.addAll((Collection)each.getValue());
      } else {
         MutationDetails baseMutation = (MutationDetails)mutationsInHandlerBlock.get(0);
         int firstBlock = baseMutation.getBlock();
         FunctionalList<Integer> ids = FCollection.map((Iterable)each.getValue(), mutationToBlock());
         if (ids.contains(Prelude.not(Prelude.isEqualTo(firstBlock)))) {
            combined.add(makeCombinedMutant((Collection)each.getValue()));
         } else {
            combined.addAll((Collection)each.getValue());
         }

      }
   }

   private boolean isPossibleToCorrectInlining(List<MutationDetails> mutationsInHandlerBlock) {
      if (mutationsInHandlerBlock.size() > 1) {
         LOG.warning("Found more than one mutation similar on same line in a finally block. Can't correct for inlining.");
         return false;
      } else {
         return !mutationsInHandlerBlock.isEmpty();
      }
   }

   private static F<MutationDetails, Boolean> isInFinallyHandler() {
      return new F<MutationDetails, Boolean>() {
         public Boolean apply(MutationDetails a) {
            return a.isInFinallyBlock();
         }
      };
   }

   private static MutationDetails makeCombinedMutant(Collection<MutationDetails> value) {
      MutationDetails first = (MutationDetails)value.iterator().next();
      Set<Integer> indexes = new HashSet();
      FCollection.mapTo(value, mutationToIndex(), indexes);
      MutationIdentifier id = new MutationIdentifier(first.getId().getLocation(), indexes, first.getId().getMutator());
      return new MutationDetails(id, first.getFilename(), first.getDescription(), first.getLineNumber(), first.getBlock());
   }

   private static F<MutationDetails, Integer> mutationToIndex() {
      return new F<MutationDetails, Integer>() {
         public Integer apply(MutationDetails a) {
            return a.getFirstIndex();
         }
      };
   }

   private static F<MutationDetails, Integer> mutationToBlock() {
      return new F<MutationDetails, Integer>() {
         public Integer apply(MutationDetails a) {
            return a.getBlock();
         }
      };
   }

   private static F<MutationDetails, LineMutatorPair> toLineMutatorPair() {
      return new F<MutationDetails, LineMutatorPair>() {
         public LineMutatorPair apply(MutationDetails a) {
            return new LineMutatorPair(a.getLineNumber(), a.getMutator());
         }
      };
   }
}

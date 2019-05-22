package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.mutationtest.ListenerArguments;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import java.util.Properties;

public class CompoundListenerFactory implements MutationResultListenerFactory {
   private final Iterable<MutationResultListenerFactory> children;

   public CompoundListenerFactory(Iterable<MutationResultListenerFactory> children) {
      this.children = children;
   }

   public MutationResultListener getListener(Properties props, ListenerArguments args) {
      return new CompoundTestListener(FCollection.map(this.children, this.factoryToListener(props, args)));
   }

   private F<MutationResultListenerFactory, MutationResultListener> factoryToListener(final Properties props, final ListenerArguments args) {
      return new F<MutationResultListenerFactory, MutationResultListener>() {
         public MutationResultListener apply(MutationResultListenerFactory a) {
            return a.getListener(props, args);
         }
      };
   }

   public String name() {
      throw new UnsupportedOperationException();
   }

   public String description() {
      throw new UnsupportedOperationException();
   }
}

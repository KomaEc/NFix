package com.google.inject.spi;

import com.google.inject.Binding;
import java.util.List;

public interface ProvisionListener {
   <T> void onProvision(ProvisionListener.ProvisionInvocation<T> var1);

   public abstract static class ProvisionInvocation<T> {
      public abstract Binding<T> getBinding();

      public abstract T provision();

      public abstract List<DependencyAndSource> getDependencyChain();
   }
}

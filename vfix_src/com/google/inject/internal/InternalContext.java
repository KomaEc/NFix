package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.DependencyAndSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class InternalContext {
   private final InjectorImpl.InjectorOptions options;
   private Map<Object, ConstructionContext<?>> constructionContexts = Maps.newHashMap();
   private Dependency<?> dependency;
   private final InternalContext.DependencyStack state = new InternalContext.DependencyStack();

   InternalContext(InjectorImpl.InjectorOptions options) {
      this.options = options;
   }

   public InjectorImpl.InjectorOptions getInjectorOptions() {
      return this.options;
   }

   public <T> ConstructionContext<T> getConstructionContext(Object key) {
      ConstructionContext<T> constructionContext = (ConstructionContext)this.constructionContexts.get(key);
      if (constructionContext == null) {
         constructionContext = new ConstructionContext();
         this.constructionContexts.put(key, constructionContext);
      }

      return constructionContext;
   }

   public Dependency<?> getDependency() {
      return this.dependency;
   }

   public Dependency<?> pushDependency(Dependency<?> dependency, Object source) {
      Dependency<?> previous = this.dependency;
      this.dependency = dependency;
      this.state.add(dependency, source);
      return previous;
   }

   public void popStateAndSetDependency(Dependency<?> newDependency) {
      this.state.pop();
      this.dependency = newDependency;
   }

   public void pushState(Key<?> key, Object source) {
      this.state.add(key, source);
   }

   public void popState() {
      this.state.pop();
   }

   public List<DependencyAndSource> getDependencyChain() {
      ImmutableList.Builder<DependencyAndSource> builder = ImmutableList.builder();

      for(int i = 0; i < this.state.size(); i += 2) {
         Object evenEntry = this.state.get(i);
         Dependency dependency;
         if (evenEntry instanceof Key) {
            dependency = Dependency.get((Key)evenEntry);
         } else {
            dependency = (Dependency)evenEntry;
         }

         builder.add((Object)(new DependencyAndSource(dependency, this.state.get(i + 1))));
      }

      return builder.build();
   }

   private static final class DependencyStack {
      private Object[] elements;
      private int size;

      private DependencyStack() {
         this.elements = new Object[16];
         this.size = 0;
      }

      public void add(Object dependencyOrKey, Object source) {
         if (this.elements.length < this.size + 2) {
            this.elements = Arrays.copyOf(this.elements, this.elements.length * 3 / 2 + 2);
         }

         this.elements[this.size++] = dependencyOrKey;
         this.elements[this.size++] = source;
      }

      public void pop() {
         this.elements[--this.size] = null;
         this.elements[--this.size] = null;
      }

      public Object get(int i) {
         return this.elements[i];
      }

      public int size() {
         return this.size;
      }

      // $FF: synthetic method
      DependencyStack(Object x0) {
         this();
      }
   }
}

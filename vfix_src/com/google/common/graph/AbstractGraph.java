package com.google.common.graph;

import com.google.common.annotations.Beta;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractGraph<N> extends AbstractBaseGraph<N> implements Graph<N> {
   public final boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Graph)) {
         return false;
      } else {
         Graph<?> other = (Graph)obj;
         return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && this.edges().equals(other.edges());
      }
   }

   public final int hashCode() {
      return this.edges().hashCode();
   }

   public String toString() {
      return "isDirected: " + this.isDirected() + ", allowsSelfLoops: " + this.allowsSelfLoops() + ", nodes: " + this.nodes() + ", edges: " + this.edges();
   }
}

package com.gzoltar.shaded.org.jacoco.core.data;

import java.util.Arrays;

public final class ExecutionData {
   private final long id;
   private final String name;
   private final boolean[] probes;

   public ExecutionData(long id, String name, boolean[] probes) {
      this.id = id;
      this.name = name;
      this.probes = probes;
   }

   public ExecutionData(long id, String name, int probeCount) {
      this.id = id;
      this.name = name;
      this.probes = new boolean[probeCount];
   }

   public long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public boolean[] getProbes() {
      return this.probes;
   }

   public void reset() {
      Arrays.fill(this.probes, false);
   }

   public boolean hasHits() {
      boolean[] arr$ = this.probes;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         boolean p = arr$[i$];
         if (p) {
            return true;
         }
      }

      return false;
   }

   public void merge(ExecutionData other) {
      this.merge(other, true);
   }

   public void merge(ExecutionData other, boolean flag) {
      this.assertCompatibility(other.getId(), other.getName(), other.getProbes().length);
      boolean[] otherData = other.getProbes();

      for(int i = 0; i < this.probes.length; ++i) {
         if (otherData[i]) {
            this.probes[i] = flag;
         }
      }

   }

   public void assertCompatibility(long id, String name, int probecount) throws IllegalStateException {
      if (this.id != id) {
         throw new IllegalStateException(String.format("Different ids (%016x and %016x).", this.id, id));
      } else if (!this.name.equals(name)) {
         throw new IllegalStateException(String.format("Different class names %s and %s for id %016x.", this.name, name, id));
      } else if (this.probes.length != probecount) {
         throw new IllegalStateException(String.format("Incompatible execution data for class %s with id %016x.", name, id));
      }
   }

   public String toString() {
      return String.format("ExecutionData[name=%s, id=%016x]", this.name, this.id);
   }
}

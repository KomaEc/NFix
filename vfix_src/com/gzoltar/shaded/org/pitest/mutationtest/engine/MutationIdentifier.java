package com.gzoltar.shaded.org.pitest.mutationtest.engine;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class MutationIdentifier implements Comparable<MutationIdentifier> {
   private final Location location;
   private final List<Integer> indexes;
   private final String mutator;

   public MutationIdentifier(Location location, int index, String mutatorUniqueId) {
      this(location, Collections.singleton(index), mutatorUniqueId);
   }

   public MutationIdentifier(Location location, Collection<Integer> indexes, String mutatorUniqueId) {
      this.location = location;
      this.indexes = new ArrayList(indexes);
      this.mutator = mutatorUniqueId;
   }

   public Location getLocation() {
      return this.location;
   }

   public String getMutator() {
      return this.mutator;
   }

   public int getFirstIndex() {
      return (Integer)this.indexes.iterator().next();
   }

   public String toString() {
      return "MutationIdentifier [location=" + this.location + ", indexes=" + this.indexes + ", mutator=" + this.mutator + "]";
   }

   public boolean matches(MutationIdentifier id) {
      return this.location.equals(id.location) && this.mutator.equals(id.mutator) && this.indexes.contains(id.getFirstIndex());
   }

   public ClassName getClassName() {
      return this.location.getClassName();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.indexes == null ? 0 : this.indexes.hashCode());
      result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
      result = 31 * result + (this.mutator == null ? 0 : this.mutator.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         MutationIdentifier other = (MutationIdentifier)obj;
         if (this.indexes == null) {
            if (other.indexes != null) {
               return false;
            }
         } else if (!this.indexes.equals(other.indexes)) {
            return false;
         }

         if (this.location == null) {
            if (other.location != null) {
               return false;
            }
         } else if (!this.location.equals(other.location)) {
            return false;
         }

         if (this.mutator == null) {
            if (other.mutator != null) {
               return false;
            }
         } else if (!this.mutator.equals(other.mutator)) {
            return false;
         }

         return true;
      }
   }

   public int compareTo(MutationIdentifier other) {
      int comp = this.location.compareTo(other.getLocation());
      if (comp != 0) {
         return comp;
      } else {
         comp = this.mutator.compareTo(other.getMutator());
         return comp != 0 ? comp : ((Integer)this.indexes.get(0)).compareTo((Integer)other.indexes.get(0));
      }
   }
}

package heros.fieldsens;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AccessPath<T> {
   private final T[] accesses;
   private final Set<T> exclusions;

   public static <T> AccessPath<T> empty() {
      return new AccessPath();
   }

   public AccessPath() {
      this.accesses = (Object[])(new Object[0]);
      this.exclusions = Sets.newHashSet();
   }

   AccessPath(T[] accesses, Set<T> exclusions) {
      this.accesses = accesses;
      this.exclusions = exclusions;
   }

   public boolean isAccessInExclusions(T fieldReference) {
      return this.exclusions.contains(fieldReference);
   }

   public boolean hasAllExclusionsOf(AccessPath<T> accPath) {
      return this.exclusions.containsAll(accPath.exclusions);
   }

   public AccessPath<T> append(T... fieldReferences) {
      if (fieldReferences.length == 0) {
         return this;
      } else if (this.isAccessInExclusions(fieldReferences[0])) {
         throw new IllegalArgumentException("FieldRef " + Arrays.toString(fieldReferences) + " cannot be added to " + this.toString());
      } else {
         T[] newAccesses = Arrays.copyOf(this.accesses, this.accesses.length + fieldReferences.length);
         System.arraycopy(fieldReferences, 0, newAccesses, this.accesses.length, fieldReferences.length);
         return new AccessPath(newAccesses, Sets.newHashSet());
      }
   }

   public AccessPath<T> prepend(T fieldRef) {
      T[] newAccesses = (Object[])(new Object[this.accesses.length + 1]);
      newAccesses[0] = fieldRef;
      System.arraycopy(this.accesses, 0, newAccesses, 1, this.accesses.length);
      return new AccessPath(newAccesses, this.exclusions);
   }

   public AccessPath<T> removeFirst() {
      T[] newAccesses = (Object[])(new Object[this.accesses.length - 1]);
      System.arraycopy(this.accesses, 1, newAccesses, 0, this.accesses.length - 1);
      return new AccessPath(newAccesses, this.exclusions);
   }

   public AccessPath<T> appendExcludedFieldReference(Collection<T> fieldReferences) {
      HashSet<T> newExclusions = Sets.newHashSet((Iterable)fieldReferences);
      newExclusions.addAll(this.exclusions);
      return new AccessPath(this.accesses, newExclusions);
   }

   public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {
      HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
      newExclusions.addAll(this.exclusions);
      return new AccessPath(this.accesses, newExclusions);
   }

   public AccessPath.PrefixTestResult isPrefixOf(AccessPath<T> accessPath) {
      if (this.accesses.length > accessPath.accesses.length) {
         return AccessPath.PrefixTestResult.NO_PREFIX;
      } else {
         for(int i = 0; i < this.accesses.length; ++i) {
            if (!this.accesses[i].equals(accessPath.accesses[i])) {
               return AccessPath.PrefixTestResult.NO_PREFIX;
            }
         }

         if (this.accesses.length < accessPath.accesses.length) {
            return this.exclusions.contains(accessPath.accesses[this.accesses.length]) ? AccessPath.PrefixTestResult.NO_PREFIX : AccessPath.PrefixTestResult.GUARANTEED_PREFIX;
         } else if (this.exclusions.isEmpty()) {
            return AccessPath.PrefixTestResult.GUARANTEED_PREFIX;
         } else if (accessPath.exclusions.isEmpty()) {
            return AccessPath.PrefixTestResult.NO_PREFIX;
         } else {
            boolean intersection = !Sets.intersection(this.exclusions, accessPath.exclusions).isEmpty();
            boolean containsAll = this.exclusions.containsAll(accessPath.exclusions);
            boolean oppositeContainsAll = accessPath.exclusions.containsAll(this.exclusions);
            boolean potentialMatch = oppositeContainsAll || !intersection || !containsAll && !oppositeContainsAll;
            if (potentialMatch) {
               return oppositeContainsAll ? AccessPath.PrefixTestResult.GUARANTEED_PREFIX : AccessPath.PrefixTestResult.POTENTIAL_PREFIX;
            } else {
               return AccessPath.PrefixTestResult.NO_PREFIX;
            }
         }
      }
   }

   public AccessPath.Delta<T> getDeltaTo(AccessPath<T> accPath) {
      assert this.isPrefixOf(accPath).atLeast(AccessPath.PrefixTestResult.POTENTIAL_PREFIX);

      HashSet<T> mergedExclusions = Sets.newHashSet((Iterable)accPath.exclusions);
      if (this.accesses.length == accPath.accesses.length) {
         mergedExclusions.addAll(this.exclusions);
      }

      AccessPath.Delta<T> delta = new AccessPath.Delta(Arrays.copyOfRange(accPath.accesses, this.accesses.length, accPath.accesses.length), mergedExclusions);

      assert this.isPrefixOf(accPath).atLeast(AccessPath.PrefixTestResult.POTENTIAL_PREFIX) && accPath.isPrefixOf(delta.applyTo(this)) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX || this.isPrefixOf(accPath) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX && accPath.equals(delta.applyTo(this));

      return delta;
   }

   public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {
      HashSet<T> newExclusions = Sets.newHashSet((Iterable)this.exclusions);
      newExclusions.addAll(accPath.exclusions);
      return new AccessPath(this.accesses, newExclusions);
   }

   public boolean canRead(T field) {
      return this.accesses.length > 0 && this.accesses[0].equals(field);
   }

   public boolean isEmpty() {
      return this.exclusions.isEmpty() && this.accesses.length == 0;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + Arrays.hashCode(this.accesses);
      result = 31 * result + (this.exclusions == null ? 0 : this.exclusions.hashCode());
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
         AccessPath other = (AccessPath)obj;
         if (!Arrays.equals(this.accesses, other.accesses)) {
            return false;
         } else {
            if (this.exclusions == null) {
               if (other.exclusions != null) {
                  return false;
               }
            } else if (!this.exclusions.equals(other.exclusions)) {
               return false;
            }

            return true;
         }
      }
   }

   public String toString() {
      String result = this.accesses.length > 0 ? "." + Joiner.on(".").join(this.accesses) : "";
      if (!this.exclusions.isEmpty()) {
         result = result + "^" + Joiner.on(",").join((Iterable)this.exclusions);
      }

      return result;
   }

   public AccessPath<T> removeAnyAccess() {
      return this.accesses.length > 0 ? new AccessPath((Object[])(new Object[0]), this.exclusions) : this;
   }

   public boolean hasEmptyAccessPath() {
      return this.accesses.length == 0;
   }

   public T getFirstAccess() {
      return this.accesses[0];
   }

   Set<T> getExclusions() {
      return this.exclusions;
   }

   public static class Delta<T> {
      final T[] accesses;
      final Set<T> exclusions;

      protected Delta(T[] accesses, Set<T> exclusions) {
         this.accesses = accesses;
         this.exclusions = exclusions;
      }

      public boolean canBeAppliedTo(AccessPath<T> accPath) {
         if (this.accesses.length > 0) {
            return !accPath.isAccessInExclusions(this.accesses[0]);
         } else {
            return true;
         }
      }

      public AccessPath<T> applyTo(AccessPath<T> accPath) {
         return accPath.append(this.accesses).appendExcludedFieldReference((Collection)this.exclusions);
      }

      public String toString() {
         String result = this.accesses.length > 0 ? "." + Joiner.on(".").join(this.accesses) : "";
         if (!this.exclusions.isEmpty()) {
            result = result + "^" + Joiner.on(",").join((Iterable)this.exclusions);
         }

         return result;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + Arrays.hashCode(this.accesses);
         result = 31 * result + (this.exclusions == null ? 0 : this.exclusions.hashCode());
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
            AccessPath.Delta other = (AccessPath.Delta)obj;
            if (!Arrays.equals(this.accesses, other.accesses)) {
               return false;
            } else {
               if (this.exclusions == null) {
                  if (other.exclusions != null) {
                     return false;
                  }
               } else if (!this.exclusions.equals(other.exclusions)) {
                  return false;
               }

               return true;
            }
         }
      }

      public static <T> AccessPath.Delta<T> empty() {
         return new AccessPath.Delta((Object[])(new Object[0]), Sets.newHashSet());
      }
   }

   public static enum PrefixTestResult {
      GUARANTEED_PREFIX(2),
      POTENTIAL_PREFIX(1),
      NO_PREFIX(0);

      private int value;

      private PrefixTestResult(int value) {
         this.value = value;
      }

      public boolean atLeast(AccessPath.PrefixTestResult minimum) {
         return this.value >= minimum.value;
      }
   }
}

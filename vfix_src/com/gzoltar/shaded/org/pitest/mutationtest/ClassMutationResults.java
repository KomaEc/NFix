package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import java.util.ArrayList;
import java.util.Collection;

public class ClassMutationResults {
   private final Collection<MutationResult> mutations = new ArrayList();

   public ClassMutationResults(Collection<MutationResult> mutations) {
      this.mutations.addAll(mutations);
   }

   public String getFileName() {
      return ((MutationResult)this.mutations.iterator().next()).getDetails().getFilename();
   }

   public Collection<MutationResult> getMutations() {
      return this.mutations;
   }

   public ClassName getMutatedClass() {
      return ((MutationResult)this.mutations.iterator().next()).getDetails().getClassName();
   }

   public String getPackageName() {
      ClassName name = this.getMutatedClass();
      int lastDot = name.asJavaName().lastIndexOf(46);
      return lastDot > 0 ? name.asJavaName().substring(0, lastDot) : "default";
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.mutations == null ? 0 : this.mutations.hashCode());
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
         ClassMutationResults other = (ClassMutationResults)obj;
         if (this.mutations == null) {
            if (other.mutations != null) {
               return false;
            }
         } else if (!this.mutations.equals(other.mutations)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return "ClassMutationResults [mutations=" + this.mutations + "]";
   }
}

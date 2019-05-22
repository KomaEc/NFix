package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class MutationMetaData {
   private final List<MutationResult> mutations;

   public MutationMetaData(List<MutationResult> mutations) {
      this.mutations = mutations;
   }

   public Collection<MutationResult> getMutations() {
      return this.mutations;
   }

   public Collection<ClassMutationResults> toClassResults() {
      Collections.sort(this.mutations, comparator());
      List<ClassMutationResults> cmrs = new ArrayList();
      List<MutationResult> buffer = new ArrayList();
      ClassName cn = null;
      Iterator i$ = this.mutations.iterator();

      while(i$.hasNext()) {
         MutationResult each = (MutationResult)i$.next();
         if (cn != null && !each.getDetails().getClassName().equals(cn)) {
            cmrs.add(new ClassMutationResults(buffer));
            buffer.clear();
         }

         cn = each.getDetails().getClassName();
         buffer.add(each);
      }

      if (!buffer.isEmpty()) {
         cmrs.add(new ClassMutationResults(buffer));
      }

      return cmrs;
   }

   private static Comparator<MutationResult> comparator() {
      return new Comparator<MutationResult>() {
         public int compare(MutationResult arg0, MutationResult arg1) {
            return arg0.getDetails().getId().compareTo(arg1.getDetails().getId());
         }
      };
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
         MutationMetaData other = (MutationMetaData)obj;
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
}

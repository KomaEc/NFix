package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import java.util.Iterator;

public class CompoundTestListener implements MutationResultListener {
   private final Iterable<MutationResultListener> children;

   public CompoundTestListener(Iterable<MutationResultListener> children) {
      this.children = children;
   }

   public void runStart() {
      Iterator i$ = this.children.iterator();

      while(i$.hasNext()) {
         MutationResultListener each = (MutationResultListener)i$.next();
         each.runStart();
      }

   }

   public void handleMutationResult(ClassMutationResults metaData) {
      Iterator i$ = this.children.iterator();

      while(i$.hasNext()) {
         MutationResultListener each = (MutationResultListener)i$.next();
         each.handleMutationResult(metaData);
      }

   }

   public void runEnd() {
      Iterator i$ = this.children.iterator();

      while(i$.hasNext()) {
         MutationResultListener each = (MutationResultListener)i$.next();
         each.runEnd();
      }

   }
}

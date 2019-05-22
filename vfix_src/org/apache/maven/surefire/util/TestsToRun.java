package org.apache.maven.surefire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.maven.surefire.testset.TestSetFailedException;

public class TestsToRun implements Iterable<Class> {
   private final List<Class> locatedClasses;

   public TestsToRun(List<Class> locatedClasses) {
      this.locatedClasses = Collections.unmodifiableList(locatedClasses);
      Set<Class> testSets = new HashSet();
      Iterator i$ = locatedClasses.iterator();

      while(i$.hasNext()) {
         Class testClass = (Class)i$.next();
         if (testSets.contains(testClass)) {
            throw new RuntimeException("Duplicate test set '" + testClass.getName() + "'");
         }

         testSets.add(testClass);
      }

   }

   public static TestsToRun fromClass(Class clazz) throws TestSetFailedException {
      return new TestsToRun(Arrays.asList(clazz));
   }

   public Iterator<Class> iterator() {
      return this.locatedClasses.iterator();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("TestsToRun: [");
      Iterator it = this.iterator();

      while(it.hasNext()) {
         Class clazz = (Class)it.next();
         sb.append(" ").append(clazz.getName());
      }

      sb.append(']');
      return sb.toString();
   }

   public boolean containsAtLeast(int atLeast) {
      return this.containsAtLeast(this.iterator(), atLeast);
   }

   private boolean containsAtLeast(Iterator it, int atLeast) {
      for(int i = 0; i < atLeast; ++i) {
         if (!it.hasNext()) {
            return false;
         }

         it.next();
      }

      return true;
   }

   public boolean containsExactly(int items) {
      Iterator it = this.iterator();
      return this.containsAtLeast(it, items) && !it.hasNext();
   }

   public boolean allowEagerReading() {
      return true;
   }

   public Class[] getLocatedClasses() {
      if (!this.allowEagerReading()) {
         throw new IllegalStateException("Cannot eagerly read");
      } else {
         List<Class> result = new ArrayList();
         Iterator it = this.iterator();

         while(it.hasNext()) {
            result.add(it.next());
         }

         return (Class[])result.toArray(new Class[result.size()]);
      }
   }
}

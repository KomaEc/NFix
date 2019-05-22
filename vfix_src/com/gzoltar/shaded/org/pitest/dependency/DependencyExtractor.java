package com.gzoltar.shaded.org.pitest.dependency;

import com.gzoltar.shaded.org.pitest.bytecode.NullVisitor;
import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.util.Functions;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class DependencyExtractor {
   private static final Logger LOG = Log.getLogger();
   private final int depth;
   private final ClassByteArraySource classToBytes;

   public DependencyExtractor(ClassByteArraySource classToBytes, int depth) {
      this.depth = depth;
      this.classToBytes = classToBytes;
   }

   public Collection<String> extractCallDependenciesForPackages(String clazz, Predicate<String> targetPackages) throws IOException {
      Set<String> allDependencies = this.extractCallDependencies(clazz, new IgnoreCoreClasses());
      return FCollection.filter(allDependencies, Prelude.and(asJVMNamePredicate(targetPackages), notSuppliedClass(clazz)));
   }

   private static F<String, Boolean> notSuppliedClass(final String clazz) {
      return new F<String, Boolean>() {
         public Boolean apply(String a) {
            return !((String)Functions.jvmClassToClassName().apply(a)).equals(clazz);
         }
      };
   }

   private static F<String, Boolean> asJVMNamePredicate(final Predicate<String> predicate) {
      return new F<String, Boolean>() {
         public Boolean apply(String a) {
            return (Boolean)predicate.apply(Functions.jvmClassToClassName().apply(a));
         }
      };
   }

   public Collection<String> extractCallDependenciesForPackages(String clazz, Predicate<String> targetPackages, Predicate<DependencyAccess> doNotTraverse) throws IOException {
      Set<String> allDependencies = this.extractCallDependencies(clazz, doNotTraverse);
      return FCollection.filter(allDependencies, targetPackages);
   }

   Set<String> extractCallDependencies(String clazz, Predicate<DependencyAccess> filter) throws IOException {
      return this.extractCallDependencies(clazz, new TreeSet(), filter, 0);
   }

   public int getMaxDistance() {
      return this.depth;
   }

   private Set<String> extractCallDependencies(String clazz, TreeSet<String> visited, Predicate<DependencyAccess> filter, int currentDepth) throws IOException {
      Map<String, List<DependencyAccess>> classesToAccesses = this.groupDependenciesByClass(this.extractRelevantDependencies(clazz, filter));
      Set<String> dependencies = new HashSet(classesToAccesses.keySet());
      dependencies.removeAll(visited);
      visited.addAll(dependencies);
      if (currentDepth < this.depth - 1 || this.depth == 0) {
         dependencies.addAll(this.examineChildDependencies(currentDepth, dependencies, visited, filter));
      }

      return dependencies;
   }

   private Set<String> examineChildDependencies(int currentDepth, Set<String> classes, TreeSet<String> visited, Predicate<DependencyAccess> filter) throws IOException {
      Set<String> deps = new HashSet();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         String each = (String)i$.next();
         Set<String> childDependencies = this.extractCallDependencies(each, visited, filter, currentDepth + 1);
         deps.addAll(childDependencies);
      }

      return deps;
   }

   private Set<DependencyAccess> extractRelevantDependencies(String clazz, Predicate<DependencyAccess> filter) throws IOException {
      List<DependencyAccess> dependencies = this.extract(clazz, filter);
      Set<DependencyAccess> relevantDependencies = new TreeSet(equalDestinationComparator());
      FCollection.filter(dependencies, filter, relevantDependencies);
      return relevantDependencies;
   }

   private static Comparator<DependencyAccess> equalDestinationComparator() {
      return new Comparator<DependencyAccess>() {
         public int compare(DependencyAccess o1, DependencyAccess o2) {
            return o1.getDest().compareTo(o2.getDest());
         }
      };
   }

   private List<DependencyAccess> extract(String clazz, Predicate<DependencyAccess> filter) throws IOException {
      Option<byte[]> bytes = this.classToBytes.getBytes(clazz);
      if (bytes.hasNone()) {
         LOG.warning("No bytes found for " + clazz);
         return Collections.emptyList();
      } else {
         ClassReader reader = new ClassReader((byte[])bytes.value());
         List<DependencyAccess> dependencies = new ArrayList();
         SideEffect1<DependencyAccess> se = constructCollectingSideEffectForVisitor(dependencies, Prelude.and(Prelude.not(nameIsEqual(clazz)), filter));
         DependencyClassVisitor dcv = new DependencyClassVisitor(new NullVisitor(), se);
         reader.accept(dcv, 8);
         return dependencies;
      }
   }

   private Map<String, List<DependencyAccess>> groupDependenciesByClass(Set<DependencyAccess> relevantDependencies) {
      List<DependencyAccess> sortedByClass = new ArrayList(relevantDependencies.size());
      Collections.sort(sortedByClass, classNameComparator());
      return (Map)FCollection.fold(addDependenciesToMap(), new HashMap(), relevantDependencies);
   }

   private static F2<Map<String, List<DependencyAccess>>, DependencyAccess, Map<String, List<DependencyAccess>>> addDependenciesToMap() {
      return new F2<Map<String, List<DependencyAccess>>, DependencyAccess, Map<String, List<DependencyAccess>>>() {
         public Map<String, List<DependencyAccess>> apply(Map<String, List<DependencyAccess>> map, DependencyAccess access) {
            List<DependencyAccess> list = (List)map.get(access.getDest().getOwner());
            if (list == null) {
               list = new ArrayList();
            }

            ((List)list).add(access);
            map.put(access.getDest().getOwner(), list);
            return map;
         }
      };
   }

   private static Comparator<DependencyAccess> classNameComparator() {
      return new Comparator<DependencyAccess>() {
         public int compare(DependencyAccess lhs, DependencyAccess rhs) {
            return lhs.getDest().getOwner().compareTo(rhs.getDest().getOwner());
         }
      };
   }

   private static Predicate<DependencyAccess> nameIsEqual(final String clazz) {
      return new Predicate<DependencyAccess>() {
         public Boolean apply(DependencyAccess a) {
            return a.getDest().getOwner().equals(clazz);
         }
      };
   }

   private static SideEffect1<DependencyAccess> constructCollectingSideEffectForVisitor(final List<DependencyAccess> dependencies, final Predicate<DependencyAccess> predicate) {
      SideEffect1<DependencyAccess> se = new SideEffect1<DependencyAccess>() {
         public void apply(DependencyAccess a) {
            if ((Boolean)predicate.apply(a)) {
               dependencies.add(a);
            }

         }
      };
      return se;
   }
}

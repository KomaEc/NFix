package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.CoverageNodeImpl;
import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BundleCoverageImpl extends CoverageNodeImpl implements IBundleCoverage {
   private final Collection<IPackageCoverage> packages;

   public BundleCoverageImpl(String name, Collection<IPackageCoverage> packages) {
      super(ICoverageNode.ElementType.BUNDLE, name);
      this.packages = packages;
      this.increment(packages);
   }

   public BundleCoverageImpl(String name, Collection<IClassCoverage> classes, Collection<ISourceFileCoverage> sourcefiles) {
      this(name, groupByPackage(classes, sourcefiles));
   }

   private static Collection<IPackageCoverage> groupByPackage(Collection<IClassCoverage> classes, Collection<ISourceFileCoverage> sourcefiles) {
      Map<String, Collection<IClassCoverage>> classesByPackage = new HashMap();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         IClassCoverage c = (IClassCoverage)i$.next();
         addByName(classesByPackage, c.getPackageName(), c);
      }

      Map<String, Collection<ISourceFileCoverage>> sourceFilesByPackage = new HashMap();
      Iterator i$ = sourcefiles.iterator();

      while(i$.hasNext()) {
         ISourceFileCoverage s = (ISourceFileCoverage)i$.next();
         addByName(sourceFilesByPackage, s.getPackageName(), s);
      }

      Set<String> packageNames = new HashSet();
      packageNames.addAll(classesByPackage.keySet());
      packageNames.addAll(sourceFilesByPackage.keySet());
      Collection<IPackageCoverage> result = new ArrayList();

      String name;
      Object c;
      Object s;
      for(Iterator i$ = packageNames.iterator(); i$.hasNext(); result.add(new PackageCoverageImpl(name, (Collection)c, (Collection)s))) {
         name = (String)i$.next();
         c = (Collection)classesByPackage.get(name);
         if (c == null) {
            c = Collections.emptyList();
         }

         s = (Collection)sourceFilesByPackage.get(name);
         if (s == null) {
            s = Collections.emptyList();
         }
      }

      return result;
   }

   private static <T> void addByName(Map<String, Collection<T>> map, String name, T value) {
      Collection<T> list = (Collection)map.get(name);
      if (list == null) {
         list = new ArrayList();
         map.put(name, list);
      }

      ((Collection)list).add(value);
   }

   public Collection<IPackageCoverage> getPackages() {
      return this.packages;
   }
}

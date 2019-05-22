package com.gzoltar.shaded.org.jacoco.core.analysis;

import com.gzoltar.shaded.org.jacoco.core.internal.analysis.BundleCoverageImpl;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.SourceFileCoverageImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CoverageBuilder implements ICoverageVisitor {
   private final Map<String, IClassCoverage> classes = new HashMap();
   private final Map<String, ISourceFileCoverage> sourcefiles = new HashMap();

   public Collection<IClassCoverage> getClasses() {
      return Collections.unmodifiableCollection(this.classes.values());
   }

   public Collection<ISourceFileCoverage> getSourceFiles() {
      return Collections.unmodifiableCollection(this.sourcefiles.values());
   }

   public IBundleCoverage getBundle(String name) {
      return new BundleCoverageImpl(name, this.classes.values(), this.sourcefiles.values());
   }

   public Collection<IClassCoverage> getNoMatchClasses() {
      Collection<IClassCoverage> result = new ArrayList();
      Iterator i$ = this.classes.values().iterator();

      while(i$.hasNext()) {
         IClassCoverage c = (IClassCoverage)i$.next();
         if (c.isNoMatch()) {
            result.add(c);
         }
      }

      return result;
   }

   public void visitCoverage(IClassCoverage coverage) {
      if (coverage.getInstructionCounter().getTotalCount() > 0) {
         String name = coverage.getName();
         IClassCoverage dup = (IClassCoverage)this.classes.put(name, coverage);
         if (dup != null) {
            if (dup.getId() != coverage.getId()) {
               throw new IllegalStateException("Can't add different class with same name: " + name);
            }
         } else {
            String source = coverage.getSourceFileName();
            if (source != null) {
               SourceFileCoverageImpl sourceFile = this.getSourceFile(source, coverage.getPackageName());
               sourceFile.increment(coverage);
            }
         }
      }

   }

   private SourceFileCoverageImpl getSourceFile(String filename, String packagename) {
      String key = packagename + '/' + filename;
      SourceFileCoverageImpl sourcefile = (SourceFileCoverageImpl)this.sourcefiles.get(key);
      if (sourcefile == null) {
         sourcefile = new SourceFileCoverageImpl(filename, packagename);
         this.sourcefiles.put(key, sourcefile);
      }

      return sourcefile;
   }
}

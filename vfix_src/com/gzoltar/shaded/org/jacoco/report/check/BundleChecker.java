package com.gzoltar.shaded.org.jacoco.report.check;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;
import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

class BundleChecker {
   private final ILanguageNames names;
   private final IViolationsOutput output;
   private final Collection<Rule> bundleRules;
   private final Collection<Rule> packageRules;
   private final Collection<Rule> classRules;
   private final Collection<Rule> sourceFileRules;
   private final Collection<Rule> methodRules;
   private final boolean traversePackages;
   private final boolean traverseClasses;
   private final boolean traverseSourceFiles;
   private final boolean traverseMethods;

   public BundleChecker(Collection<Rule> rules, ILanguageNames names, IViolationsOutput output) {
      this.names = names;
      this.output = output;
      this.bundleRules = new ArrayList();
      this.packageRules = new ArrayList();
      this.classRules = new ArrayList();
      this.sourceFileRules = new ArrayList();
      this.methodRules = new ArrayList();
      Iterator i$ = rules.iterator();

      while(i$.hasNext()) {
         Rule rule = (Rule)i$.next();
         switch(rule.getElement()) {
         case BUNDLE:
            this.bundleRules.add(rule);
            break;
         case PACKAGE:
            this.packageRules.add(rule);
            break;
         case CLASS:
            this.classRules.add(rule);
            break;
         case SOURCEFILE:
            this.sourceFileRules.add(rule);
            break;
         case METHOD:
            this.methodRules.add(rule);
         }
      }

      this.traverseMethods = !this.methodRules.isEmpty();
      this.traverseClasses = !this.classRules.isEmpty() || this.traverseMethods;
      this.traverseSourceFiles = !this.sourceFileRules.isEmpty();
      this.traversePackages = !this.packageRules.isEmpty() || this.traverseClasses || this.traverseSourceFiles;
   }

   public void checkBundle(IBundleCoverage bundleCoverage) {
      String name = bundleCoverage.getName();
      this.checkRules(bundleCoverage, this.bundleRules, "bundle", name);
      if (this.traversePackages) {
         Iterator i$ = bundleCoverage.getPackages().iterator();

         while(i$.hasNext()) {
            IPackageCoverage p = (IPackageCoverage)i$.next();
            this.check(p);
         }
      }

   }

   private void check(IPackageCoverage packageCoverage) {
      String name = this.names.getPackageName(packageCoverage.getName());
      this.checkRules(packageCoverage, this.packageRules, "package", name);
      Iterator i$;
      if (this.traverseClasses) {
         i$ = packageCoverage.getClasses().iterator();

         while(i$.hasNext()) {
            IClassCoverage c = (IClassCoverage)i$.next();
            this.check(c);
         }
      }

      if (this.traverseSourceFiles) {
         i$ = packageCoverage.getSourceFiles().iterator();

         while(i$.hasNext()) {
            ISourceFileCoverage s = (ISourceFileCoverage)i$.next();
            this.check(s);
         }
      }

   }

   private void check(IClassCoverage classCoverage) {
      String name = this.names.getQualifiedClassName(classCoverage.getName());
      this.checkRules(classCoverage, this.classRules, "class", name);
      if (this.traverseMethods) {
         Iterator i$ = classCoverage.getMethods().iterator();

         while(i$.hasNext()) {
            IMethodCoverage m = (IMethodCoverage)i$.next();
            this.check(m, classCoverage.getName());
         }
      }

   }

   private void check(ISourceFileCoverage sourceFile) {
      String name = sourceFile.getPackageName() + "/" + sourceFile.getName();
      this.checkRules(sourceFile, this.sourceFileRules, "source file", name);
   }

   private void check(IMethodCoverage method, String className) {
      String name = this.names.getQualifiedMethodName(className, method.getName(), method.getDesc(), method.getSignature());
      this.checkRules(method, this.methodRules, "method", name);
   }

   private void checkRules(ICoverageNode node, Collection<Rule> rules, String typename, String elementname) {
      Iterator i$ = rules.iterator();

      while(true) {
         Rule rule;
         do {
            if (!i$.hasNext()) {
               return;
            }

            rule = (Rule)i$.next();
         } while(!rule.matches(elementname));

         Iterator i$ = rule.getLimits().iterator();

         while(i$.hasNext()) {
            Limit limit = (Limit)i$.next();
            this.checkLimit(node, typename, elementname, rule, limit);
         }
      }
   }

   private void checkLimit(ICoverageNode node, String elementtype, String typename, Rule rule, Limit limit) {
      String message = limit.check(node);
      if (message != null) {
         this.output.onViolation(node, rule, limit, String.format("Rule violated for %s %s: %s", elementtype, typename, message));
      }

   }
}

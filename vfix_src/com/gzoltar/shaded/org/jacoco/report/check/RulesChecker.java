package com.gzoltar.shaded.org.jacoco.report.check;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import com.gzoltar.shaded.org.jacoco.report.IReportGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.IReportVisitor;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.JavaNames;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RulesChecker {
   private List<Rule> rules = new ArrayList();
   private ILanguageNames languageNames;

   public RulesChecker() {
      this.setLanguageNames(new JavaNames());
   }

   public void setRules(List<Rule> rules) {
      this.rules = rules;
   }

   public void setLanguageNames(ILanguageNames languageNames) {
      this.languageNames = languageNames;
   }

   public IReportVisitor createVisitor(IViolationsOutput output) {
      final BundleChecker bundleChecker = new BundleChecker(this.rules, this.languageNames, output);
      return new IReportVisitor() {
         public IReportGroupVisitor visitGroup(String name) throws IOException {
            return this;
         }

         public void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
            bundleChecker.checkBundle(bundle);
         }

         public void visitInfo(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData) throws IOException {
         }

         public void visitEnd() throws IOException {
         }
      };
   }
}

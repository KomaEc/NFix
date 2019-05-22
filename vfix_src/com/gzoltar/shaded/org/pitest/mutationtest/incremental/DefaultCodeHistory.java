package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassInfoSource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.ClassHistory;
import com.gzoltar.shaded.org.pitest.mutationtest.HistoryStore;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import java.math.BigInteger;
import java.util.Map;

public class DefaultCodeHistory implements CodeHistory {
   private final ClassInfoSource code;
   private final Map<MutationIdentifier, MutationStatusTestPair> previousResults;
   private final Map<ClassName, ClassHistory> previousClassPath;

   public DefaultCodeHistory(CodeSource code, HistoryStore historyStore) {
      this(code, historyStore.getHistoricResults(), historyStore.getHistoricClassPath());
   }

   public DefaultCodeHistory(ClassInfoSource code, Map<MutationIdentifier, MutationStatusTestPair> previousResults, Map<ClassName, ClassHistory> previousClassPath) {
      this.code = code;
      this.previousResults = previousResults;
      this.previousClassPath = previousClassPath;
   }

   public Option<MutationStatusTestPair> getPreviousResult(MutationIdentifier id) {
      return Option.some(this.previousResults.get(id));
   }

   public boolean hasClassChanged(ClassName className) {
      ClassHistory historic = (ClassHistory)this.previousClassPath.get(className);
      if (historic == null) {
         return true;
      } else {
         Option<ClassInfo> current = this.code.fetchClass(className);
         return !((ClassInfo)current.value()).getHierarchicalId().equals(historic.getId());
      }
   }

   public boolean hasCoverageChanged(ClassName className, BigInteger currentCoverage) {
      return !((ClassHistory)this.previousClassPath.get(className)).getCoverageId().equals(currentCoverage.toString(16));
   }
}

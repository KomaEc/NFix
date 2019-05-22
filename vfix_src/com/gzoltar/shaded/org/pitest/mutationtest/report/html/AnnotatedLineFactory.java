package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.coverage.ClassLine;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.FunctionalIterable;
import com.gzoltar.shaded.org.pitest.functional.FunctionalList;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.util.InputStreamLineIterable;
import com.gzoltar.shaded.org.pitest.util.StringUtil;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public class AnnotatedLineFactory {
   private final FunctionalIterable<MutationResult> mutations;
   private final CoverageDatabase statistics;
   private final Collection<ClassInfo> classesInFile;

   public AnnotatedLineFactory(FunctionalIterable<MutationResult> mutations, CoverageDatabase statistics, Collection<ClassInfo> classes) {
      this.mutations = mutations;
      this.statistics = statistics;
      this.classesInFile = classes;
   }

   public FunctionalList<Line> convert(Reader source) throws IOException {
      FunctionalList var3;
      try {
         InputStreamLineIterable lines = new InputStreamLineIterable(source);
         var3 = lines.map(this.stringToAnnotatedLine());
      } finally {
         source.close();
      }

      return var3;
   }

   private F<String, Line> stringToAnnotatedLine() {
      return new F<String, Line>() {
         private int lineNumber = 1;

         public Line apply(String a) {
            Line l = new Line((long)this.lineNumber, StringUtil.escapeBasicHtmlChars(a), AnnotatedLineFactory.this.lineCovered(this.lineNumber), AnnotatedLineFactory.this.getMutationsForLine(this.lineNumber));
            ++this.lineNumber;
            return l;
         }
      };
   }

   private List<MutationResult> getMutationsForLine(int lineNumber) {
      return this.mutations.filter(this.isAtLineNumber(lineNumber));
   }

   private F<MutationResult, Boolean> isAtLineNumber(final int lineNumber) {
      return new F<MutationResult, Boolean>() {
         public Boolean apply(MutationResult result) {
            return result.getDetails().getLineNumber() == lineNumber;
         }
      };
   }

   private LineStatus lineCovered(int line) {
      if (!this.isCodeLine(line)) {
         return LineStatus.NotApplicable;
      } else {
         return this.isLineCovered(line) ? LineStatus.Covered : LineStatus.NotCovered;
      }
   }

   private boolean isCodeLine(final int line) {
      F<ClassInfo, Boolean> predicate = new F<ClassInfo, Boolean>() {
         public Boolean apply(ClassInfo a) {
            return a.isCodeLine(line);
         }
      };
      return FCollection.contains(this.classesInFile, predicate);
   }

   private boolean isLineCovered(final int line) {
      F<ClassInfo, Boolean> predicate = new F<ClassInfo, Boolean>() {
         public Boolean apply(ClassInfo a) {
            return !AnnotatedLineFactory.this.statistics.getTestsForClassLine(new ClassLine(a.getName().asInternalName(), line)).isEmpty();
         }
      };
      return FCollection.contains(this.classesInFile, predicate);
   }
}

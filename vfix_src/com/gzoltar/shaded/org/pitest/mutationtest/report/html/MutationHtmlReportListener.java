package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.SourceLocator;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplate;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplateGroup;
import com.gzoltar.shaded.org.pitest.util.FileUtil;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class MutationHtmlReportListener implements MutationResultListener {
   private final ResultOutputStrategy outputStrategy;
   private final Collection<SourceLocator> sourceRoots;
   private final PackageSummaryMap packageSummaryData = new PackageSummaryMap();
   private final CoverageDatabase coverage;
   private final Set<String> mutatorNames;
   private final String css;

   public MutationHtmlReportListener(CoverageDatabase coverage, ResultOutputStrategy outputStrategy, Collection<String> mutatorNames, SourceLocator... locators) {
      this.coverage = coverage;
      this.outputStrategy = outputStrategy;
      this.sourceRoots = new HashSet(Arrays.asList(locators));
      this.mutatorNames = new HashSet(mutatorNames);
      this.css = this.loadCss();
   }

   private String loadCss() {
      try {
         return FileUtil.readToString(IsolationUtils.getContextClassLoader().getResourceAsStream("templates/mutation/style.css"));
      } catch (IOException var2) {
         Log.getLogger().log(Level.SEVERE, "Error while loading css", var2);
         return "";
      }
   }

   private void generateAnnotatedSourceFile(MutationTestSummaryData mutationMetaData) {
      try {
         String fileName = mutationMetaData.getPackageName() + File.separator + mutationMetaData.getFileName() + ".html";
         Writer writer = this.outputStrategy.createWriterForFile(fileName);
         StringTemplateGroup group = new StringTemplateGroup("mutation_test");
         StringTemplate st = group.getInstanceOf("templates/mutation/mutation_report");
         st.setAttribute("css", (Object)this.css);
         st.setAttribute("tests", (Object)mutationMetaData.getTests());
         st.setAttribute("mutators", (Object)mutationMetaData.getMutators());
         SourceFile sourceFile = this.createAnnotatedSourceFile(mutationMetaData);
         st.setAttribute("sourceFile", (Object)sourceFile);
         st.setAttribute("mutatedClasses", (Object)mutationMetaData.getMutatedClasses());
         writer.write(st.toString());
         writer.close();
      } catch (IOException var7) {
         Log.getLogger().log(Level.WARNING, "Error while writing report", var7);
      }

   }

   private PackageSummaryData collectPackageSummaries(ClassMutationResults mutationMetaData) {
      String packageName = mutationMetaData.getPackageName();
      return this.packageSummaryData.update(packageName, this.createSummaryData(this.coverage, mutationMetaData));
   }

   public MutationTestSummaryData createSummaryData(CoverageDatabase coverage, ClassMutationResults data) {
      return new MutationTestSummaryData(data.getFileName(), data.getMutations(), this.mutatorNames, coverage.getClassInfo(Collections.singleton(data.getMutatedClass())), (long)coverage.getNumberOfCoveredLines(Collections.singleton(data.getMutatedClass())));
   }

   private SourceFile createAnnotatedSourceFile(MutationTestSummaryData mutationMetaData) throws IOException {
      String fileName = mutationMetaData.getFileName();
      String packageName = mutationMetaData.getPackageName();
      MutationResultList mutationsForThisFile = mutationMetaData.getResults();
      List<Line> lines = this.createAnnotatedSourceCodeLines(fileName, packageName, mutationsForThisFile);
      return new SourceFile(fileName, lines, mutationsForThisFile.groupMutationsByLine());
   }

   private List<Line> createAnnotatedSourceCodeLines(String sourceFile, String packageName, MutationResultList mutationsForThisFile) throws IOException {
      Collection<ClassInfo> classes = this.coverage.getClassesForFile(sourceFile, packageName);
      Option<Reader> reader = this.findSourceFile(this.classInfoToNames(classes), sourceFile);
      if (reader.hasSome()) {
         AnnotatedLineFactory alf = new AnnotatedLineFactory(mutationsForThisFile, this.coverage, classes);
         return alf.convert((Reader)reader.value());
      } else {
         return Collections.emptyList();
      }
   }

   private Collection<String> classInfoToNames(Collection<ClassInfo> classes) {
      return FCollection.map(classes, this.classInfoToJavaName());
   }

   private F<ClassInfo, String> classInfoToJavaName() {
      return new F<ClassInfo, String>() {
         public String apply(ClassInfo a) {
            return a.getName().asJavaName();
         }
      };
   }

   private Option<Reader> findSourceFile(Collection<String> classes, String fileName) {
      Iterator i$ = this.sourceRoots.iterator();

      Option maybe;
      do {
         if (!i$.hasNext()) {
            return Option.none();
         }

         SourceLocator each = (SourceLocator)i$.next();
         maybe = each.locate(classes, fileName);
      } while(!maybe.hasSome());

      return maybe;
   }

   public void onRunEnd() {
      this.createIndexPages();
   }

   private void createIndexPages() {
      StringTemplateGroup group = new StringTemplateGroup("mutation_test");
      StringTemplate st = group.getInstanceOf("templates/mutation/mutation_package_index");
      Writer writer = this.outputStrategy.createWriterForFile("index.html");
      MutationTotals totals = new MutationTotals();
      List<PackageSummaryData> psd = new ArrayList(this.packageSummaryData.values());
      Collections.sort(psd);
      Iterator i$ = psd.iterator();

      while(i$.hasNext()) {
         PackageSummaryData psData = (PackageSummaryData)i$.next();
         totals.add(psData.getTotals());
         this.createPackageIndexPage(psData);
      }

      st.setAttribute("totals", (Object)totals);
      st.setAttribute("packageSummaries", (Object)psd);

      try {
         writer.write(st.toString());
         writer.close();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   private void createPackageIndexPage(PackageSummaryData psData) {
      StringTemplateGroup group = new StringTemplateGroup("mutation_test");
      StringTemplate st = group.getInstanceOf("templates/mutation/package_index");
      Writer writer = this.outputStrategy.createWriterForFile(psData.getPackageDirectory() + File.separator + "index.html");
      st.setAttribute("packageData", (Object)psData);

      try {
         writer.write(st.toString());
         writer.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void runStart() {
   }

   public void runEnd() {
      this.createIndexPages();
   }

   public void handleMutationResult(ClassMutationResults metaData) {
      PackageSummaryData packageData = this.collectPackageSummaries(metaData);
      this.generateAnnotatedSourceFile(packageData.getForSourceFile(metaData.getFileName()));
   }
}

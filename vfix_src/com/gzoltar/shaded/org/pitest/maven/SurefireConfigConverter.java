package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.util.Glob;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class SurefireConfigConverter {
   public ReportOptions update(ReportOptions option, Xpp3Dom configuration) {
      if (configuration == null) {
         return option;
      } else {
         this.convertExcludes(option, configuration);
         this.convertGroups(option, configuration);
         return option;
      }
   }

   private void convertGroups(ReportOptions option, Xpp3Dom configuration) {
      TestGroupConfig existing = option.getGroupConfig();
      if (existing == null || existing.getExcludedGroups().isEmpty() && existing.getIncludedGroups().isEmpty()) {
         List<String> groups = this.extractStrings("groups", configuration);
         List<String> excluded = this.extractStrings("excludedGroups", configuration);
         TestGroupConfig gc = new TestGroupConfig(excluded, groups);
         option.setGroupConfig(gc);
      }

   }

   private List<String> extractStrings(String element, Xpp3Dom configuration) {
      Xpp3Dom groups = configuration.getChild(element);
      if (groups != null) {
         String[] parts = groups.getValue().split(" ");
         return Arrays.asList(parts);
      } else {
         return Collections.emptyList();
      }
   }

   private void convertExcludes(ReportOptions option, Xpp3Dom configuration) {
      List<Predicate<String>> excludes = FCollection.map(this.extract("excludes", configuration), this.filenameToClassFilter());
      excludes.addAll(option.getExcludedClasses());
      option.setExcludedClasses(excludes);
   }

   private F<String, Predicate<String>> filenameToClassFilter() {
      return new F<String, Predicate<String>>() {
         public Predicate<String> apply(String a) {
            return new Glob(a.replace(".java", "").replace("/", "."));
         }
      };
   }

   private List<String> extract(String childname, Xpp3Dom config) {
      Xpp3Dom subelement = config.getChild(childname);
      if (subelement == null) {
         return Collections.emptyList();
      } else {
         List<String> result = new LinkedList();
         Xpp3Dom[] children = subelement.getChildren();
         Xpp3Dom[] arr$ = children;
         int len$ = children.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Xpp3Dom child = arr$[i$];
            result.add(child.getValue());
         }

         return result;
      }
   }
}

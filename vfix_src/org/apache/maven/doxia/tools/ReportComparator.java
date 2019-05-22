package org.apache.maven.doxia.tools;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import org.apache.maven.reporting.MavenReport;

public class ReportComparator implements Comparator {
   private final Locale locale;

   public ReportComparator(Locale locale) {
      if (locale == null) {
         throw new IllegalArgumentException("locale should be defined");
      } else {
         this.locale = locale;
      }
   }

   public int compare(Object o1, Object o2) {
      MavenReport r1 = (MavenReport)o1;
      MavenReport r2 = (MavenReport)o2;
      Collator collator = Collator.getInstance(this.locale);
      return collator.compare(r1.getName(this.locale), r2.getName(this.locale));
   }
}

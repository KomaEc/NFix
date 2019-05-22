package org.testng.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.testng.xml.IPostProcessor;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class OverrideProcessor implements IPostProcessor {
   private String[] m_groups;
   private String[] m_excludedGroups;

   public OverrideProcessor(String[] groups, String[] excludedGroups) {
      this.m_groups = groups;
      this.m_excludedGroups = excludedGroups;
   }

   public Collection<XmlSuite> process(Collection<XmlSuite> suites) {
      Iterator i$ = suites.iterator();

      while(true) {
         XmlSuite s;
         Iterator i$;
         XmlTest t;
         do {
            do {
               if (!i$.hasNext()) {
                  return suites;
               }

               s = (XmlSuite)i$.next();
               if (this.m_groups != null && this.m_groups.length > 0) {
                  i$ = s.getTests().iterator();

                  while(i$.hasNext()) {
                     t = (XmlTest)i$.next();
                     t.getIncludedGroups().clear();
                     t.getIncludedGroups().addAll(Arrays.asList(this.m_groups));
                  }
               }
            } while(this.m_excludedGroups == null);
         } while(this.m_excludedGroups.length <= 0);

         i$ = s.getTests().iterator();

         while(i$.hasNext()) {
            t = (XmlTest)i$.next();
            t.getExcludedGroups().clear();
            t.getExcludedGroups().addAll(Arrays.asList(this.m_excludedGroups));
         }
      }
   }
}

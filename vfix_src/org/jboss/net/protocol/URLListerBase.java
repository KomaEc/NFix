package org.jboss.net.protocol;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

public abstract class URLListerBase implements URLLister {
   protected static final URLLister.URLFilter acceptAllFilter = new URLLister.URLFilter() {
      public boolean accept(URL baseURL, String memberName) {
         return true;
      }
   };

   public Collection listMembers(URL baseUrl, String patterns, boolean scanNonDottedSubDirs) throws IOException {
      StringTokenizer tokens = new StringTokenizer(patterns, ",");
      String[] members = new String[tokens.countTokens()];

      for(int i = 0; tokens.hasMoreTokens(); ++i) {
         String token = tokens.nextToken();
         members[i] = token.trim();
      }

      URLLister.URLFilter filter = new URLListerBase.URLFilterImpl(members);
      return this.listMembers(baseUrl, filter, scanNonDottedSubDirs);
   }

   public Collection listMembers(URL baseUrl, String patterns) throws IOException {
      return this.listMembers(baseUrl, patterns, false);
   }

   public static class URLFilterImpl implements URLLister.URLFilter {
      protected boolean allowAll;
      protected HashSet constants;

      public URLFilterImpl(String[] patterns) {
         this.constants = new HashSet(Arrays.asList(patterns));
         this.allowAll = this.constants.contains("*");
      }

      public boolean accept(URL baseUrl, String name) {
         if (this.allowAll) {
            return true;
         } else {
            return this.constants.contains(name);
         }
      }
   }
}

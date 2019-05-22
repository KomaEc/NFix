package org.jboss.net.protocol;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public interface URLLister {
   Collection listMembers(URL var1, String var2, boolean var3) throws IOException;

   Collection listMembers(URL var1, String var2) throws IOException;

   Collection listMembers(URL var1, URLLister.URLFilter var2, boolean var3) throws IOException;

   Collection listMembers(URL var1, URLLister.URLFilter var2) throws IOException;

   public interface URLFilter {
      boolean accept(URL var1, String var2);
   }
}

package com.gzoltar.shaded.org.jacoco.report;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiSourceFileLocator implements ISourceFileLocator {
   private final int tabWidth;
   private final List<ISourceFileLocator> delegates;

   public MultiSourceFileLocator(int tabWidth) {
      this.tabWidth = tabWidth;
      this.delegates = new ArrayList();
   }

   public void add(ISourceFileLocator locator) {
      this.delegates.add(locator);
   }

   public Reader getSourceFile(String packageName, String fileName) throws IOException {
      Iterator i$ = this.delegates.iterator();

      Reader reader;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         ISourceFileLocator d = (ISourceFileLocator)i$.next();
         reader = d.getSourceFile(packageName, fileName);
      } while(reader == null);

      return reader;
   }

   public int getTabWidth() {
      return this.tabWidth;
   }
}

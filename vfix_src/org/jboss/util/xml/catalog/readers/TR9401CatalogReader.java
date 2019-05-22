package org.jboss.util.xml.catalog.readers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Vector;
import org.jboss.util.xml.catalog.Catalog;
import org.jboss.util.xml.catalog.CatalogEntry;
import org.jboss.util.xml.catalog.CatalogException;

public class TR9401CatalogReader extends TextCatalogReader {
   public void readCatalog(Catalog catalog, InputStream is) throws MalformedURLException, IOException {
      this.catfile = is;
      if (this.catfile != null) {
         Vector unknownEntry = null;

         while(true) {
            String token = this.nextToken();
            if (token == null) {
               if (unknownEntry != null) {
                  catalog.unknownEntry(unknownEntry);
                  unknownEntry = null;
               }

               this.catfile.close();
               this.catfile = null;
               return;
            }

            String entryToken = null;
            if (this.caseSensitive) {
               entryToken = token;
            } else {
               entryToken = token.toUpperCase();
            }

            if (entryToken.equals("DELEGATE")) {
               entryToken = "DELEGATE_PUBLIC";
            }

            try {
               int type = CatalogEntry.getEntryType(entryToken);
               int numArgs = CatalogEntry.getEntryArgCount(type);
               Vector args = new Vector();
               if (unknownEntry != null) {
                  catalog.unknownEntry(unknownEntry);
                  unknownEntry = null;
               }

               for(int count = 0; count < numArgs; ++count) {
                  args.addElement(this.nextToken());
               }

               catalog.addEntry(new CatalogEntry(entryToken, args));
            } catch (CatalogException var10) {
               if (var10.getExceptionType() == 3) {
                  if (unknownEntry == null) {
                     unknownEntry = new Vector();
                  }

                  unknownEntry.addElement(token);
               } else if (var10.getExceptionType() == 2) {
                  catalog.getCatalogManager().debug.message(1, "Invalid catalog entry", token);
                  unknownEntry = null;
               }
            }
         }
      }
   }
}

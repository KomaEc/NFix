package org.jboss.util.xml.catalog.readers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import org.jboss.util.xml.catalog.Catalog;
import org.jboss.util.xml.catalog.CatalogException;

public interface CatalogReader {
   void readCatalog(Catalog var1, String var2) throws MalformedURLException, IOException, CatalogException;

   void readCatalog(Catalog var1, InputStream var2) throws IOException, CatalogException;
}

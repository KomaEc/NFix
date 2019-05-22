package org.jboss.util.xml.catalog.readers;

import org.jboss.util.xml.catalog.Catalog;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

public interface SAXCatalogParser extends ContentHandler, DocumentHandler {
   void setCatalog(Catalog var1);
}

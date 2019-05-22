package org.apache.commons.digester;

import org.xml.sax.Attributes;

public interface ObjectCreationFactory {
   Object createObject(Attributes var1) throws Exception;

   Digester getDigester();

   void setDigester(Digester var1);
}

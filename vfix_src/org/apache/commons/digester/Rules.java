package org.apache.commons.digester;

import java.util.List;

public interface Rules {
   Digester getDigester();

   void setDigester(Digester var1);

   String getNamespaceURI();

   void setNamespaceURI(String var1);

   void add(String var1, Rule var2);

   void clear();

   /** @deprecated */
   List match(String var1);

   List match(String var1, String var2);

   List rules();
}

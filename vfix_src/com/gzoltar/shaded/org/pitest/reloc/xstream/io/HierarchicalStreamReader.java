package com.gzoltar.shaded.org.pitest.reloc.xstream.io;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorReporter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorWriter;
import java.util.Iterator;

public interface HierarchicalStreamReader extends ErrorReporter {
   boolean hasMoreChildren();

   void moveDown();

   void moveUp();

   String getNodeName();

   String getValue();

   String getAttribute(String var1);

   String getAttribute(int var1);

   int getAttributeCount();

   String getAttributeName(int var1);

   Iterator getAttributeNames();

   void appendErrors(ErrorWriter var1);

   void close();

   HierarchicalStreamReader underlyingReader();
}

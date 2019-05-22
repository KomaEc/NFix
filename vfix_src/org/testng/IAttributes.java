package org.testng;

import java.io.Serializable;
import java.util.Set;

public interface IAttributes extends Serializable {
   Object getAttribute(String var1);

   void setAttribute(String var1, Object var2);

   Set<String> getAttributeNames();

   Object removeAttribute(String var1);
}

package org.testng.xml.dom;

import org.w3c.dom.Node;

public interface ITagSetter<T> {
   void setProperty(String var1, T var2, Node var3);
}

package org.testng.xml;

import java.util.Collection;

public interface IPostProcessor {
   Collection<XmlSuite> process(Collection<XmlSuite> var1);
}

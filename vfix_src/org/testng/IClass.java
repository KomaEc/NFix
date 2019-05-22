package org.testng;

import java.io.Serializable;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

public interface IClass extends Serializable {
   String getName();

   XmlTest getXmlTest();

   XmlClass getXmlClass();

   String getTestName();

   Class getRealClass();

   Object[] getInstances(boolean var1);

   int getInstanceCount();

   long[] getInstanceHashCodes();

   void addInstance(Object var1);
}

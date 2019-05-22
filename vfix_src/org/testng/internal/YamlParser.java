package org.testng.internal;

import java.io.FileNotFoundException;
import java.io.InputStream;
import org.testng.TestNGException;
import org.testng.xml.ISuiteParser;
import org.testng.xml.XmlSuite;

public class YamlParser implements ISuiteParser {
   public XmlSuite parse(String filePath, InputStream is, boolean loadClasses) throws TestNGException {
      try {
         return Yaml.parse(filePath, is);
      } catch (FileNotFoundException var5) {
         throw new TestNGException(var5);
      }
   }

   public boolean accept(String fileName) {
      return fileName.endsWith(".yaml");
   }
}

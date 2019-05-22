package org.apache.commons.digester.xmlrules;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DigesterLoader {
   public static Digester createDigester(InputSource rulesSource) {
      RuleSet ruleSet = new FromXmlRuleSet(rulesSource);
      Digester digester = new Digester();
      digester.addRuleSet(ruleSet);
      return digester;
   }

   public static Digester createDigester(InputSource rulesSource, Digester rulesDigester) {
      RuleSet ruleSet = new FromXmlRuleSet(rulesSource, rulesDigester);
      Digester digester = new Digester();
      digester.addRuleSet(ruleSet);
      return digester;
   }

   public static Digester createDigester(URL rulesXml) {
      RuleSet ruleSet = new FromXmlRuleSet(rulesXml);
      Digester digester = new Digester();
      digester.addRuleSet(ruleSet);
      return digester;
   }

   public static Digester createDigester(URL rulesXml, Digester rulesDigester) {
      RuleSet ruleSet = new FromXmlRuleSet(rulesXml, rulesDigester);
      Digester digester = new Digester();
      digester.addRuleSet(ruleSet);
      return digester;
   }

   public static Object load(URL digesterRules, ClassLoader classLoader, URL fileURL) throws IOException, SAXException, DigesterLoadingException {
      return load(digesterRules, classLoader, fileURL.openStream());
   }

   public static Object load(URL digesterRules, ClassLoader classLoader, InputStream input) throws IOException, SAXException, DigesterLoadingException {
      Digester digester = createDigester(digesterRules);
      digester.setClassLoader(classLoader);

      try {
         return digester.parse(input);
      } catch (XmlLoadException var5) {
         throw new DigesterLoadingException(var5.getMessage(), var5);
      }
   }

   public static Object load(URL digesterRules, ClassLoader classLoader, Reader reader) throws IOException, SAXException, DigesterLoadingException {
      Digester digester = createDigester(digesterRules);
      digester.setClassLoader(classLoader);

      try {
         return digester.parse(reader);
      } catch (XmlLoadException var5) {
         throw new DigesterLoadingException(var5.getMessage(), var5);
      }
   }

   public static Object load(URL digesterRules, ClassLoader classLoader, URL fileURL, Object rootObject) throws IOException, SAXException, DigesterLoadingException {
      return load(digesterRules, classLoader, fileURL.openStream(), rootObject);
   }

   public static Object load(URL digesterRules, ClassLoader classLoader, InputStream input, Object rootObject) throws IOException, SAXException, DigesterLoadingException {
      Digester digester = createDigester(digesterRules);
      digester.setClassLoader(classLoader);
      digester.push(rootObject);

      try {
         return digester.parse(input);
      } catch (XmlLoadException var6) {
         throw new DigesterLoadingException(var6.getMessage(), var6);
      }
   }

   public static Object load(URL digesterRules, ClassLoader classLoader, Reader input, Object rootObject) throws IOException, SAXException, DigesterLoadingException {
      Digester digester = createDigester(digesterRules);
      digester.setClassLoader(classLoader);
      digester.push(rootObject);

      try {
         return digester.parse(input);
      } catch (XmlLoadException var6) {
         throw new DigesterLoadingException(var6.getMessage(), var6);
      }
   }
}

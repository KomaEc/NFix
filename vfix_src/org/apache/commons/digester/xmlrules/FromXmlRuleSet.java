package org.apache.commons.digester.xmlrules;

import java.net.URL;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.InputSource;

public class FromXmlRuleSet extends RuleSetBase {
   public static final String DIGESTER_DTD_PATH = "org/apache/commons/digester/xmlrules/digester-rules.dtd";
   private FromXmlRuleSet.XMLRulesLoader rulesLoader;
   private DigesterRuleParser parser;
   private Digester rulesDigester;

   public FromXmlRuleSet(URL rulesXml) {
      this(rulesXml, new DigesterRuleParser(), new Digester());
   }

   public FromXmlRuleSet(URL rulesXml, Digester rulesDigester) {
      this(rulesXml, new DigesterRuleParser(), rulesDigester);
   }

   public FromXmlRuleSet(URL rulesXml, DigesterRuleParser parser) {
      this(rulesXml, parser, new Digester());
   }

   public FromXmlRuleSet(URL rulesXml, DigesterRuleParser parser, Digester rulesDigester) {
      this.init(new FromXmlRuleSet.URLXMLRulesLoader(rulesXml), parser, rulesDigester);
   }

   public FromXmlRuleSet(InputSource inputSource) {
      this(inputSource, new DigesterRuleParser(), new Digester());
   }

   public FromXmlRuleSet(InputSource inputSource, Digester rulesDigester) {
      this(inputSource, new DigesterRuleParser(), rulesDigester);
   }

   public FromXmlRuleSet(InputSource inputSource, DigesterRuleParser parser) {
      this(inputSource, parser, new Digester());
   }

   public FromXmlRuleSet(InputSource inputSource, DigesterRuleParser parser, Digester rulesDigester) {
      this.init(new FromXmlRuleSet.InputSourceXMLRulesLoader(inputSource), parser, rulesDigester);
   }

   private void init(FromXmlRuleSet.XMLRulesLoader rulesLoader, DigesterRuleParser parser, Digester rulesDigester) {
      this.rulesLoader = rulesLoader;
      this.parser = parser;
      this.rulesDigester = rulesDigester;
   }

   public void addRuleInstances(Digester digester) throws XmlLoadException {
      this.addRuleInstances(digester, (String)null);
   }

   public void addRuleInstances(Digester digester, String basePath) throws XmlLoadException {
      URL dtdURL = this.getClass().getClassLoader().getResource("org/apache/commons/digester/xmlrules/digester-rules.dtd");
      if (dtdURL == null) {
         throw new XmlLoadException("Cannot find resource \"org/apache/commons/digester/xmlrules/digester-rules.dtd\"");
      } else {
         this.parser.setDigesterRulesDTD(dtdURL.toString());
         this.parser.setTarget(digester);
         this.parser.setBasePath(basePath);
         this.rulesDigester.addRuleSet(this.parser);
         this.rulesDigester.push(this.parser);
         this.rulesLoader.loadRules();
      }
   }

   private class InputSourceXMLRulesLoader extends FromXmlRuleSet.XMLRulesLoader {
      private InputSource inputSource;

      public InputSourceXMLRulesLoader(InputSource inputSource) {
         super(null);
         this.inputSource = inputSource;
      }

      public void loadRules() throws XmlLoadException {
         try {
            FromXmlRuleSet.this.rulesDigester.parse(this.inputSource);
         } catch (Exception var2) {
            throw new XmlLoadException(var2);
         }
      }
   }

   private class URLXMLRulesLoader extends FromXmlRuleSet.XMLRulesLoader {
      private URL url;

      public URLXMLRulesLoader(URL url) {
         super(null);
         this.url = url;
      }

      public void loadRules() throws XmlLoadException {
         try {
            FromXmlRuleSet.this.rulesDigester.parse(this.url.openStream());
         } catch (Exception var2) {
            throw new XmlLoadException(var2);
         }
      }
   }

   private abstract static class XMLRulesLoader {
      private XMLRulesLoader() {
      }

      public abstract void loadRules() throws XmlLoadException;

      // $FF: synthetic method
      XMLRulesLoader(Object x0) {
         this();
      }
   }
}

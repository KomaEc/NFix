package org.apache.commons.digester.plugins;

import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.xml.sax.Attributes;

public class PluginDeclarationRule extends Rule {
   public void begin(String namespace, String name, Attributes attributes) throws Exception {
      int nAttrs = attributes.getLength();
      Properties props = new Properties();

      for(int i = 0; i < nAttrs; ++i) {
         String key = attributes.getLocalName(i);
         if (key == null || key.length() == 0) {
            key = attributes.getQName(i);
         }

         String value = attributes.getValue(i);
         props.setProperty(key, value);
      }

      try {
         declarePlugin(this.digester, props);
      } catch (PluginInvalidInputException var9) {
         throw new PluginInvalidInputException("Error on element [" + this.digester.getMatch() + "]: " + var9.getMessage());
      }
   }

   public static void declarePlugin(Digester digester, Properties props) throws PluginException {
      Log log = digester.getLogger();
      boolean debug = log.isDebugEnabled();
      String id = props.getProperty("id");
      String pluginClassName = props.getProperty("class");
      if (id == null) {
         throw new PluginInvalidInputException("mandatory attribute id not present on plugin declaration");
      } else if (pluginClassName == null) {
         throw new PluginInvalidInputException("mandatory attribute class not present on plugin declaration");
      } else {
         Declaration newDecl = new Declaration(pluginClassName);
         newDecl.setId(id);
         newDecl.setProperties(props);
         PluginRules rc = (PluginRules)digester.getRules();
         PluginManager pm = rc.getPluginManager();
         newDecl.init(digester, pm);
         pm.addDeclaration(newDecl);
      }
   }
}

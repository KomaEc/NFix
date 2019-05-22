package org.apache.commons.digester.plugins.strategies;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.plugins.PluginException;
import org.apache.commons.digester.plugins.RuleFinder;
import org.apache.commons.digester.plugins.RuleLoader;

public class FinderFromFile extends RuleFinder {
   public static String DFLT_FILENAME_ATTR = "file";
   private String filenameAttr;

   public FinderFromFile() {
      this(DFLT_FILENAME_ATTR);
   }

   public FinderFromFile(String filenameAttr) {
      this.filenameAttr = filenameAttr;
   }

   public RuleLoader findLoader(Digester d, Class pluginClass, Properties p) throws PluginException {
      String rulesFileName = p.getProperty(this.filenameAttr);
      if (rulesFileName == null) {
         return null;
      } else {
         FileInputStream is = null;

         try {
            is = new FileInputStream(rulesFileName);
         } catch (IOException var19) {
            throw new PluginException("Unable to process file [" + rulesFileName + "]", var19);
         }

         LoaderFromStream var7;
         try {
            RuleLoader loader = new LoaderFromStream(is);
            var7 = loader;
         } catch (Exception var17) {
            throw new PluginException("Unable to load xmlrules from file [" + rulesFileName + "]", var17);
         } finally {
            try {
               is.close();
            } catch (IOException var16) {
               throw new PluginException("Unable to close stream for file [" + rulesFileName + "]", var16);
            }
         }

         return var7;
      }
   }
}

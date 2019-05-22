package soot.plugins.internal;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.PackManager;
import soot.Transform;
import soot.plugins.SootPhasePlugin;
import soot.plugins.model.PhasePluginDescription;
import soot.plugins.model.PluginDescription;
import soot.plugins.model.Plugins;

public class PluginLoader {
   private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);
   private static ClassLoadingStrategy loadStrategy = new ReflectionClassLoadingStrategy();

   private static String[] appendEnabled(String[] options) {
      String[] result = options;
      int var2 = options.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String option = result[var3];
         if (option.equals("enabled")) {
            return options;
         }
      }

      result = new String[options.length + 1];
      result[0] = "enabled";
      System.arraycopy(options, 0, result, 1, options.length);
      return result;
   }

   private static String concat(String[] options) {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      String[] var3 = options;
      int var4 = options.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String option = var3[var5];
         if (!first) {
            sb.append(" ");
         }

         first = false;
         sb.append(option);
      }

      return sb.toString();
   }

   private static String getPackName(String phaseName) {
      if (!phaseName.contains(".")) {
         throw new RuntimeException("Name of phase '" + phaseName + "'does not contain a dot.");
      } else {
         return phaseName.substring(0, phaseName.indexOf(46));
      }
   }

   private static void handlePhasePlugin(PhasePluginDescription pluginDescription) {
      try {
         Object instance = loadStrategy.create(pluginDescription.getClassName());
         if (!(instance instanceof SootPhasePlugin)) {
            throw new RuntimeException("The plugin class '" + pluginDescription.getClassName() + "' does not implement SootPhasePlugin.");
         } else {
            SootPhasePlugin phasePlugin = (SootPhasePlugin)instance;
            phasePlugin.setDescription(pluginDescription);
            String packName = getPackName(pluginDescription.getPhaseName());
            Transform transform = new Transform(pluginDescription.getPhaseName(), phasePlugin.getTransformer());
            transform.setDeclaredOptions(concat(appendEnabled(phasePlugin.getDeclaredOptions())));
            transform.setDefaultOptions(concat(phasePlugin.getDefaultOptions()));
            PackManager.v().getPack(packName).add(transform);
         }
      } catch (ClassNotFoundException var5) {
         throw new RuntimeException("Failed to load plugin class for " + pluginDescription + ".", var5);
      } catch (InstantiationException var6) {
         throw new RuntimeException("Failed to instanciate plugin class for " + pluginDescription + ".", var6);
      }
   }

   public static boolean load(String file) {
      File configFile = new File(file);
      if (!configFile.exists()) {
         System.err.println("The configuration file '" + configFile + "' does not exist.");
         return false;
      } else if (!configFile.canRead()) {
         System.err.println("Cannot read the configuration file '" + configFile + "'.");
         return false;
      } else {
         try {
            JAXBContext context = JAXBContext.newInstance(Plugins.class, PluginDescription.class, PhasePluginDescription.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object root = unmarshaller.unmarshal(configFile);
            if (!(root instanceof Plugins)) {
               System.err.println("Expected a root node of type Plugins got " + root.getClass());
               return false;
            } else {
               loadPlugins((Plugins)root);
               return true;
            }
         } catch (RuntimeException var5) {
            System.err.println("Failed to load plugin correctly.");
            logger.error((String)var5.getMessage(), (Throwable)var5);
            return false;
         } catch (JAXBException var6) {
            System.err.println("An error occured while loading plugin configuration '" + file + "'.");
            logger.error((String)var6.getMessage(), (Throwable)var6);
            return false;
         }
      }
   }

   public static void loadPlugins(Plugins plugins) throws RuntimeException {
      Iterator var1 = plugins.getPluginDescriptions().iterator();

      while(var1.hasNext()) {
         PluginDescription plugin = (PluginDescription)var1.next();
         if (plugin instanceof PhasePluginDescription) {
            handlePhasePlugin((PhasePluginDescription)plugin);
         } else {
            logger.debug("[Warning] Unhandled plugin of type '" + plugin.getClass() + "'");
         }
      }

   }

   public static void setClassLoadingStrategy(ClassLoadingStrategy strategy) {
      if (strategy == null) {
         throw new InvalidParameterException("Class loading strategy is not allowed to be null.");
      } else {
         loadStrategy = strategy;
      }
   }

   private PluginLoader() {
   }
}

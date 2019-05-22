package groovy.grape;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Grape {
   private static boolean enableGrapes = Boolean.valueOf(System.getProperties().getProperty("groovy.grape.enable", "true"));
   private static boolean enableAutoDownload = Boolean.valueOf(System.getProperties().getProperty("groovy.grape.autoDownload", "true"));
   protected static GrapeEngine instance;

   public static boolean getEnableGrapes() {
      return enableGrapes;
   }

   public static void setEnableGrapes(boolean enableGrapes) {
      Grape.enableGrapes = enableGrapes;
   }

   public static boolean getEnableAutoDownload() {
      return enableAutoDownload;
   }

   public static void setEnableAutoDownload(boolean enableAutoDownload) {
      Grape.enableAutoDownload = enableAutoDownload;
   }

   public static synchronized GrapeEngine getInstance() {
      if (instance == null) {
         try {
            instance = (GrapeEngine)Class.forName("groovy.grape.GrapeIvy").newInstance();
         } catch (InstantiationException var1) {
         } catch (IllegalAccessException var2) {
         } catch (ClassNotFoundException var3) {
         }
      }

      return instance;
   }

   public static void grab(String endorsed) {
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            instance.grab(endorsed);
         }
      }

   }

   public static void grab(Map<String, Object> dependency) {
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            if (!dependency.containsKey("autoDownload")) {
               dependency.put("autoDownload", enableAutoDownload);
            }

            instance.grab(dependency);
         }
      }

   }

   public static void grab(Map<String, Object> args, Map... dependencies) {
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            if (!args.containsKey("autoDownload")) {
               args.put("autoDownload", enableAutoDownload);
            }

            instance.grab(args, dependencies);
         }
      }

   }

   public static Map<String, Map<String, List<String>>> enumerateGrapes() {
      Map<String, Map<String, List<String>>> grapes = null;
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            grapes = instance.enumerateGrapes();
         }
      }

      return grapes == null ? Collections.emptyMap() : grapes;
   }

   public static URI[] resolve(Map<String, Object> args, Map... dependencies) {
      return resolve(args, (List)null, dependencies);
   }

   public static URI[] resolve(Map<String, Object> args, List depsInfo, Map... dependencies) {
      URI[] uris = null;
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            if (!args.containsKey("autoDownload")) {
               args.put("autoDownload", enableAutoDownload);
            }

            uris = instance.resolve(args, depsInfo, dependencies);
         }
      }

      return uris == null ? new URI[0] : uris;
   }

   public static Map[] listDependencies(ClassLoader cl) {
      Map[] maps = null;
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            maps = instance.listDependencies(cl);
         }
      }

      return maps == null ? new Map[0] : maps;
   }

   public static void addResolver(Map<String, Object> args) {
      if (enableGrapes) {
         GrapeEngine instance = getInstance();
         if (instance != null) {
            instance.addResolver(args);
         }
      }

   }
}

package groovy.lang;

import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.codehaus.groovy.util.ReferenceBundle;
import org.codehaus.groovy.util.ReleaseInfo;

public final class GroovySystem {
   private static MetaClass objectMetaClass;
   private static final boolean USE_REFLECTION = true;
   private static final MetaClassRegistry META_CLASS_REGISTRY = new MetaClassRegistryImpl();
   private static boolean keepJavaMetaClasses = false;

   private GroovySystem() {
   }

   public static boolean isUseReflection() {
      return USE_REFLECTION;
   }

   public static MetaClassRegistry getMetaClassRegistry() {
      return META_CLASS_REGISTRY;
   }

   public static void setKeepJavaMetaClasses(boolean keepJavaMetaClasses) {
      GroovySystem.keepJavaMetaClasses = keepJavaMetaClasses;
   }

   public static boolean isKeepJavaMetaClasses() {
      return keepJavaMetaClasses;
   }

   public static void stopThreadedReferenceManager() {
      ReferenceBundle.getSoftBundle().getManager().stopThread();
      ReferenceBundle.getWeakBundle().getManager().stopThread();
   }

   public static String getVersion() {
      return ReleaseInfo.getVersion();
   }
}

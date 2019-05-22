package org.apache.maven.plugin.surefire;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import org.apache.maven.surefire.booter.Classpath;

public class ClasspathCache {
   private static final ConcurrentHashMap<String, Classpath> classpaths = new ConcurrentHashMap(4);

   public static Classpath getCachedClassPath(@Nonnull String artifactId) {
      return (Classpath)classpaths.get(artifactId);
   }

   public static void setCachedClasspath(@Nonnull String key, @Nonnull Classpath classpath) {
      classpaths.put(key, classpath);
   }
}

package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.io.IOException;
import java.util.logging.Logger;

public class ClassPathByteArraySource implements ClassByteArraySource {
   private static final Logger LOG = Log.getLogger();
   private final ClassPath classPath;

   public ClassPathByteArraySource() {
      this(new ClassPath());
   }

   public ClassPathByteArraySource(ClassPath classPath) {
      this.classPath = classPath;
   }

   public Option<byte[]> getBytes(String classname) {
      try {
         return Option.some(this.classPath.getClassData(classname));
      } catch (IOException var3) {
         LOG.fine("Could not read class " + classname + ":" + var3.getMessage());
         return Option.none();
      }
   }
}

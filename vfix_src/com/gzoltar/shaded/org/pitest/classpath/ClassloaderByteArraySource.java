package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.IOException;

public class ClassloaderByteArraySource implements ClassByteArraySource {
   private final ClassPath cp;

   public ClassloaderByteArraySource(ClassLoader loader) {
      this.cp = new ClassPath(new ClassPathRoot[]{new OtherClassLoaderClassPathRoot(loader)});
   }

   public Option<byte[]> getBytes(String classname) {
      try {
         return Option.some(this.cp.getClassData(classname));
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }
}

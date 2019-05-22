package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.boot.HotSwapAgent;
import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classpath.ClassPathByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.process.JavaAgent;
import com.gzoltar.shaded.org.pitest.util.FileUtil;
import com.gzoltar.shaded.org.pitest.util.PitError;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.zip.ZipEntry;
import sun.pitest.CodeCoverageStore;
import sun.pitest.InvokeReceiver;

public class JarCreatingJarFinder implements JavaAgent {
   protected static final String CAN_REDEFINE_CLASSES = "Can-Redefine-Classes";
   protected static final String PREMAIN_CLASS = "Premain-Class";
   protected static final String CAN_SET_NATIVE_METHOD = "Can-Set-Native-Method-Prefix";
   protected static final String BOOT_CLASSPATH = "Boot-Class-Path";
   private static final String AGENT_CLASS_NAME = HotSwapAgent.class.getName();
   private Option<String> location;
   private final ClassByteArraySource classByteSource;

   public JarCreatingJarFinder(ClassByteArraySource classByteSource) {
      this.location = Option.none();
      this.classByteSource = classByteSource;
   }

   public JarCreatingJarFinder() {
      this(new ClassPathByteArraySource());
   }

   public Option<String> getJarLocation() {
      if (this.location.hasNone()) {
         this.location = this.createJar();
      }

      return this.location;
   }

   private Option<String> createJar() {
      try {
         File randomName = File.createTempFile(FileUtil.randomFilename(), ".jar");
         FileOutputStream fos = new FileOutputStream(randomName);
         this.createJarFromClassPathResources(fos, randomName.getAbsolutePath());
         return Option.some(randomName.getAbsolutePath());
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   private void createJarFromClassPathResources(FileOutputStream fos, String location) throws IOException {
      Manifest m = new Manifest();
      m.clear();
      Attributes global = m.getMainAttributes();
      if (global.getValue(Name.MANIFEST_VERSION) == null) {
         global.put(Name.MANIFEST_VERSION, "1.0");
      }

      File mylocation = new File(location);
      global.putValue("Boot-Class-Path", this.getBootClassPath(mylocation));
      global.putValue("Premain-Class", AGENT_CLASS_NAME);
      global.putValue("Can-Redefine-Classes", "true");
      global.putValue("Can-Set-Native-Method-Prefix", "true");
      JarOutputStream jos = new JarOutputStream(fos, m);
      this.addClass(HotSwapAgent.class, jos);
      this.addClass(CodeCoverageStore.class, jos);
      this.addClass(InvokeReceiver.class, jos);
      jos.close();
   }

   private String getBootClassPath(File mylocation) {
      return mylocation.getAbsolutePath().replace('\\', '/');
   }

   private void addClass(Class<?> clazz, JarOutputStream jos) throws IOException {
      String className = clazz.getName();
      ZipEntry ze = new ZipEntry(className.replace(".", "/") + ".class");
      jos.putNextEntry(ze);
      jos.write(this.classBytes(className));
      jos.closeEntry();
   }

   private byte[] classBytes(String className) {
      Option<byte[]> bytes = this.classByteSource.getBytes(className);
      if (bytes.hasSome()) {
         return (byte[])bytes.value();
      } else {
         throw new PitError("Unable to load class content for " + className);
      }
   }

   public void close() {
      if (this.location.hasSome()) {
         File f = new File((String)this.location.value());
         f.delete();
      }

   }
}

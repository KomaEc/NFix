package soot.jimple.toolkits.pointer.util;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoFileDescriptorNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoFileInputStreamNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoFileOutputStreamNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoFileSystemNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoObjectInputStreamNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoObjectOutputStreamNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaIoObjectStreamClassNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangClassLoaderNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangClassLoaderNativeLibraryNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangClassNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangDoubleNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangFloatNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangObjectNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangPackageNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangReflectArrayNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangReflectConstructorNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangReflectFieldNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangReflectMethodNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangReflectProxyNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangRuntimeNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangSecurityManagerNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangShutdownNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangStrictMathNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangStringNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangSystemNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangThreadNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaLangThrowableNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaNetInetAddressImplNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaNetInetAddressNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaSecurityAccessControllerNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilJarJarFileNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilResourceBundleNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilTimeZoneNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilZipCRC32Native;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilZipInflaterNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilZipZipEntryNative;
import soot.jimple.toolkits.pointer.nativemethods.JavaUtilZipZipFileNative;
import soot.jimple.toolkits.pointer.nativemethods.NativeMethodClass;
import soot.jimple.toolkits.pointer.nativemethods.NativeMethodNotSupportedException;
import soot.jimple.toolkits.pointer.nativemethods.SunMiscSignalHandlerNative;
import soot.jimple.toolkits.pointer.nativemethods.SunMiscSignalNative;
import soot.jimple.toolkits.pointer.nativemethods.SunMiscUnsafeNative;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;

public class NativeMethodDriver {
   private static final Logger logger = LoggerFactory.getLogger(NativeMethodDriver.class);
   private final HashMap<String, NativeMethodClass> cnameToSim = new HashMap(100);
   private final boolean DEBUG = false;

   public NativeMethodDriver(NativeHelper helper) {
      this.cnameToSim.put("java.lang.Object", new JavaLangObjectNative(helper));
      this.cnameToSim.put("java.lang.System", new JavaLangSystemNative(helper));
      this.cnameToSim.put("java.lang.Runtime", new JavaLangRuntimeNative(helper));
      this.cnameToSim.put("java.lang.Shutdown", new JavaLangShutdownNative(helper));
      this.cnameToSim.put("java.lang.String", new JavaLangStringNative(helper));
      this.cnameToSim.put("java.lang.Float", new JavaLangFloatNative(helper));
      this.cnameToSim.put("java.lang.Double", new JavaLangDoubleNative(helper));
      this.cnameToSim.put("java.lang.StrictMath", new JavaLangStrictMathNative(helper));
      this.cnameToSim.put("java.lang.Throwable", new JavaLangThrowableNative(helper));
      this.cnameToSim.put("java.lang.Class", new JavaLangClassNative(helper));
      this.cnameToSim.put("java.lang.Package", new JavaLangPackageNative(helper));
      this.cnameToSim.put("java.lang.Thread", new JavaLangThreadNative(helper));
      this.cnameToSim.put("java.lang.ClassLoader", new JavaLangClassLoaderNative(helper));
      this.cnameToSim.put("java.lang.ClassLoader$NativeLibrary", new JavaLangClassLoaderNativeLibraryNative(helper));
      this.cnameToSim.put("java.lang.SecurityManager", new JavaLangSecurityManagerNative(helper));
      this.cnameToSim.put("java.lang.reflect.Field", new JavaLangReflectFieldNative(helper));
      this.cnameToSim.put("java.lang.reflect.Array", new JavaLangReflectArrayNative(helper));
      this.cnameToSim.put("java.lang.reflect.Method", new JavaLangReflectMethodNative(helper));
      this.cnameToSim.put("java.lang.reflect.Constructor", new JavaLangReflectConstructorNative(helper));
      this.cnameToSim.put("java.lang.reflect.Proxy", new JavaLangReflectProxyNative(helper));
      this.cnameToSim.put("java.io.FileInputStream", new JavaIoFileInputStreamNative(helper));
      this.cnameToSim.put("java.io.FileOutputStream", new JavaIoFileOutputStreamNative(helper));
      this.cnameToSim.put("java.io.ObjectInputStream", new JavaIoObjectInputStreamNative(helper));
      this.cnameToSim.put("java.io.ObjectOutputStream", new JavaIoObjectOutputStreamNative(helper));
      this.cnameToSim.put("java.io.ObjectStreamClass", new JavaIoObjectStreamClassNative(helper));
      this.cnameToSim.put("java.io.FileSystem", new JavaIoFileSystemNative(helper));
      this.cnameToSim.put("java.io.FileDescriptor", new JavaIoFileDescriptorNative(helper));
      this.cnameToSim.put("java.util.ResourceBundle", new JavaUtilResourceBundleNative(helper));
      this.cnameToSim.put("java.util.TimeZone", new JavaUtilTimeZoneNative(helper));
      this.cnameToSim.put("java.util.jar.JarFile", new JavaUtilJarJarFileNative(helper));
      this.cnameToSim.put("java.util.zip.CRC32", new JavaUtilZipCRC32Native(helper));
      this.cnameToSim.put("java.util.zip.Inflater", new JavaUtilZipInflaterNative(helper));
      this.cnameToSim.put("java.util.zip.ZipFile", new JavaUtilZipZipFileNative(helper));
      this.cnameToSim.put("java.util.zip.ZipEntry", new JavaUtilZipZipEntryNative(helper));
      this.cnameToSim.put("java.security.AccessController", new JavaSecurityAccessControllerNative(helper));
      this.cnameToSim.put("java.net.InetAddress", new JavaNetInetAddressNative(helper));
      this.cnameToSim.put("java.net.InetAddressImpl", new JavaNetInetAddressImplNative(helper));
      this.cnameToSim.put("sun.misc.Signal", new SunMiscSignalNative(helper));
      this.cnameToSim.put("sun.misc.NativeSignalHandler", new SunMiscSignalHandlerNative(helper));
      this.cnameToSim.put("sun.misc.Unsafe", new SunMiscUnsafeNative(helper));
   }

   public boolean process(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String cname = method.getDeclaringClass().getName();
      NativeMethodClass clsSim = (NativeMethodClass)this.cnameToSim.get(cname);
      if (clsSim == null) {
         return true;
      } else {
         try {
            clsSim.simulateMethod(method, thisVar, returnVar, params);
         } catch (NativeMethodNotSupportedException var8) {
         }

         return true;
      }
   }
}

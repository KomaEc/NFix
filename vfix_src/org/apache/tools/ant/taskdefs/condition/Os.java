package org.apache.tools.ant.taskdefs.condition;

import java.util.Locale;
import org.apache.tools.ant.BuildException;

public class Os implements Condition {
   private static final String OS_NAME;
   private static final String OS_ARCH;
   private static final String OS_VERSION;
   private static final String PATH_SEP;
   private String family;
   private String name;
   private String version;
   private String arch;
   public static final String FAMILY_WINDOWS = "windows";
   public static final String FAMILY_9X = "win9x";
   public static final String FAMILY_NT = "winnt";
   public static final String FAMILY_OS2 = "os/2";
   public static final String FAMILY_NETWARE = "netware";
   public static final String FAMILY_DOS = "dos";
   public static final String FAMILY_MAC = "mac";
   public static final String FAMILY_TANDEM = "tandem";
   public static final String FAMILY_UNIX = "unix";
   public static final String FAMILY_VMS = "openvms";
   public static final String FAMILY_ZOS = "z/os";
   public static final String FAMILY_OS400 = "os/400";

   public Os() {
   }

   public Os(String family) {
      this.setFamily(family);
   }

   public void setFamily(String f) {
      this.family = f.toLowerCase(Locale.US);
   }

   public void setName(String name) {
      this.name = name.toLowerCase(Locale.US);
   }

   public void setArch(String arch) {
      this.arch = arch.toLowerCase(Locale.US);
   }

   public void setVersion(String version) {
      this.version = version.toLowerCase(Locale.US);
   }

   public boolean eval() throws BuildException {
      return isOs(this.family, this.name, this.arch, this.version);
   }

   public static boolean isFamily(String family) {
      return isOs(family, (String)null, (String)null, (String)null);
   }

   public static boolean isName(String name) {
      return isOs((String)null, name, (String)null, (String)null);
   }

   public static boolean isArch(String arch) {
      return isOs((String)null, (String)null, arch, (String)null);
   }

   public static boolean isVersion(String version) {
      return isOs((String)null, (String)null, (String)null, version);
   }

   public static boolean isOs(String family, String name, String arch, String version) {
      boolean retValue = false;
      if (family != null || name != null || arch != null || version != null) {
         boolean isFamily = true;
         boolean isName = true;
         boolean isArch = true;
         boolean isVersion = true;
         if (family != null) {
            boolean isWindows = OS_NAME.indexOf("windows") > -1;
            boolean is9x = false;
            boolean isNT = false;
            if (isWindows) {
               is9x = OS_NAME.indexOf("95") >= 0 || OS_NAME.indexOf("98") >= 0 || OS_NAME.indexOf("me") >= 0 || OS_NAME.indexOf("ce") >= 0;
               isNT = !is9x;
            }

            if (family.equals("windows")) {
               isFamily = isWindows;
            } else if (family.equals("win9x")) {
               isFamily = isWindows && is9x;
            } else if (family.equals("winnt")) {
               isFamily = isWindows && isNT;
            } else if (family.equals("os/2")) {
               isFamily = OS_NAME.indexOf("os/2") > -1;
            } else if (family.equals("netware")) {
               isFamily = OS_NAME.indexOf("netware") > -1;
            } else if (family.equals("dos")) {
               isFamily = PATH_SEP.equals(";") && !isFamily("netware");
            } else if (family.equals("mac")) {
               isFamily = OS_NAME.indexOf("mac") > -1;
            } else if (family.equals("tandem")) {
               isFamily = OS_NAME.indexOf("nonstop_kernel") > -1;
            } else if (family.equals("unix")) {
               isFamily = PATH_SEP.equals(":") && !isFamily("openvms") && (!isFamily("mac") || OS_NAME.endsWith("x"));
            } else if (!family.equals("z/os")) {
               if (family.equals("os/400")) {
                  isFamily = OS_NAME.indexOf("os/400") > -1;
               } else {
                  if (!family.equals("openvms")) {
                     throw new BuildException("Don't know how to detect os family \"" + family + "\"");
                  }

                  isFamily = OS_NAME.indexOf("openvms") > -1;
               }
            } else {
               isFamily = OS_NAME.indexOf("z/os") > -1 || OS_NAME.indexOf("os/390") > -1;
            }
         }

         if (name != null) {
            isName = name.equals(OS_NAME);
         }

         if (arch != null) {
            isArch = arch.equals(OS_ARCH);
         }

         if (version != null) {
            isVersion = version.equals(OS_VERSION);
         }

         retValue = isFamily && isName && isArch && isVersion;
      }

      return retValue;
   }

   static {
      OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);
      OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.US);
      OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);
      PATH_SEP = System.getProperty("path.separator");
   }
}

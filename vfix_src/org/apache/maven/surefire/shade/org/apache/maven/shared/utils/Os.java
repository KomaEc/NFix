package org.apache.maven.surefire.shade.org.apache.maven.shared.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class Os {
   public static final String OS_NAME;
   public static final String OS_ARCH;
   public static final String OS_VERSION;
   public static final String PATH_SEP;
   public static final String LINE_SEP;
   public static final String OS_FAMILY;
   private static final Set<String> VALID_FAMILIES;
   private String family;
   public static final String FAMILY_WINDOWS = "windows";
   public static final String FAMILY_WIN9X = "win9x";
   public static final String FAMILY_NT = "winnt";
   public static final String FAMILY_OS2 = "os/2";
   public static final String FAMILY_NETWARE = "netware";
   public static final String FAMILY_DOS = "dos";
   public static final String FAMILY_MAC = "mac";
   public static final String FAMILY_TANDEM = "tandem";
   public static final String FAMILY_UNIX = "unix";
   public static final String FAMILY_OPENVMS = "openvms";
   public static final String FAMILY_ZOS = "z/os";
   public static final String FAMILY_OS400 = "os/400";
   private static final String DARWIN = "darwin";

   public static Set<String> getValidFamilies() {
      if (VALID_FAMILIES != null) {
         return VALID_FAMILIES;
      } else {
         Set<String> valid = new HashSet();
         valid.add("dos");
         valid.add("mac");
         valid.add("netware");
         valid.add("winnt");
         valid.add("openvms");
         valid.add("os/2");
         valid.add("os/400");
         valid.add("tandem");
         valid.add("unix");
         valid.add("win9x");
         valid.add("windows");
         valid.add("z/os");
         return Collections.unmodifiableSet(valid);
      }
   }

   public Os() {
   }

   public Os(String family) {
      this.setFamily(family);
   }

   private void setFamily(String f) {
      this.family = f.toLowerCase(Locale.ENGLISH);
   }

   boolean eval() {
      return isOs(this.family, (String)null, (String)null, (String)null);
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

   private static boolean isOs(String family, String name, String arch, String version) {
      boolean retValue = false;
      if (family != null || name != null || arch != null || version != null) {
         boolean isFamily = true;
         boolean isName = true;
         boolean isArch = true;
         boolean isVersion = true;
         if (family != null) {
            boolean isWindows = OS_NAME.contains("windows");
            boolean is9x = false;
            boolean isNT = false;
            if (isWindows) {
               is9x = OS_NAME.contains("95") || OS_NAME.contains("98") || OS_NAME.contains("me") || OS_NAME.contains("ce");
               isNT = !is9x;
            }

            if (family.equals("windows")) {
               isFamily = isWindows;
            } else if (family.equals("win9x")) {
               isFamily = isWindows && is9x;
            } else if (family.equals("winnt")) {
               isFamily = isWindows && isNT;
            } else if (family.equals("os/2")) {
               isFamily = OS_NAME.contains("os/2");
            } else if (family.equals("netware")) {
               isFamily = OS_NAME.contains("netware");
            } else if (family.equals("dos")) {
               isFamily = PATH_SEP.equals(";") && !isFamily("netware");
            } else if (family.equals("mac")) {
               isFamily = OS_NAME.contains("mac") || OS_NAME.contains("darwin");
            } else if (family.equals("tandem")) {
               isFamily = OS_NAME.contains("nonstop_kernel");
            } else if (family.equals("unix")) {
               isFamily = PATH_SEP.equals(":") && !isFamily("openvms") && (!isFamily("mac") || OS_NAME.endsWith("x") || OS_NAME.contains("darwin"));
            } else if (!family.equals("z/os")) {
               if (family.equals("os/400")) {
                  isFamily = OS_NAME.contains("os/400");
               } else if (family.equals("openvms")) {
                  isFamily = OS_NAME.contains("openvms");
               } else {
                  isFamily = OS_NAME.contains(family.toLowerCase(Locale.US));
               }
            } else {
               isFamily = OS_NAME.contains("z/os") || OS_NAME.contains("os/390");
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

   private static String getOsFamily() {
      Set<String> families = getValidFamilies();
      Iterator i$ = families.iterator();

      String fam;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         fam = (String)i$.next();
      } while(!isFamily(fam));

      return fam;
   }

   public static boolean isValidFamily(String family) {
      return VALID_FAMILIES.contains(family);
   }

   static {
      OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
      OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
      OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
      PATH_SEP = System.getProperty("path.separator");
      LINE_SEP = System.getProperty("line.separator");
      OS_FAMILY = getOsFamily();
      VALID_FAMILIES = getValidFamilies();
   }
}

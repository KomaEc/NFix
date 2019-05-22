package com.mks.api.util;

import com.mks.api.VersionNumber;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class APIVersion implements VersionNumber {
   private static String apiReleaseVersion;
   private int majorVersion;
   private int minorVersion;

   public static String getAPIReleaseVersion() {
      if (apiReleaseVersion == null) {
         try {
            ResourceBundle rb = ResourceBundle.getBundle("com.mks.api.version");
            apiReleaseVersion = rb.getString("MKS_API_VERSION");
         } catch (Throwable var1) {
         }
      }

      return apiReleaseVersion;
   }

   public static String format(int majorVersion, int minorVersion) {
      return majorVersion <= 0 ? getAPIReleaseVersion() : MessageFormat.format("{0,number,#}.{1,number,#} 000-00 0", new Integer(majorVersion), new Integer(minorVersion));
   }

   public APIVersion(int major, int minor) {
      this.majorVersion = major;
      this.minorVersion = minor;
   }

   public int getMajor() {
      return this.majorVersion;
   }

   public int getMinor() {
      return this.minorVersion;
   }

   public String toVersionString() {
      return format(this.majorVersion, this.minorVersion);
   }

   public String toString() {
      return this.toVersionString();
   }
}

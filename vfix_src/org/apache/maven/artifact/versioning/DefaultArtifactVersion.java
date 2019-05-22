package org.apache.maven.artifact.versioning;

import java.util.StringTokenizer;

public class DefaultArtifactVersion implements ArtifactVersion {
   private Integer majorVersion;
   private Integer minorVersion;
   private Integer incrementalVersion;
   private Integer buildNumber;
   private String qualifier;
   private String unparsed;

   public DefaultArtifactVersion(String version) {
      this.parseVersion(version);
   }

   public int compareTo(Object o) {
      ArtifactVersion otherVersion = (ArtifactVersion)o;
      int result = this.getMajorVersion() - otherVersion.getMajorVersion();
      if (result == 0) {
         result = this.getMinorVersion() - otherVersion.getMinorVersion();
      }

      if (result == 0) {
         result = this.getIncrementalVersion() - otherVersion.getIncrementalVersion();
      }

      if (result == 0) {
         if (this.qualifier != null) {
            String otherQualifier = otherVersion.getQualifier();
            if (otherQualifier != null) {
               if (this.qualifier.length() > otherQualifier.length() && this.qualifier.startsWith(otherQualifier)) {
                  result = -1;
               } else if (this.qualifier.length() < otherQualifier.length() && otherQualifier.startsWith(this.qualifier)) {
                  result = 1;
               } else {
                  result = this.qualifier.compareTo(otherQualifier);
               }
            } else {
               result = -1;
            }
         } else if (otherVersion.getQualifier() != null) {
            result = 1;
         } else {
            result = this.getBuildNumber() - otherVersion.getBuildNumber();
         }
      }

      return result;
   }

   public int getMajorVersion() {
      return this.majorVersion != null ? this.majorVersion : 0;
   }

   public int getMinorVersion() {
      return this.minorVersion != null ? this.minorVersion : 0;
   }

   public int getIncrementalVersion() {
      return this.incrementalVersion != null ? this.incrementalVersion : 0;
   }

   public int getBuildNumber() {
      return this.buildNumber != null ? this.buildNumber : 0;
   }

   public String getQualifier() {
      return this.qualifier;
   }

   public final void parseVersion(String version) {
      this.unparsed = version;
      int index = version.indexOf("-");
      String part2 = null;
      String part1;
      if (index < 0) {
         part1 = version;
      } else {
         part1 = version.substring(0, index);
         part2 = version.substring(index + 1);
      }

      if (part2 != null) {
         try {
            if (part2.length() != 1 && part2.startsWith("0")) {
               this.qualifier = part2;
            } else {
               this.buildNumber = Integer.valueOf(part2);
            }
         } catch (NumberFormatException var10) {
            this.qualifier = part2;
         }
      }

      if (part1.indexOf(".") < 0 && !part1.startsWith("0")) {
         try {
            this.majorVersion = Integer.valueOf(part1);
         } catch (NumberFormatException var8) {
            this.qualifier = version;
            this.buildNumber = null;
         }
      } else {
         boolean fallback = false;
         StringTokenizer tok = new StringTokenizer(part1, ".");

         try {
            this.majorVersion = getNextIntegerToken(tok);
            if (tok.hasMoreTokens()) {
               this.minorVersion = getNextIntegerToken(tok);
            }

            if (tok.hasMoreTokens()) {
               this.incrementalVersion = getNextIntegerToken(tok);
            }

            if (tok.hasMoreTokens()) {
               fallback = true;
            }

            if (part1.indexOf("..") >= 0 || part1.startsWith(".") || part1.endsWith(".")) {
               fallback = true;
            }
         } catch (NumberFormatException var9) {
            fallback = true;
         }

         if (fallback) {
            this.qualifier = version;
            this.majorVersion = null;
            this.minorVersion = null;
            this.incrementalVersion = null;
            this.buildNumber = null;
         }
      }

   }

   private static Integer getNextIntegerToken(StringTokenizer tok) {
      String s = tok.nextToken();
      if (s.length() > 1 && s.startsWith("0")) {
         throw new NumberFormatException("Number part has a leading 0: '" + s + "'");
      } else {
         return Integer.valueOf(s);
      }
   }

   public String toString() {
      return this.unparsed;
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof ArtifactVersion)) {
         return false;
      } else {
         return 0 == this.compareTo(other);
      }
   }

   public int hashCode() {
      int result = 1229;
      int result = 1223 * result + this.getMajorVersion();
      result = 1223 * result + this.getMinorVersion();
      result = 1223 * result + this.getIncrementalVersion();
      result = 1223 * result + this.getBuildNumber();
      if (null != this.getQualifier()) {
         result = 1223 * result + this.getQualifier().hashCode();
      }

      return result;
   }
}

package org.jf.dexlib2;

public class VersionMap {
   public static final int NO_VERSION = -1;

   public static int mapArtVersionToApi(int artVersion) {
      if (artVersion >= 87) {
         return 26;
      } else if (artVersion >= 79) {
         return 24;
      } else if (artVersion >= 64) {
         return 23;
      } else if (artVersion >= 45) {
         return 22;
      } else {
         return artVersion >= 39 ? 21 : 19;
      }
   }

   public static int mapApiToArtVersion(int api) {
      switch(api) {
      case 19:
      case 20:
         return 7;
      case 21:
         return 39;
      case 22:
         return 45;
      case 23:
         return 64;
      case 24:
      case 25:
         return 79;
      default:
         return api > 25 ? 87 : -1;
      }
   }
}

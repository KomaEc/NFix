package org.jf.dexlib2;

import java.util.HashMap;

public enum AccessFlags {
   PUBLIC(1, "public", true, true, true),
   PRIVATE(2, "private", true, true, true),
   PROTECTED(4, "protected", true, true, true),
   STATIC(8, "static", true, true, true),
   FINAL(16, "final", true, true, true),
   SYNCHRONIZED(32, "synchronized", false, true, false),
   VOLATILE(64, "volatile", false, false, true),
   BRIDGE(64, "bridge", false, true, false),
   TRANSIENT(128, "transient", false, false, true),
   VARARGS(128, "varargs", false, true, false),
   NATIVE(256, "native", false, true, false),
   INTERFACE(512, "interface", true, false, false),
   ABSTRACT(1024, "abstract", true, true, false),
   STRICTFP(2048, "strictfp", false, true, false),
   SYNTHETIC(4096, "synthetic", true, true, true),
   ANNOTATION(8192, "annotation", true, false, false),
   ENUM(16384, "enum", true, false, true),
   CONSTRUCTOR(65536, "constructor", false, true, false),
   DECLARED_SYNCHRONIZED(131072, "declared-synchronized", false, true, false);

   private int value;
   private String accessFlagName;
   private boolean validForClass;
   private boolean validForMethod;
   private boolean validForField;
   private static final AccessFlags[] allFlags = values();
   private static HashMap<String, AccessFlags> accessFlagsByName = new HashMap();

   private AccessFlags(int value, String accessFlagName, boolean validForClass, boolean validForMethod, boolean validForField) {
      this.value = value;
      this.accessFlagName = accessFlagName;
      this.validForClass = validForClass;
      this.validForMethod = validForMethod;
      this.validForField = validForField;
   }

   public boolean isSet(int accessFlags) {
      return (this.value & accessFlags) != 0;
   }

   public static AccessFlags[] getAccessFlagsForClass(int accessFlagValue) {
      int size = 0;
      AccessFlags[] accessFlags = allFlags;
      int accessFlagsPosition = accessFlags.length;

      for(int var4 = 0; var4 < accessFlagsPosition; ++var4) {
         AccessFlags accessFlag = accessFlags[var4];
         if (accessFlag.validForClass && (accessFlagValue & accessFlag.value) != 0) {
            ++size;
         }
      }

      accessFlags = new AccessFlags[size];
      accessFlagsPosition = 0;
      AccessFlags[] var8 = allFlags;
      int var9 = var8.length;

      for(int var6 = 0; var6 < var9; ++var6) {
         AccessFlags accessFlag = var8[var6];
         if (accessFlag.validForClass && (accessFlagValue & accessFlag.value) != 0) {
            accessFlags[accessFlagsPosition++] = accessFlag;
         }
      }

      return accessFlags;
   }

   private static String formatAccessFlags(AccessFlags[] accessFlags) {
      int size = 0;
      AccessFlags[] var2 = accessFlags;
      int var3 = accessFlags.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         AccessFlags accessFlag = var2[var4];
         size += accessFlag.toString().length() + 1;
      }

      StringBuilder sb = new StringBuilder(size);
      AccessFlags[] var8 = accessFlags;
      var4 = accessFlags.length;

      for(int var9 = 0; var9 < var4; ++var9) {
         AccessFlags accessFlag = var8[var9];
         sb.append(accessFlag.toString());
         sb.append(" ");
      }

      if (accessFlags.length > 0) {
         sb.delete(sb.length() - 1, sb.length());
      }

      return sb.toString();
   }

   public static String formatAccessFlagsForClass(int accessFlagValue) {
      return formatAccessFlags(getAccessFlagsForClass(accessFlagValue));
   }

   public static AccessFlags[] getAccessFlagsForMethod(int accessFlagValue) {
      int size = 0;
      AccessFlags[] accessFlags = allFlags;
      int accessFlagsPosition = accessFlags.length;

      for(int var4 = 0; var4 < accessFlagsPosition; ++var4) {
         AccessFlags accessFlag = accessFlags[var4];
         if (accessFlag.validForMethod && (accessFlagValue & accessFlag.value) != 0) {
            ++size;
         }
      }

      accessFlags = new AccessFlags[size];
      accessFlagsPosition = 0;
      AccessFlags[] var8 = allFlags;
      int var9 = var8.length;

      for(int var6 = 0; var6 < var9; ++var6) {
         AccessFlags accessFlag = var8[var6];
         if (accessFlag.validForMethod && (accessFlagValue & accessFlag.value) != 0) {
            accessFlags[accessFlagsPosition++] = accessFlag;
         }
      }

      return accessFlags;
   }

   public static String formatAccessFlagsForMethod(int accessFlagValue) {
      return formatAccessFlags(getAccessFlagsForMethod(accessFlagValue));
   }

   public static AccessFlags[] getAccessFlagsForField(int accessFlagValue) {
      int size = 0;
      AccessFlags[] accessFlags = allFlags;
      int accessFlagsPosition = accessFlags.length;

      for(int var4 = 0; var4 < accessFlagsPosition; ++var4) {
         AccessFlags accessFlag = accessFlags[var4];
         if (accessFlag.validForField && (accessFlagValue & accessFlag.value) != 0) {
            ++size;
         }
      }

      accessFlags = new AccessFlags[size];
      accessFlagsPosition = 0;
      AccessFlags[] var8 = allFlags;
      int var9 = var8.length;

      for(int var6 = 0; var6 < var9; ++var6) {
         AccessFlags accessFlag = var8[var6];
         if (accessFlag.validForField && (accessFlagValue & accessFlag.value) != 0) {
            accessFlags[accessFlagsPosition++] = accessFlag;
         }
      }

      return accessFlags;
   }

   public static String formatAccessFlagsForField(int accessFlagValue) {
      return formatAccessFlags(getAccessFlagsForField(accessFlagValue));
   }

   public static AccessFlags getAccessFlag(String accessFlag) {
      return (AccessFlags)accessFlagsByName.get(accessFlag);
   }

   public int getValue() {
      return this.value;
   }

   public String toString() {
      return this.accessFlagName;
   }

   static {
      AccessFlags[] var0 = allFlags;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         AccessFlags accessFlag = var0[var2];
         accessFlagsByName.put(accessFlag.accessFlagName, accessFlag);
      }

   }
}

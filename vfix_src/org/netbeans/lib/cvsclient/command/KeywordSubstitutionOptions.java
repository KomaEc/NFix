package org.netbeans.lib.cvsclient.command;

public final class KeywordSubstitutionOptions {
   public static final KeywordSubstitutionOptions DEFAULT = new KeywordSubstitutionOptions("kv");
   public static final KeywordSubstitutionOptions DEFAULT_LOCKER = new KeywordSubstitutionOptions("kvl");
   public static final KeywordSubstitutionOptions ONLY_KEYWORDS = new KeywordSubstitutionOptions("k");
   public static final KeywordSubstitutionOptions ONLY_VALUES = new KeywordSubstitutionOptions("v");
   public static final KeywordSubstitutionOptions OLD_VALUES = new KeywordSubstitutionOptions("o");
   public static final KeywordSubstitutionOptions BINARY = new KeywordSubstitutionOptions("b");
   private String value;

   public static KeywordSubstitutionOptions findKeywordSubstOption(String var0) {
      if (BINARY.toString().equals(var0)) {
         return BINARY;
      } else if (DEFAULT.toString().equals(var0)) {
         return DEFAULT;
      } else if (DEFAULT_LOCKER.toString().equals(var0)) {
         return DEFAULT_LOCKER;
      } else if (OLD_VALUES.toString().equals(var0)) {
         return OLD_VALUES;
      } else if (ONLY_KEYWORDS.toString().equals(var0)) {
         return ONLY_KEYWORDS;
      } else {
         return ONLY_VALUES.toString().equals(var0) ? ONLY_VALUES : null;
      }
   }

   private KeywordSubstitutionOptions(String var1) {
      this.value = var1;
   }

   public String toString() {
      return this.value;
   }
}

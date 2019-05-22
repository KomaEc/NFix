package com.gzoltar.shaded.jline.console.completer;

import com.gzoltar.shaded.jline.internal.Preconditions;

public class EnumCompleter extends StringsCompleter {
   public EnumCompleter(Class<? extends Enum<?>> source) {
      Preconditions.checkNotNull(source);
      Enum[] var2 = (Enum[])source.getEnumConstants();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Enum<?> n = var2[var4];
         this.getStrings().add(n.name().toLowerCase());
      }

   }
}

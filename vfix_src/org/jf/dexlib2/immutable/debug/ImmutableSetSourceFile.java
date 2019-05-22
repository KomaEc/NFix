package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.reference.StringReference;

public class ImmutableSetSourceFile extends ImmutableDebugItem implements SetSourceFile {
   @Nullable
   protected final String sourceFile;

   public ImmutableSetSourceFile(int codeAddress, @Nullable String sourceFile) {
      super(codeAddress);
      this.sourceFile = sourceFile;
   }

   @Nonnull
   public static ImmutableSetSourceFile of(@Nonnull SetSourceFile setSourceFile) {
      return setSourceFile instanceof ImmutableSetSourceFile ? (ImmutableSetSourceFile)setSourceFile : new ImmutableSetSourceFile(setSourceFile.getCodeAddress(), setSourceFile.getSourceFile());
   }

   @Nullable
   public String getSourceFile() {
      return this.sourceFile;
   }

   @Nullable
   public StringReference getSourceFileReference() {
      return this.sourceFile == null ? null : new BaseStringReference() {
         @Nonnull
         public String getString() {
            return ImmutableSetSourceFile.this.sourceFile;
         }
      };
   }

   public int getDebugItemType() {
      return 9;
   }
}

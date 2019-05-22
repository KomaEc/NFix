package org.jf.dexlib2.builder.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.reference.StringReference;

public class BuilderSetSourceFile extends BuilderDebugItem implements SetSourceFile {
   @Nullable
   private final StringReference sourceFile;

   public BuilderSetSourceFile(@Nullable StringReference sourceFile) {
      this.sourceFile = sourceFile;
   }

   public int getDebugItemType() {
      return 9;
   }

   @Nullable
   public String getSourceFile() {
      return this.sourceFile == null ? null : this.sourceFile.getString();
   }

   @Nullable
   public StringReference getSourceFileReference() {
      return this.sourceFile;
   }
}

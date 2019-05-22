package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.util.ImmutableUtils;

public class ImmutableDexFile implements DexFile {
   @Nonnull
   protected final ImmutableSet<? extends ImmutableClassDef> classes;
   @Nonnull
   private final Opcodes opcodes;

   public ImmutableDexFile(@Nonnull Opcodes opcodes, @Nullable Collection<? extends ClassDef> classes) {
      this.classes = ImmutableClassDef.immutableSetOf(classes);
      this.opcodes = opcodes;
   }

   public ImmutableDexFile(@Nonnull Opcodes opcodes, @Nullable ImmutableSet<? extends ImmutableClassDef> classes) {
      this.classes = ImmutableUtils.nullToEmptySet(classes);
      this.opcodes = opcodes;
   }

   public static ImmutableDexFile of(DexFile dexFile) {
      return dexFile instanceof ImmutableDexFile ? (ImmutableDexFile)dexFile : new ImmutableDexFile(dexFile.getOpcodes(), dexFile.getClasses());
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableClassDef> getClasses() {
      return this.classes;
   }

   @Nonnull
   public Opcodes getOpcodes() {
      return this.opcodes;
   }
}

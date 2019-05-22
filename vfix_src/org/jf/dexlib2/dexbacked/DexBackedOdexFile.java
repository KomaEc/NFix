package org.jf.dexlib2.dexbacked;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.raw.OdexHeaderItem;
import org.jf.dexlib2.dexbacked.util.VariableSizeList;
import org.jf.dexlib2.util.DexUtil;

public class DexBackedOdexFile extends DexBackedDexFile {
   private static final int DEPENDENCY_COUNT_OFFSET = 12;
   private static final int DEPENDENCY_START_OFFSET = 16;
   private final byte[] odexBuf;

   public DexBackedOdexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] odexBuf, byte[] dexBuf) {
      super(opcodes, dexBuf);
      this.odexBuf = odexBuf;
   }

   public boolean isOdexFile() {
      return true;
   }

   public boolean hasOdexOpcodes() {
      return true;
   }

   @Nonnull
   public List<String> getDependencies() {
      int dexOffset = OdexHeaderItem.getDexOffset(this.odexBuf);
      int dependencyOffset = OdexHeaderItem.getDependenciesOffset(this.odexBuf) - dexOffset;
      BaseDexBuffer buf = new BaseDexBuffer(this.buf);
      int dependencyCount = buf.readInt(dependencyOffset + 12);
      return new VariableSizeList<String>(this, dependencyOffset + 16, dependencyCount) {
         protected String readNextItem(@Nonnull DexReader reader, int index) {
            int length = reader.readInt();
            int offset = reader.getOffset();
            reader.moveRelative(length + 20);

            try {
               return new String(DexBackedOdexFile.this.buf, offset, length - 1, "US-ASCII");
            } catch (UnsupportedEncodingException var6) {
               throw new RuntimeException(var6);
            }
         }
      };
   }

   @Nonnull
   public static DexBackedOdexFile fromInputStream(@Nonnull Opcodes opcodes, @Nonnull InputStream is) throws IOException {
      DexUtil.verifyOdexHeader(is);
      is.reset();
      byte[] odexBuf = new byte[40];
      ByteStreams.readFully(is, odexBuf);
      int dexOffset = OdexHeaderItem.getDexOffset(odexBuf);
      if (dexOffset > 40) {
         ByteStreams.skipFully(is, (long)(dexOffset - 40));
      }

      byte[] dexBuf = ByteStreams.toByteArray(is);
      return new DexBackedOdexFile(opcodes, odexBuf, dexBuf);
   }

   public int getOdexVersion() {
      return OdexHeaderItem.getVersion(this.odexBuf, 0);
   }

   public static class NotAnOdexFile extends RuntimeException {
      public NotAnOdexFile() {
      }

      public NotAnOdexFile(Throwable cause) {
         super(cause);
      }

      public NotAnOdexFile(String message) {
         super(message);
      }

      public NotAnOdexFile(String message, Throwable cause) {
         super(message, cause);
      }
   }
}

package org.jf.dexlib2.dexbacked;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.io.ByteStreams;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.util.AbstractForwardSequentialList;

public class OatFile extends BaseDexBuffer implements MultiDexContainer<OatFile.OatDexFile> {
   private static final byte[] ELF_MAGIC = new byte[]{127, 69, 76, 70};
   private static final byte[] OAT_MAGIC = new byte[]{111, 97, 116, 10};
   private static final int MIN_ELF_HEADER_SIZE = 52;
   private static final int MIN_OAT_VERSION = 56;
   private static final int MAX_OAT_VERSION = 86;
   public static final int UNSUPPORTED = 0;
   public static final int SUPPORTED = 1;
   public static final int UNKNOWN = 2;
   private final boolean is64bit;
   @Nonnull
   private final OatFile.OatHeader oatHeader;
   @Nonnull
   private final Opcodes opcodes;
   @Nullable
   private final OatFile.VdexProvider vdexProvider;

   public OatFile(@Nonnull byte[] buf) {
      this(buf, (OatFile.VdexProvider)null);
   }

   public OatFile(@Nonnull byte[] buf, @Nullable OatFile.VdexProvider vdexProvider) {
      super(buf);
      if (buf.length < 52) {
         throw new OatFile.NotAnOatFileException();
      } else {
         verifyMagic(buf);
         if (buf[4] == 1) {
            this.is64bit = false;
         } else {
            if (buf[4] != 2) {
               throw new OatFile.InvalidOatFileException(String.format("Invalid word-size value: %x", buf[5]));
            }

            this.is64bit = true;
         }

         OatFile.OatHeader oatHeader = null;
         OatFile.SymbolTable symbolTable = this.getSymbolTable();
         Iterator var5 = symbolTable.getSymbols().iterator();

         while(var5.hasNext()) {
            OatFile.SymbolTable.Symbol symbol = (OatFile.SymbolTable.Symbol)var5.next();
            if (symbol.getName().equals("oatdata")) {
               oatHeader = new OatFile.OatHeader(symbol.getFileOffset());
               break;
            }
         }

         if (oatHeader == null) {
            throw new OatFile.InvalidOatFileException("Oat file has no oatdata symbol");
         } else {
            this.oatHeader = oatHeader;
            if (!oatHeader.isValid()) {
               throw new OatFile.InvalidOatFileException("Invalid oat magic value");
            } else {
               this.opcodes = Opcodes.forArtVersion(oatHeader.getVersion());
               this.vdexProvider = vdexProvider;
            }
         }
      }
   }

   private static void verifyMagic(byte[] buf) {
      for(int i = 0; i < ELF_MAGIC.length; ++i) {
         if (buf[i] != ELF_MAGIC[i]) {
            throw new OatFile.NotAnOatFileException();
         }
      }

   }

   public static OatFile fromInputStream(@Nonnull InputStream is) throws IOException {
      return fromInputStream(is, (OatFile.VdexProvider)null);
   }

   public static OatFile fromInputStream(@Nonnull InputStream is, @Nullable OatFile.VdexProvider vdexProvider) throws IOException {
      if (!is.markSupported()) {
         throw new IllegalArgumentException("InputStream must support mark");
      } else {
         is.mark(4);
         byte[] partialHeader = new byte[4];

         try {
            ByteStreams.readFully(is, partialHeader);
         } catch (EOFException var7) {
            throw new OatFile.NotAnOatFileException();
         } finally {
            is.reset();
         }

         verifyMagic(partialHeader);
         is.reset();
         byte[] buf = ByteStreams.toByteArray(is);
         return new OatFile(buf, vdexProvider);
      }
   }

   public int getOatVersion() {
      return this.oatHeader.getVersion();
   }

   public int isSupportedVersion() {
      int version = this.getOatVersion();
      if (version < 56) {
         return 0;
      } else {
         return version <= 86 ? 1 : 2;
      }
   }

   @Nonnull
   public List<String> getBootClassPath() {
      if (this.getOatVersion() < 75) {
         return ImmutableList.of();
      } else {
         String bcp = this.oatHeader.getKeyValue("bootclasspath");
         return (List)(bcp == null ? ImmutableList.of() : Arrays.asList(bcp.split(":")));
      }
   }

   @Nonnull
   public Opcodes getOpcodes() {
      return this.opcodes;
   }

   @Nonnull
   public List<OatFile.OatDexFile> getDexFiles() {
      return new AbstractForwardSequentialList<OatFile.OatDexFile>() {
         public int size() {
            return OatFile.this.oatHeader.getDexFileCount();
         }

         @Nonnull
         public Iterator<OatFile.OatDexFile> iterator() {
            return Iterators.transform(OatFile.this.new DexEntryIterator(), new Function<OatFile.DexEntry, OatFile.OatDexFile>() {
               @Nullable
               public OatFile.OatDexFile apply(OatFile.DexEntry dexEntry) {
                  return dexEntry.getDexFile();
               }
            });
         }
      };
   }

   @Nonnull
   public List<String> getDexEntryNames() throws IOException {
      return new AbstractForwardSequentialList<String>() {
         public int size() {
            return OatFile.this.oatHeader.getDexFileCount();
         }

         @Nonnull
         public Iterator<String> iterator() {
            return Iterators.transform(OatFile.this.new DexEntryIterator(), new Function<OatFile.DexEntry, String>() {
               @Nullable
               public String apply(OatFile.DexEntry dexEntry) {
                  return dexEntry.entryName;
               }
            });
         }
      };
   }

   @Nullable
   public OatFile.OatDexFile getEntry(@Nonnull String entryName) throws IOException {
      OatFile.DexEntryIterator iterator = new OatFile.DexEntryIterator();

      OatFile.DexEntry entry;
      do {
         if (!iterator.hasNext()) {
            return null;
         }

         entry = iterator.next();
      } while(!entry.entryName.equals(entryName));

      return entry.getDexFile();
   }

   @Nonnull
   private List<OatFile.SectionHeader> getSections() {
      final int offset;
      final int entrySize;
      final int entryCount;
      if (this.is64bit) {
         offset = this.readLongAsSmallUint(40);
         entrySize = this.readUshort(58);
         entryCount = this.readUshort(60);
      } else {
         offset = this.readSmallUint(32);
         entrySize = this.readUshort(46);
         entryCount = this.readUshort(48);
      }

      if (offset + entrySize * entryCount > this.buf.length) {
         throw new OatFile.InvalidOatFileException("The ELF section headers extend past the end of the file");
      } else {
         return new AbstractList<OatFile.SectionHeader>() {
            public OatFile.SectionHeader get(int index) {
               if (index >= 0 && index < entryCount) {
                  return (OatFile.SectionHeader)(OatFile.this.is64bit ? OatFile.this.new SectionHeader64Bit(offset + index * entrySize) : OatFile.this.new SectionHeader32Bit(offset + index * entrySize));
               } else {
                  throw new IndexOutOfBoundsException();
               }
            }

            public int size() {
               return entryCount;
            }
         };
      }
   }

   @Nonnull
   private OatFile.SymbolTable getSymbolTable() {
      Iterator var1 = this.getSections().iterator();

      OatFile.SectionHeader header;
      do {
         if (!var1.hasNext()) {
            throw new OatFile.InvalidOatFileException("Oat file has no symbol table");
         }

         header = (OatFile.SectionHeader)var1.next();
      } while(header.getType() != 11);

      return new OatFile.SymbolTable(header);
   }

   @Nonnull
   private OatFile.StringTable getSectionNameStringTable() {
      int index = this.readUshort(50);
      if (index == 0) {
         throw new OatFile.InvalidOatFileException("There is no section name string table");
      } else {
         try {
            return new OatFile.StringTable((OatFile.SectionHeader)this.getSections().get(index));
         } catch (IndexOutOfBoundsException var3) {
            throw new OatFile.InvalidOatFileException("The section index for the section name string table is invalid");
         }
      }
   }

   public interface VdexProvider {
      @Nullable
      byte[] getVdex();
   }

   public static class NotAnOatFileException extends RuntimeException {
   }

   public static class InvalidOatFileException extends RuntimeException {
      public InvalidOatFileException(String message) {
         super(message);
      }
   }

   private class DexEntryIterator implements Iterator<OatFile.DexEntry> {
      int index;
      int offset;

      private DexEntryIterator() {
         this.index = 0;
         this.offset = OatFile.this.oatHeader.getDexListStart();
      }

      public boolean hasNext() {
         return this.index < OatFile.this.oatHeader.getDexFileCount();
      }

      public OatFile.DexEntry next() {
         int filenameLength = OatFile.this.readSmallUint(this.offset);
         this.offset += 4;
         String filename = new String(OatFile.this.buf, this.offset, filenameLength, Charset.forName("US-ASCII"));
         this.offset += filenameLength;
         this.offset += 4;
         int dexOffset = OatFile.this.readSmallUint(this.offset);
         this.offset += 4;
         byte[] buf;
         if (OatFile.this.getOatVersion() >= 87 && OatFile.this.vdexProvider != null && OatFile.this.vdexProvider.getVdex() != null) {
            buf = OatFile.this.vdexProvider.getVdex();
         } else {
            buf = OatFile.this.buf;
            dexOffset += OatFile.this.oatHeader.headerOffset;
         }

         if (OatFile.this.getOatVersion() >= 75) {
            this.offset += 4;
         }

         if (OatFile.this.getOatVersion() >= 73) {
            this.offset += 4;
         }

         if (OatFile.this.getOatVersion() < 75) {
            int classCount = OatFile.this.readSmallUint(dexOffset + 96);
            this.offset += 4 * classCount;
         }

         ++this.index;
         return OatFile.this.new DexEntry(filename, buf, dexOffset);
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      DexEntryIterator(Object x1) {
         this();
      }
   }

   private class DexEntry {
      public final String entryName;
      public final byte[] buf;
      public final int dexOffset;

      public DexEntry(String entryName, byte[] buf, int dexOffset) {
         this.entryName = entryName;
         this.buf = buf;
         this.dexOffset = dexOffset;
      }

      public OatFile.OatDexFile getDexFile() {
         return OatFile.this.new OatDexFile(this.buf, this.dexOffset, this.entryName);
      }
   }

   private class StringTable {
      private final int offset;
      private final int size;

      public StringTable(@Nonnull OatFile.SectionHeader header) {
         this.offset = header.getOffset();
         this.size = header.getSize();
         if (this.offset + this.size > OatFile.this.buf.length) {
            throw new OatFile.InvalidOatFileException("String table extends past end of file");
         }
      }

      @Nonnull
      public String getString(int index) {
         if (index >= this.size) {
            throw new OatFile.InvalidOatFileException("String index is out of bounds");
         } else {
            int start = this.offset + index;
            int end = start;

            do {
               if (OatFile.this.buf[end] == 0) {
                  return new String(OatFile.this.buf, start, end - start, Charset.forName("US-ASCII"));
               }

               ++end;
            } while(end < this.offset + this.size);

            throw new OatFile.InvalidOatFileException("String extends past end of string table");
         }
      }
   }

   class SymbolTable {
      @Nonnull
      private final OatFile.StringTable stringTable;
      private final int offset;
      private final int entryCount;
      private final int entrySize;

      public SymbolTable(@Nonnull OatFile.SectionHeader header) {
         try {
            this.stringTable = OatFile.this.new StringTable((OatFile.SectionHeader)OatFile.this.getSections().get(header.getLink()));
         } catch (IndexOutOfBoundsException var4) {
            throw new OatFile.InvalidOatFileException("String table section index is invalid");
         }

         this.offset = header.getOffset();
         this.entrySize = header.getEntrySize();
         this.entryCount = header.getSize() / this.entrySize;
         if (this.offset + this.entryCount * this.entrySize > OatFile.this.buf.length) {
            throw new OatFile.InvalidOatFileException("Symbol table extends past end of file");
         }
      }

      @Nonnull
      public List<OatFile.SymbolTable.Symbol> getSymbols() {
         return new AbstractList<OatFile.SymbolTable.Symbol>() {
            public OatFile.SymbolTable.Symbol get(int index) {
               if (index >= 0 && index < SymbolTable.this.entryCount) {
                  return (OatFile.SymbolTable.Symbol)(OatFile.this.is64bit ? SymbolTable.this.new Symbol64(SymbolTable.this.offset + index * SymbolTable.this.entrySize) : SymbolTable.this.new Symbol32(SymbolTable.this.offset + index * SymbolTable.this.entrySize));
               } else {
                  throw new IndexOutOfBoundsException();
               }
            }

            public int size() {
               return SymbolTable.this.entryCount;
            }
         };
      }

      public class Symbol64 extends OatFile.SymbolTable.Symbol {
         public Symbol64(int offset) {
            super(offset);
         }

         @Nonnull
         public String getName() {
            return SymbolTable.this.stringTable.getString(OatFile.this.readSmallUint(this.offset));
         }

         public long getValue() {
            return OatFile.this.readLong(this.offset + 8);
         }

         public int getSize() {
            return OatFile.this.readLongAsSmallUint(this.offset + 16);
         }

         public int getSectionIndex() {
            return OatFile.this.readUshort(this.offset + 6);
         }
      }

      public class Symbol32 extends OatFile.SymbolTable.Symbol {
         public Symbol32(int offset) {
            super(offset);
         }

         @Nonnull
         public String getName() {
            return SymbolTable.this.stringTable.getString(OatFile.this.readSmallUint(this.offset));
         }

         public long getValue() {
            return (long)OatFile.this.readSmallUint(this.offset + 4);
         }

         public int getSize() {
            return OatFile.this.readSmallUint(this.offset + 8);
         }

         public int getSectionIndex() {
            return OatFile.this.readUshort(this.offset + 14);
         }
      }

      public abstract class Symbol {
         protected final int offset;

         public Symbol(int offset) {
            this.offset = offset;
         }

         @Nonnull
         public abstract String getName();

         public abstract long getValue();

         public abstract int getSize();

         public abstract int getSectionIndex();

         public int getFileOffset() {
            OatFile.SectionHeader sectionHeader;
            try {
               sectionHeader = (OatFile.SectionHeader)OatFile.this.getSections().get(this.getSectionIndex());
            } catch (IndexOutOfBoundsException var10) {
               throw new OatFile.InvalidOatFileException("Section index for symbol is out of bounds");
            }

            long sectionAddress = sectionHeader.getAddress();
            int sectionOffset = sectionHeader.getOffset();
            int sectionSize = sectionHeader.getSize();
            long symbolAddress = this.getValue();
            if (symbolAddress >= sectionAddress && symbolAddress < sectionAddress + (long)sectionSize) {
               long fileOffset = (long)sectionOffset + (this.getValue() - sectionAddress);

               assert fileOffset <= 2147483647L;

               return (int)fileOffset;
            } else {
               throw new OatFile.InvalidOatFileException("symbol address lies outside it's associated section");
            }
         }
      }
   }

   private class SectionHeader64Bit extends OatFile.SectionHeader {
      public SectionHeader64Bit(int offset) {
         super(offset);
      }

      public long getAddress() {
         return OatFile.this.readLong(this.offset + 16);
      }

      public int getOffset() {
         return OatFile.this.readLongAsSmallUint(this.offset + 24);
      }

      public int getSize() {
         return OatFile.this.readLongAsSmallUint(this.offset + 32);
      }

      public int getLink() {
         return OatFile.this.readSmallUint(this.offset + 40);
      }

      public int getEntrySize() {
         return OatFile.this.readLongAsSmallUint(this.offset + 56);
      }
   }

   private class SectionHeader32Bit extends OatFile.SectionHeader {
      public SectionHeader32Bit(int offset) {
         super(offset);
      }

      public long getAddress() {
         return (long)OatFile.this.readInt(this.offset + 12) & 4294967295L;
      }

      public int getOffset() {
         return OatFile.this.readSmallUint(this.offset + 16);
      }

      public int getSize() {
         return OatFile.this.readSmallUint(this.offset + 20);
      }

      public int getLink() {
         return OatFile.this.readSmallUint(this.offset + 24);
      }

      public int getEntrySize() {
         return OatFile.this.readSmallUint(this.offset + 36);
      }
   }

   private abstract class SectionHeader {
      protected final int offset;
      public static final int TYPE_DYNAMIC_SYMBOL_TABLE = 11;

      public SectionHeader(int offset) {
         this.offset = offset;
      }

      @Nonnull
      public String getName() {
         return OatFile.this.getSectionNameStringTable().getString(OatFile.this.readSmallUint(this.offset));
      }

      public int getType() {
         return OatFile.this.readInt(this.offset + 4);
      }

      public abstract long getAddress();

      public abstract int getOffset();

      public abstract int getSize();

      public abstract int getLink();

      public abstract int getEntrySize();
   }

   private class OatHeader {
      private final int headerOffset;

      public OatHeader(int offset) {
         this.headerOffset = offset;
      }

      public boolean isValid() {
         int i;
         for(i = 0; i < OatFile.OAT_MAGIC.length; ++i) {
            if (OatFile.this.buf[this.headerOffset + i] != OatFile.OAT_MAGIC[i]) {
               return false;
            }
         }

         for(i = 4; i < 7; ++i) {
            if (OatFile.this.buf[this.headerOffset + i] < 48 || OatFile.this.buf[this.headerOffset + i] > 57) {
               return false;
            }
         }

         return OatFile.this.buf[this.headerOffset + 7] == 0;
      }

      public int getVersion() {
         return Integer.valueOf(new String(OatFile.this.buf, this.headerOffset + 4, 3));
      }

      public int getDexFileCount() {
         return OatFile.this.readSmallUint(this.headerOffset + 20);
      }

      public int getKeyValueStoreSize() {
         if (this.getVersion() < 56) {
            throw new IllegalStateException("Unsupported oat version");
         } else {
            int fieldOffset = 68;
            return OatFile.this.readSmallUint(this.headerOffset + fieldOffset);
         }
      }

      public int getHeaderSize() {
         if (this.getVersion() < 56) {
            throw new IllegalStateException("Unsupported oat version");
         } else {
            return 72 + this.getKeyValueStoreSize();
         }
      }

      @Nullable
      public String getKeyValue(@Nonnull String key) {
         int size = this.getKeyValueStoreSize();
         int offset = this.headerOffset + 72;

         for(int endOffset = offset + size; offset < endOffset; ++offset) {
            int keyStartOffset;
            for(keyStartOffset = offset; offset < endOffset && OatFile.this.buf[offset] != 0; ++offset) {
            }

            if (offset >= endOffset) {
               throw new OatFile.InvalidOatFileException("Oat file contains truncated key value store");
            }

            String k = new String(OatFile.this.buf, keyStartOffset, offset - keyStartOffset);
            if (k.equals(key)) {
               ++offset;

               int valueStartOffset;
               for(valueStartOffset = offset; offset < endOffset && OatFile.this.buf[offset] != 0; ++offset) {
               }

               if (offset >= endOffset) {
                  throw new OatFile.InvalidOatFileException("Oat file contains truncated key value store");
               }

               return new String(OatFile.this.buf, valueStartOffset, offset - valueStartOffset);
            }
         }

         return null;
      }

      public int getDexListStart() {
         return this.headerOffset + this.getHeaderSize();
      }
   }

   public class OatDexFile extends DexBackedDexFile implements MultiDexContainer.MultiDexFile {
      @Nonnull
      public final String filename;

      public OatDexFile(byte[] buf, int offset, @Nonnull String filename) {
         super(OatFile.this.opcodes, buf, offset);
         this.filename = filename;
      }

      @Nonnull
      public String getEntryName() {
         return this.filename;
      }

      @Nonnull
      public OatFile getContainer() {
         return OatFile.this;
      }

      public boolean hasOdexOpcodes() {
         return true;
      }
   }
}

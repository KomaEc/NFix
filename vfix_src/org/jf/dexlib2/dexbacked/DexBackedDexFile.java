package org.jf.dexlib2.dexbacked;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.reference.DexBackedFieldReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedStringReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedTypeReference;
import org.jf.dexlib2.dexbacked.util.FixedSizeSet;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.DexUtil;
import org.jf.util.ExceptionWithContext;

public class DexBackedDexFile extends BaseDexBuffer implements DexFile {
   @Nonnull
   private final Opcodes opcodes;
   private final int stringCount;
   private final int stringStartOffset;
   private final int typeCount;
   private final int typeStartOffset;
   private final int protoCount;
   private final int protoStartOffset;
   private final int fieldCount;
   private final int fieldStartOffset;
   private final int methodCount;
   private final int methodStartOffset;
   private final int classCount;
   private final int classStartOffset;

   protected DexBackedDexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] buf, int offset, boolean verifyMagic) {
      super(buf, offset);
      this.opcodes = opcodes;
      if (verifyMagic) {
         DexUtil.verifyDexHeader(buf, offset);
      }

      this.stringCount = this.readSmallUint(56);
      this.stringStartOffset = this.readSmallUint(60);
      this.typeCount = this.readSmallUint(64);
      this.typeStartOffset = this.readSmallUint(68);
      this.protoCount = this.readSmallUint(72);
      this.protoStartOffset = this.readSmallUint(76);
      this.fieldCount = this.readSmallUint(80);
      this.fieldStartOffset = this.readSmallUint(84);
      this.methodCount = this.readSmallUint(88);
      this.methodStartOffset = this.readSmallUint(92);
      this.classCount = this.readSmallUint(96);
      this.classStartOffset = this.readSmallUint(100);
   }

   public DexBackedDexFile(@Nonnull Opcodes opcodes, @Nonnull BaseDexBuffer buf) {
      this(opcodes, buf.buf, buf.baseOffset);
   }

   public DexBackedDexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] buf, int offset) {
      this(opcodes, buf, offset, false);
   }

   public DexBackedDexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] buf) {
      this(opcodes, buf, 0, true);
   }

   @Nonnull
   public static DexBackedDexFile fromInputStream(@Nonnull Opcodes opcodes, @Nonnull InputStream is) throws IOException {
      DexUtil.verifyDexHeader(is);
      byte[] buf = ByteStreams.toByteArray(is);
      return new DexBackedDexFile(opcodes, buf, 0, false);
   }

   @Nonnull
   public Opcodes getOpcodes() {
      return this.opcodes;
   }

   public boolean isOdexFile() {
      return false;
   }

   public boolean hasOdexOpcodes() {
      return false;
   }

   @Nonnull
   public Set<? extends DexBackedClassDef> getClasses() {
      return new FixedSizeSet<DexBackedClassDef>() {
         @Nonnull
         public DexBackedClassDef readItem(int index) {
            return new DexBackedClassDef(DexBackedDexFile.this, DexBackedDexFile.this.getClassDefItemOffset(index));
         }

         public int size() {
            return DexBackedDexFile.this.classCount;
         }
      };
   }

   public int getStringIdItemOffset(int stringIndex) {
      if (stringIndex >= 0 && stringIndex < this.stringCount) {
         return this.stringStartOffset + stringIndex * 4;
      } else {
         throw new DexBackedDexFile.InvalidItemIndex(stringIndex, "String index out of bounds: %d", new Object[]{stringIndex});
      }
   }

   public int getTypeIdItemOffset(int typeIndex) {
      if (typeIndex >= 0 && typeIndex < this.typeCount) {
         return this.typeStartOffset + typeIndex * 4;
      } else {
         throw new DexBackedDexFile.InvalidItemIndex(typeIndex, "Type index out of bounds: %d", new Object[]{typeIndex});
      }
   }

   public int getFieldIdItemOffset(int fieldIndex) {
      if (fieldIndex >= 0 && fieldIndex < this.fieldCount) {
         return this.fieldStartOffset + fieldIndex * 8;
      } else {
         throw new DexBackedDexFile.InvalidItemIndex(fieldIndex, "Field index out of bounds: %d", new Object[]{fieldIndex});
      }
   }

   public int getMethodIdItemOffset(int methodIndex) {
      if (methodIndex >= 0 && methodIndex < this.methodCount) {
         return this.methodStartOffset + methodIndex * 8;
      } else {
         throw new DexBackedDexFile.InvalidItemIndex(methodIndex, "Method index out of bounds: %d", new Object[]{methodIndex});
      }
   }

   public int getProtoIdItemOffset(int protoIndex) {
      if (protoIndex >= 0 && protoIndex < this.protoCount) {
         return this.protoStartOffset + protoIndex * 12;
      } else {
         throw new DexBackedDexFile.InvalidItemIndex(protoIndex, "Proto index out of bounds: %d", new Object[]{protoIndex});
      }
   }

   public int getClassDefItemOffset(int classIndex) {
      if (classIndex >= 0 && classIndex < this.classCount) {
         return this.classStartOffset + classIndex * 32;
      } else {
         throw new DexBackedDexFile.InvalidItemIndex(classIndex, "Class index out of bounds: %d", new Object[]{classIndex});
      }
   }

   public int getClassCount() {
      return this.classCount;
   }

   public int getStringCount() {
      return this.stringCount;
   }

   public int getTypeCount() {
      return this.typeCount;
   }

   public int getProtoCount() {
      return this.protoCount;
   }

   public int getFieldCount() {
      return this.fieldCount;
   }

   public int getMethodCount() {
      return this.methodCount;
   }

   @Nonnull
   public String getString(int stringIndex) {
      int stringOffset = this.getStringIdItemOffset(stringIndex);
      int stringDataOffset = this.readSmallUint(stringOffset);
      DexReader reader = this.readerAt(stringDataOffset);
      int utf16Length = reader.readSmallUleb128();
      return reader.readString(utf16Length);
   }

   @Nullable
   public String getOptionalString(int stringIndex) {
      return stringIndex == -1 ? null : this.getString(stringIndex);
   }

   @Nonnull
   public String getType(int typeIndex) {
      int typeOffset = this.getTypeIdItemOffset(typeIndex);
      int stringIndex = this.readSmallUint(typeOffset);
      return this.getString(stringIndex);
   }

   @Nullable
   public String getOptionalType(int typeIndex) {
      return typeIndex == -1 ? null : this.getType(typeIndex);
   }

   public List<DexBackedStringReference> getStrings() {
      return new AbstractList<DexBackedStringReference>() {
         public DexBackedStringReference get(int index) {
            if (index >= 0 && index < DexBackedDexFile.this.getStringCount()) {
               return new DexBackedStringReference(DexBackedDexFile.this, index);
            } else {
               throw new IndexOutOfBoundsException();
            }
         }

         public int size() {
            return DexBackedDexFile.this.getStringCount();
         }
      };
   }

   public List<DexBackedTypeReference> getTypes() {
      return new AbstractList<DexBackedTypeReference>() {
         public DexBackedTypeReference get(int index) {
            if (index >= 0 && index < DexBackedDexFile.this.getTypeCount()) {
               return new DexBackedTypeReference(DexBackedDexFile.this, index);
            } else {
               throw new IndexOutOfBoundsException();
            }
         }

         public int size() {
            return DexBackedDexFile.this.getTypeCount();
         }
      };
   }

   public List<DexBackedMethodReference> getMethods() {
      return new AbstractList<DexBackedMethodReference>() {
         public DexBackedMethodReference get(int index) {
            if (index >= 0 && index < DexBackedDexFile.this.getMethodCount()) {
               return new DexBackedMethodReference(DexBackedDexFile.this, index);
            } else {
               throw new IndexOutOfBoundsException();
            }
         }

         public int size() {
            return DexBackedDexFile.this.getMethodCount();
         }
      };
   }

   public List<DexBackedFieldReference> getFields() {
      return new AbstractList<DexBackedFieldReference>() {
         public DexBackedFieldReference get(int index) {
            if (index >= 0 && index < DexBackedDexFile.this.getFieldCount()) {
               return new DexBackedFieldReference(DexBackedDexFile.this, index);
            } else {
               throw new IndexOutOfBoundsException();
            }
         }

         public int size() {
            return DexBackedDexFile.this.getFieldCount();
         }
      };
   }

   public List<? extends Reference> getReferences(int referenceType) {
      switch(referenceType) {
      case 0:
         return this.getStrings();
      case 1:
         return this.getTypes();
      case 2:
         return this.getFields();
      case 3:
         return this.getMethods();
      default:
         throw new IllegalArgumentException(String.format("Invalid reference type: %d", referenceType));
      }
   }

   @Nonnull
   public DexReader readerAt(int offset) {
      return new DexReader(this, offset);
   }

   public static class InvalidItemIndex extends ExceptionWithContext {
      private final int itemIndex;

      public InvalidItemIndex(int itemIndex) {
         super("");
         this.itemIndex = itemIndex;
      }

      public InvalidItemIndex(int itemIndex, String message, Object... formatArgs) {
         super(message, formatArgs);
         this.itemIndex = itemIndex;
      }

      public int getInvalidIndex() {
         return this.itemIndex;
      }
   }

   public static class NotADexFile extends RuntimeException {
      public NotADexFile() {
      }

      public NotADexFile(Throwable cause) {
         super(cause);
      }

      public NotADexFile(String message) {
         super(message);
      }

      public NotADexFile(String message, Throwable cause) {
         super(message, cause);
      }
   }
}

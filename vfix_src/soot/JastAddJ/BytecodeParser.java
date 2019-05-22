package soot.JastAddJ;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BytecodeParser implements Flags, BytecodeReader {
   public static final boolean VERBOSE = false;
   private DataInputStream is;
   public CONSTANT_Class_Info classInfo;
   public String outerClassName;
   public String name;
   public boolean isInnerClass;
   public CONSTANT_Info[] constantPool;
   private static final int CONSTANT_Class = 7;
   private static final int CONSTANT_FieldRef = 9;
   private static final int CONSTANT_MethodRef = 10;
   private static final int CONSTANT_InterfaceMethodRef = 11;
   private static final int CONSTANT_String = 8;
   private static final int CONSTANT_Integer = 3;
   private static final int CONSTANT_Float = 4;
   private static final int CONSTANT_Long = 5;
   private static final int CONSTANT_Double = 6;
   private static final int CONSTANT_NameAndType = 12;
   private static final int CONSTANT_Utf8 = 1;

   public CompilationUnit read(InputStream is, String fullName, Program p) throws FileNotFoundException, IOException {
      return (new BytecodeParser(is, fullName)).parse((TypeDecl)null, (String)null, p);
   }

   public BytecodeParser(byte[] buffer, int size, String name) {
      this.isInnerClass = false;
      this.constantPool = null;
      this.is = new DataInputStream(new ByteArrayInputStream(buffer, 0, size));
      this.name = name;
   }

   public BytecodeParser(InputStream in, String name) {
      this.isInnerClass = false;
      this.constantPool = null;
      this.is = new DataInputStream(new BytecodeParser.DummyInputStream(in));
      this.name = name;
   }

   public BytecodeParser() {
      this("");
   }

   public BytecodeParser(String name) {
      this.isInnerClass = false;
      this.constantPool = null;
      if (!name.endsWith(".class")) {
         name = name.replace('.', '/') + ".class";
      }

      this.name = name;
   }

   public int next() {
      try {
         return this.is.read();
      } catch (IOException var2) {
         System.exit(1);
         return -1;
      }
   }

   public int u1() {
      try {
         return this.is.readUnsignedByte();
      } catch (IOException var2) {
         System.exit(1);
         return -1;
      }
   }

   public int u2() {
      try {
         return this.is.readUnsignedShort();
      } catch (IOException var2) {
         System.exit(1);
         return -1;
      }
   }

   public int u4() {
      try {
         return this.is.readInt();
      } catch (IOException var2) {
         System.exit(1);
         return -1;
      }
   }

   public int readInt() {
      try {
         return this.is.readInt();
      } catch (IOException var2) {
         System.exit(1);
         return -1;
      }
   }

   public float readFloat() {
      try {
         return this.is.readFloat();
      } catch (IOException var2) {
         System.exit(1);
         return -1.0F;
      }
   }

   public long readLong() {
      try {
         return this.is.readLong();
      } catch (IOException var2) {
         System.exit(1);
         return -1L;
      }
   }

   public double readDouble() {
      try {
         return this.is.readDouble();
      } catch (IOException var2) {
         System.exit(1);
         return -1.0D;
      }
   }

   public String readUTF() {
      try {
         return this.is.readUTF();
      } catch (IOException var2) {
         System.exit(1);
         return "";
      }
   }

   public void skip(int length) {
      try {
         this.is.skip((long)length);
      } catch (IOException var3) {
         System.exit(1);
      }

   }

   public void error(String s) {
      throw new RuntimeException(s);
   }

   public void print(String s) {
   }

   public void println(String s) {
      this.print(s + "\n");
   }

   public void println() {
      this.print("\n");
   }

   public CompilationUnit parse(TypeDecl outerTypeDecl, String outerClassName, Program classPath, boolean isInner) throws FileNotFoundException, IOException {
      this.isInnerClass = isInner;
      return this.parse(outerTypeDecl, outerClassName, classPath);
   }

   public CompilationUnit parse(TypeDecl outerTypeDecl, String outerClassName, Program program) throws FileNotFoundException, IOException {
      if (this.is == null) {
         FileInputStream file = new FileInputStream(this.name);
         if (file == null) {
            throw new FileNotFoundException(this.name);
         }

         this.is = new DataInputStream(new BufferedInputStream(file));
      }

      this.outerClassName = outerClassName;
      this.parseMagic();
      this.parseMinor();
      this.parseMajor();
      this.parseConstantPool();
      CompilationUnit cu = new CompilationUnit();
      TypeDecl typeDecl = this.parseTypeDecl();
      cu.setPackageDecl(this.classInfo.packageDecl());
      cu.addTypeDecl(typeDecl);
      this.parseFields(typeDecl);
      this.parseMethods(typeDecl);
      if ((new Attributes.TypeAttributes(this, typeDecl, outerTypeDecl, program)).isInnerClass()) {
         program.addCompilationUnit(cu);

         for(int i = 0; i < cu.getTypeDecls().getNumChild(); ++i) {
            cu.getTypeDecls().removeChild(i);
         }

         program.getCompilationUnits().removeChild(program.getCompilationUnits().getIndexOfChild(cu));
      }

      this.is.close();
      this.is = null;
      return cu;
   }

   public void parseMagic() {
      if (this.next() != 202 || this.next() != 254 || this.next() != 186 || this.next() != 190) {
         this.error("magic error");
      }

   }

   public void parseMinor() {
      int low = this.u1();
      int high = this.u1();
   }

   public void parseMajor() {
      int low = this.u1();
      int high = this.u1();
   }

   public TypeDecl parseTypeDecl() {
      int flags = this.u2();
      Modifiers modifiers = modifiers(flags & '\ufddf');
      Access superClass;
      if ((flags & 16896) == 16384) {
         EnumDecl decl = new EnumDecl();
         decl.setModifiers(modifiers);
         decl.setID(this.parseThisClass());
         superClass = this.parseSuperClass();
         decl.setImplementsList(this.parseInterfaces(new List()));
         return decl;
      } else if ((flags & 512) == 0) {
         ClassDecl decl = new ClassDecl();
         decl.setModifiers(modifiers);
         decl.setID(this.parseThisClass());
         superClass = this.parseSuperClass();
         decl.setSuperClassAccessOpt(superClass == null ? new Opt() : new Opt(superClass));
         decl.setImplementsList(this.parseInterfaces(new List()));
         return decl;
      } else if ((flags & 8192) == 0) {
         InterfaceDecl decl = new InterfaceDecl();
         decl.setModifiers(modifiers);
         decl.setID(this.parseThisClass());
         superClass = this.parseSuperClass();
         decl.setSuperInterfaceIdList(this.parseInterfaces(superClass == null ? new List() : (new List()).add(superClass)));
         return decl;
      } else {
         AnnotationDecl decl = new AnnotationDecl();
         decl.setModifiers(modifiers);
         decl.setID(this.parseThisClass());
         superClass = this.parseSuperClass();
         this.parseInterfaces(superClass == null ? new List() : (new List()).add(superClass));
         return decl;
      }
   }

   public String parseThisClass() {
      int index = this.u2();
      CONSTANT_Class_Info info = (CONSTANT_Class_Info)this.constantPool[index];
      this.classInfo = info;
      return info.simpleName();
   }

   public Access parseSuperClass() {
      int index = this.u2();
      if (index == 0) {
         return null;
      } else {
         CONSTANT_Class_Info info = (CONSTANT_Class_Info)this.constantPool[index];
         return info.access();
      }
   }

   public List parseInterfaces(List list) {
      int count = this.u2();

      for(int i = 0; i < count; ++i) {
         CONSTANT_Class_Info info = (CONSTANT_Class_Info)this.constantPool[this.u2()];
         list.add(info.access());
      }

      return list;
   }

   public Access fromClassName(String s) {
      String packageName = "";
      int index = s.lastIndexOf(47);
      if (index != -1) {
         packageName = s.substring(0, index).replace('/', '.');
      }

      String typeName = s.substring(index + 1, s.length());
      return (Access)(typeName.indexOf(36) != -1 ? new BytecodeTypeAccess(packageName, typeName) : new TypeAccess(packageName, typeName));
   }

   public static Modifiers modifiers(int flags) {
      Modifiers m = new Modifiers();
      if ((flags & 1) != 0) {
         m.addModifier(new Modifier("public"));
      }

      if ((flags & 2) != 0) {
         m.addModifier(new Modifier("private"));
      }

      if ((flags & 4) != 0) {
         m.addModifier(new Modifier("protected"));
      }

      if ((flags & 8) != 0) {
         m.addModifier(new Modifier("static"));
      }

      if ((flags & 16) != 0) {
         m.addModifier(new Modifier("final"));
      }

      if ((flags & 32) != 0) {
         m.addModifier(new Modifier("synchronized"));
      }

      if ((flags & 64) != 0) {
         m.addModifier(new Modifier("volatile"));
      }

      if ((flags & 128) != 0) {
         m.addModifier(new Modifier("transient"));
      }

      if ((flags & 256) != 0) {
         m.addModifier(new Modifier("native"));
      }

      if ((flags & 1024) != 0) {
         m.addModifier(new Modifier("abstract"));
      }

      if ((flags & 2048) != 0) {
         m.addModifier(new Modifier("strictfp"));
      }

      return m;
   }

   public void parseFields(TypeDecl typeDecl) {
      int count = this.u2();

      for(int i = 0; i < count; ++i) {
         FieldInfo fieldInfo = new FieldInfo(this);
         if (!fieldInfo.isSynthetic()) {
            typeDecl.addBodyDecl(fieldInfo.bodyDecl());
         }
      }

   }

   public void parseMethods(TypeDecl typeDecl) {
      int count = this.u2();

      for(int i = 0; i < count; ++i) {
         MethodInfo info = new MethodInfo(this);
         if (!info.isSynthetic() && !info.name.equals("<clinit>")) {
            typeDecl.addBodyDecl(info.bodyDecl());
         }
      }

   }

   private void checkLengthAndNull(int index) {
      if (index >= this.constantPool.length) {
         throw new Error("Trying to access element " + index + " in constant pool of length " + this.constantPool.length);
      } else if (this.constantPool[index] == null) {
         throw new Error("Unexpected null element in constant pool at index " + index);
      }
   }

   public boolean validConstantPoolIndex(int index) {
      return index < this.constantPool.length && this.constantPool[index] != null;
   }

   public CONSTANT_Info getCONSTANT_Info(int index) {
      this.checkLengthAndNull(index);
      return this.constantPool[index];
   }

   public CONSTANT_Utf8_Info getCONSTANT_Utf8_Info(int index) {
      this.checkLengthAndNull(index);
      CONSTANT_Info info = this.constantPool[index];
      if (!(info instanceof CONSTANT_Utf8_Info)) {
         throw new Error("Expected CONSTANT_Utf8_info at " + index + " in constant pool but found " + info.getClass().getName());
      } else {
         return (CONSTANT_Utf8_Info)info;
      }
   }

   public CONSTANT_Class_Info getCONSTANT_Class_Info(int index) {
      this.checkLengthAndNull(index);
      CONSTANT_Info info = this.constantPool[index];
      if (!(info instanceof CONSTANT_Class_Info)) {
         throw new Error("Expected CONSTANT_Class_info at " + index + " in constant pool but found " + info.getClass().getName());
      } else {
         return (CONSTANT_Class_Info)info;
      }
   }

   public void parseConstantPool() {
      int count = this.u2();
      this.constantPool = new CONSTANT_Info[count + 1];

      for(int i = 1; i < count; ++i) {
         this.parseEntry(i);
         if (this.constantPool[i] instanceof CONSTANT_Long_Info || this.constantPool[i] instanceof CONSTANT_Double_Info) {
            ++i;
         }
      }

   }

   public void parseEntry(int i) {
      int tag = this.u1();
      switch(tag) {
      case 1:
         this.constantPool[i] = new CONSTANT_Utf8_Info(this);
         break;
      case 2:
      default:
         this.println("Unknown entry: " + tag);
         break;
      case 3:
         this.constantPool[i] = new CONSTANT_Integer_Info(this);
         break;
      case 4:
         this.constantPool[i] = new CONSTANT_Float_Info(this);
         break;
      case 5:
         this.constantPool[i] = new CONSTANT_Long_Info(this);
         break;
      case 6:
         this.constantPool[i] = new CONSTANT_Double_Info(this);
         break;
      case 7:
         this.constantPool[i] = new CONSTANT_Class_Info(this);
         break;
      case 8:
         this.constantPool[i] = new CONSTANT_String_Info(this);
         break;
      case 9:
         this.constantPool[i] = new CONSTANT_Fieldref_Info(this);
         break;
      case 10:
         this.constantPool[i] = new CONSTANT_Methodref_Info(this);
         break;
      case 11:
         this.constantPool[i] = new CONSTANT_InterfaceMethodref_Info(this);
         break;
      case 12:
         this.constantPool[i] = new CONSTANT_NameAndType_Info(this);
      }

   }

   private static class DummyInputStream extends InputStream {
      byte[] bytes;
      int pos;
      int size;

      public DummyInputStream(byte[] buffer, int size) {
         this.bytes = buffer;
         this.size = size;
      }

      public DummyInputStream(InputStream is) {
         this.bytes = new byte[1024];
         int index = 0;
         this.size = 1024;

         int status;
         try {
            do {
               status = is.read(this.bytes, index, this.size - index);
               if (status != -1) {
                  index += status;
                  if (index == this.size) {
                     byte[] newBytes = new byte[this.size * 2];
                     System.arraycopy(this.bytes, 0, newBytes, 0, this.size);
                     this.bytes = newBytes;
                     this.size *= 2;
                  }
               }
            } while(status != -1);
         } catch (IOException var5) {
            System.err.println("Something went wrong trying to read " + is);
         }

         this.size = index;
         this.pos = 0;
      }

      public int available() {
         return this.size - this.pos;
      }

      public void close() {
      }

      public void mark(int readlimit) {
      }

      public boolean markSupported() {
         return false;
      }

      public int read(byte[] b) {
         int actualLength = Math.min(b.length, this.size - this.pos);
         System.arraycopy(this.bytes, this.pos, b, 0, actualLength);
         this.pos += actualLength;
         return actualLength;
      }

      public int read(byte[] b, int offset, int length) {
         int actualLength = Math.min(length, this.size - this.pos);
         System.arraycopy(this.bytes, this.pos, b, offset, actualLength);
         this.pos += actualLength;
         return actualLength;
      }

      public void reset() {
      }

      public long skip(long n) {
         if (this.size == this.pos) {
            return -1L;
         } else {
            long skipSize = Math.min(n, (long)(this.size - this.pos));
            this.pos = (int)((long)this.pos + skipSize);
            return skipSize;
         }
      }

      public int read() throws IOException {
         if (this.pos < this.size) {
            int i = this.bytes[this.pos++];
            if (i < 0) {
               i += 256;
            }

            return i;
         } else {
            return -1;
         }
      }
   }
}

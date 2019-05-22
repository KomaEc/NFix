package polyglot.types.reflect;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.types.CachingResolver;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.LazyClassInitializer;
import polyglot.types.MethodInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;

public class ClassFile implements LazyClassInitializer {
   protected Constant[] constants;
   int modifiers;
   int thisClass;
   int superClass;
   int[] interfaces;
   protected Field[] fields;
   protected Method[] methods;
   protected Attribute[] attrs;
   protected InnerClasses innerClasses;
   File classFileSource;
   private ExtensionInfo extensionInfo;
   static Collection verbose;
   Map jlcInfo = new HashMap();

   public ClassFile(File classFileSource, byte[] code, ExtensionInfo ext) {
      this.classFileSource = classFileSource;
      this.extensionInfo = ext;

      try {
         ByteArrayInputStream bin = new ByteArrayInputStream(code);
         DataInputStream in = new DataInputStream(bin);
         this.read(in);
         in.close();
         bin.close();
      } catch (IOException var6) {
         throw new InternalCompilerError("I/O exception on ByteArrayInputStream");
      }
   }

   public Method createMethod(DataInputStream in) throws IOException {
      Method m = new Method(in, this);
      m.initialize();
      return m;
   }

   public Field createField(DataInputStream in) throws IOException {
      Field f = new Field(in, this);
      f.initialize();
      return f;
   }

   public Attribute createAttribute(DataInputStream in, String name, int nameIndex, int length) throws IOException {
      if (name.equals("InnerClasses")) {
         this.innerClasses = new InnerClasses(in, nameIndex, length);
         return this.innerClasses;
      } else {
         return null;
      }
   }

   public Constant[] constants() {
      return this.constants;
   }

   public boolean fromClassFile() {
      return true;
   }

   JLCInfo getJLCInfo(String ts) {
      JLCInfo jlc = (JLCInfo)this.jlcInfo.get(ts);
      if (jlc != null) {
         return jlc;
      } else {
         jlc = new JLCInfo();
         this.jlcInfo.put(ts, jlc);

         try {
            int mask = 0;

            for(int i = 0; i < this.fields.length; ++i) {
               if (this.fields[i].name().equals("jlc$SourceLastModified$" + ts)) {
                  jlc.sourceLastModified = this.fields[i].getLong();
                  mask |= 1;
               } else if (this.fields[i].name().equals("jlc$CompilerVersion$" + ts)) {
                  jlc.compilerVersion = this.fields[i].getString();
                  mask |= 2;
               } else if (this.fields[i].name().equals("jlc$ClassType$" + ts)) {
                  jlc.encodedClassType = this.fields[i].getString();
                  mask |= 4;
               }
            }

            if (mask != 7) {
               jlc.sourceLastModified = 0L;
               jlc.compilerVersion = null;
               jlc.encodedClassType = null;
            }
         } catch (SemanticException var5) {
            jlc.sourceLastModified = 0L;
            jlc.compilerVersion = null;
            jlc.encodedClassType = null;
         }

         return jlc;
      }
   }

   public long sourceLastModified(String ts) {
      JLCInfo jlc = this.getJLCInfo(ts);
      return jlc.sourceLastModified;
   }

   public long rawSourceLastModified() {
      return this.classFileSource.lastModified();
   }

   public String compilerVersion(String ts) {
      JLCInfo jlc = this.getJLCInfo(ts);
      return jlc.compilerVersion;
   }

   public String encodedClassType(String ts) {
      JLCInfo jlc = this.getJLCInfo(ts);
      return jlc.encodedClassType;
   }

   void read(DataInputStream in) throws IOException {
      this.readHeader(in);
      this.readConstantPool(in);
      this.readAccessFlags(in);
      this.readClassInfo(in);
      this.readFields(in);
      this.readMethods(in);
      this.readAttributes(in);
   }

   public ParsedClassType type(TypeSystem ts) throws SemanticException {
      ParsedClassType ct = this.createType(ts);
      if (ts.equals(ct, ts.Object())) {
         ct.superType((Type)null);
      } else {
         String superName = this.classNameCP(this.superClass);
         if (superName != null) {
            ct.superType(this.typeForName(ts, superName));
         } else {
            ct.superType(ts.Object());
         }
      }

      return ct;
   }

   public void initMemberClasses(ParsedClassType ct) {
      if (this.innerClasses != null) {
         TypeSystem ts = ct.typeSystem();

         for(int i = 0; i < this.innerClasses.classes.length; ++i) {
            InnerClasses.Info c = this.innerClasses.classes[i];
            if (c.outerClassIndex == this.thisClass && c.classIndex != 0) {
               String name = this.classNameCP(c.classIndex);
               int index = name.lastIndexOf(36);
               if (index < 0 || !Character.isDigit(name.charAt(index + 1))) {
                  ClassType t = this.quietTypeForName(ts, name);
                  if (!t.isMember()) {
                     throw new InternalCompilerError(name + " should be a member class.");
                  }

                  if (Report.should_report((Collection)verbose, 3)) {
                     Report.report(3, "adding member " + t + " to " + ct);
                  }

                  ct.addMemberClass(t);
                  if (t instanceof ParsedClassType) {
                     ParsedClassType pt = (ParsedClassType)t;
                     pt.flags(ts.flagsForBits(c.modifiers));
                  }
               }
            }
         }

      }
   }

   public void initInterfaces(ParsedClassType ct) {
      TypeSystem ts = ct.typeSystem();

      for(int i = 0; i < this.interfaces.length; ++i) {
         String name = this.classNameCP(this.interfaces[i]);
         ct.addInterface(this.quietTypeForName(ts, name));
      }

   }

   public void initFields(ParsedClassType ct) {
      TypeSystem ts = ct.typeSystem();
      LazyClassInitializer init = ts.defaultClassInitializer();
      init.initFields(ct);

      for(int i = 0; i < this.fields.length; ++i) {
         if (!this.fields[i].name().startsWith("jlc$") && !this.fields[i].isSynthetic()) {
            FieldInstance fi = this.fields[i].fieldInstance(ts, ct);
            if (Report.should_report((Collection)verbose, 3)) {
               Report.report(3, "adding " + fi + " to " + ct);
            }

            ct.addField(fi);
         }
      }

   }

   public void initMethods(ParsedClassType ct) {
      TypeSystem ts = ct.typeSystem();

      for(int i = 0; i < this.methods.length; ++i) {
         if (!this.methods[i].name().equals("<init>") && !this.methods[i].name().equals("<clinit>") && !this.methods[i].isSynthetic()) {
            MethodInstance mi = this.methods[i].methodInstance(ts, ct);
            if (Report.should_report((Collection)verbose, 3)) {
               Report.report(3, "adding " + mi + " to " + ct);
            }

            ct.addMethod(mi);
         }
      }

   }

   public void initConstructors(ParsedClassType ct) {
      TypeSystem ts = ct.typeSystem();

      for(int i = 0; i < this.methods.length; ++i) {
         if (this.methods[i].name().equals("<init>") && !this.methods[i].isSynthetic()) {
            ConstructorInstance ci = this.methods[i].constructorInstance(ts, ct, this.fields);
            if (Report.should_report((Collection)verbose, 3)) {
               Report.report(3, "adding " + ci + " to " + ct);
            }

            ct.addConstructor(ci);
         }
      }

   }

   Type arrayOf(Type t, int dims) {
      return (Type)(dims == 0 ? t : t.typeSystem().arrayOf(t, dims));
   }

   List typeListForString(TypeSystem ts, String str) {
      List types = new ArrayList();

      label49:
      for(int i = 0; i < str.length(); ++i) {
         int dims;
         for(dims = 0; str.charAt(i) == '['; ++i) {
            ++dims;
         }

         switch(str.charAt(i)) {
         case 'B':
            types.add(this.arrayOf(ts.Byte(), dims));
            break;
         case 'C':
            types.add(this.arrayOf(ts.Char(), dims));
            break;
         case 'D':
            types.add(this.arrayOf(ts.Double(), dims));
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'W':
         case 'X':
         case 'Y':
         default:
            break;
         case 'F':
            types.add(this.arrayOf(ts.Float(), dims));
            break;
         case 'I':
            types.add(this.arrayOf(ts.Int(), dims));
            break;
         case 'J':
            types.add(this.arrayOf(ts.Long(), dims));
            break;
         case 'L':
            ++i;
            int start = i;

            while(true) {
               if (i >= str.length()) {
                  continue label49;
               }

               if (str.charAt(i) == ';') {
                  String s = str.substring(start, i);
                  s = s.replace('/', '.');
                  types.add(this.arrayOf(this.quietTypeForName(ts, s), dims));
                  continue label49;
               }

               ++i;
            }
         case 'S':
            types.add(this.arrayOf(ts.Short(), dims));
            break;
         case 'V':
            types.add(this.arrayOf(ts.Void(), dims));
            break;
         case 'Z':
            types.add(this.arrayOf(ts.Boolean(), dims));
         }
      }

      if (Report.should_report((Collection)verbose, 4)) {
         Report.report(4, "parsed \"" + str + "\" -> " + types);
      }

      return types;
   }

   Type typeForString(TypeSystem ts, String str) {
      List l = this.typeListForString(ts, str);
      if (l.size() == 1) {
         return (Type)l.get(0);
      } else {
         throw new InternalCompilerError("Bad type string: \"" + str + "\"");
      }
   }

   ClassType quietTypeForName(TypeSystem ts, String name) {
      if (Report.should_report((Collection)verbose, 2)) {
         Report.report(2, "resolving " + name);
      }

      try {
         return (ClassType)ts.systemResolver().find(name);
      } catch (SemanticException var4) {
         throw new InternalCompilerError("could not load " + name);
      }
   }

   public ClassType typeForName(TypeSystem ts, String name) throws SemanticException {
      if (Report.should_report((Collection)verbose, 2)) {
         Report.report(2, "resolving " + name);
      }

      return (ClassType)ts.systemResolver().find(name);
   }

   ParsedClassType createType(TypeSystem ts) throws SemanticException {
      String name = this.classNameCP(this.thisClass);
      if (Report.should_report((Collection)verbose, 2)) {
         Report.report(2, "creating ClassType for " + name);
      }

      ParsedClassType ct = ts.createClassType((LazyClassInitializer)this);
      ct.flags(ts.flagsForBits(this.modifiers));
      ct.position(this.position());
      ((CachingResolver)ts.systemResolver()).install(name, ct);
      String packageName = StringUtil.getPackageComponent(name);
      if (!packageName.equals("")) {
         ct.package_(ts.packageForName(packageName));
      }

      String className = StringUtil.getShortNameComponent(name);
      String outerName = name;
      String innerName = null;

      while(true) {
         int dollar = outerName.lastIndexOf(36);
         if (dollar < 0) {
            innerName = null;
            break;
         }

         outerName = name.substring(0, dollar);
         innerName = name.substring(dollar + 1);

         try {
            if (Report.should_report((Collection)verbose, 2)) {
               Report.report(2, "resolving " + outerName + " for " + name);
            }

            ct.outer(this.typeForName(ts, outerName));
            break;
         } catch (SemanticException var11) {
            if (Report.should_report((Collection)verbose, 3)) {
               Report.report(2, "error resolving " + outerName);
            }
         }
      }

      ClassType.Kind kind = ClassType.TOP_LEVEL;
      if (innerName != null) {
         StringTokenizer st = new StringTokenizer(className, "$");

         while(st.hasMoreTokens()) {
            String s = st.nextToken();
            if (Character.isDigit(s.charAt(0))) {
               kind = ClassType.ANONYMOUS;
            } else if (kind == ClassType.ANONYMOUS) {
               kind = ClassType.LOCAL;
            } else {
               kind = ClassType.MEMBER;
            }
         }
      }

      if (Report.should_report((Collection)verbose, 3)) {
         Report.report(3, name + " is " + kind);
      }

      ct.kind(kind);
      if (ct.isTopLevel()) {
         ct.name(className);
      } else if (ct.isMember() || ct.isLocal()) {
         ct.name(innerName);
      }

      return ct;
   }

   public Position position() {
      return new Position(this.name() + ".class");
   }

   String classNameCP(int index) {
      Constant c = this.constants[index];
      if (c != null && c.tag() == 7) {
         Integer nameIndex = (Integer)c.value();
         if (nameIndex != null) {
            c = this.constants[nameIndex];
            if (c.tag() == 1) {
               String s = (String)c.value();
               return s.replace('/', '.');
            }
         }
      }

      return null;
   }

   public String name() {
      Constant c = this.constants[this.thisClass];
      if (c.tag() == 7) {
         Integer nameIndex = (Integer)c.value();
         if (nameIndex != null) {
            c = this.constants[nameIndex];
            if (c.tag() == 1) {
               return (String)c.value();
            }
         }
      }

      throw new ClassFormatError("Couldn't find class name in file");
   }

   Constant readConstant(DataInputStream in) throws IOException {
      int tag = in.readUnsignedByte();
      Object value;
      switch(tag) {
      case 1:
         value = in.readUTF();
         break;
      case 2:
      default:
         throw new ClassFormatError("Invalid constant tag: " + tag);
      case 3:
         value = new Integer(in.readInt());
         break;
      case 4:
         value = new Float(in.readFloat());
         break;
      case 5:
         value = new Long(in.readLong());
         break;
      case 6:
         value = new Double(in.readDouble());
         break;
      case 7:
      case 8:
         value = new Integer(in.readUnsignedShort());
         break;
      case 9:
      case 10:
      case 11:
      case 12:
         value = new int[2];
         ((int[])value)[0] = in.readUnsignedShort();
         ((int[])value)[1] = in.readUnsignedShort();
      }

      return new Constant(tag, value);
   }

   void readHeader(DataInputStream in) throws IOException {
      int magic = in.readInt();
      if (magic != -889275714) {
         throw new ClassFormatError("Bad magic number.");
      } else {
         int major = in.readUnsignedShort();
         int minor = in.readUnsignedShort();
      }
   }

   void readConstantPool(DataInputStream in) throws IOException {
      int count = in.readUnsignedShort();
      this.constants = new Constant[count];
      this.constants[0] = null;
      int i = 1;

      while(i < count) {
         this.constants[i] = this.readConstant(in);
         switch(this.constants[i].tag()) {
         case 5:
         case 6:
            ++i;
            this.constants[i] = null;
         default:
            ++i;
         }
      }

   }

   void readAccessFlags(DataInputStream in) throws IOException {
      this.modifiers = in.readUnsignedShort();
   }

   void readClassInfo(DataInputStream in) throws IOException {
      this.thisClass = in.readUnsignedShort();
      this.superClass = in.readUnsignedShort();
      int numInterfaces = in.readUnsignedShort();
      this.interfaces = new int[numInterfaces];

      for(int i = 0; i < numInterfaces; ++i) {
         this.interfaces[i] = in.readUnsignedShort();
      }

   }

   void readFields(DataInputStream in) throws IOException {
      int numFields = in.readUnsignedShort();
      this.fields = new Field[numFields];

      for(int i = 0; i < numFields; ++i) {
         this.fields[i] = this.createField(in);
      }

   }

   void readMethods(DataInputStream in) throws IOException {
      int numMethods = in.readUnsignedShort();
      this.methods = new Method[numMethods];

      for(int i = 0; i < numMethods; ++i) {
         this.methods[i] = this.createMethod(in);
      }

   }

   public void readAttributes(DataInputStream in) throws IOException {
      int numAttributes = in.readUnsignedShort();
      this.attrs = new Attribute[numAttributes];

      for(int i = 0; i < numAttributes; ++i) {
         int nameIndex = in.readUnsignedShort();
         int length = in.readInt();
         String name = (String)this.constants[nameIndex].value();
         Attribute a = this.createAttribute(in, name, nameIndex, length);
         if (a != null) {
            this.attrs[i] = a;
         } else {
            long n = in.skip((long)length);
            if (n != (long)length) {
               throw new EOFException();
            }
         }
      }

   }

   static {
      verbose = ClassFileLoader.verbose;
   }
}

package soot.javaToJimple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefLikeType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.VoidType;
import soot.jimple.Jimple;

public class LocalGenerator {
   protected final Body body;
   protected transient Set<String> localNames = null;
   private int tempInt = -1;
   private int tempVoid = -1;
   private int tempBoolean = -1;
   private int tempLong = -1;
   private int tempDouble = -1;
   private int tempFloat = -1;
   private int tempRefLikeType = -1;
   private int tempByte = -1;
   private int tempShort = -1;
   private int tempChar = -1;
   private int tempUnknownType = -1;

   public LocalGenerator(Body b) {
      this.body = b;
   }

   protected boolean bodyContainsLocal(String name) {
      return this.localNames.contains(name);
   }

   private void initLocalNames() {
      this.localNames = new HashSet();
      Iterator var1 = this.body.getLocals().iterator();

      while(var1.hasNext()) {
         Local l = (Local)var1.next();
         this.localNames.add(l.getName());
      }

   }

   public Local generateLocal(Type type) {
      this.initLocalNames();
      String name = "v";
      if (type instanceof IntType) {
         do {
            name = this.nextIntName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof ByteType) {
         do {
            name = this.nextByteName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof ShortType) {
         do {
            name = this.nextShortName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof BooleanType) {
         do {
            name = this.nextBooleanName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof VoidType) {
         do {
            name = this.nextVoidName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof CharType) {
         do {
            name = this.nextCharName();
         } while(this.bodyContainsLocal(name));

         type = CharType.v();
      } else if (type instanceof DoubleType) {
         do {
            name = this.nextDoubleName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof FloatType) {
         do {
            name = this.nextFloatName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof LongType) {
         do {
            name = this.nextLongName();
         } while(this.bodyContainsLocal(name));
      } else if (type instanceof RefLikeType) {
         do {
            name = this.nextRefLikeTypeName();
         } while(this.bodyContainsLocal(name));
      } else {
         if (!(type instanceof UnknownType)) {
            this.localNames = null;
            throw new RuntimeException("Unhandled Type of Local variable to Generate - Not Implemented");
         }

         do {
            name = this.nextUnknownTypeName();
         } while(this.bodyContainsLocal(name));
      }

      this.localNames = null;
      return this.createLocal(name, (Type)type);
   }

   private String nextIntName() {
      ++this.tempInt;
      return "$i" + this.tempInt;
   }

   private String nextCharName() {
      ++this.tempChar;
      return "$c" + this.tempChar;
   }

   private String nextVoidName() {
      ++this.tempVoid;
      return "$v" + this.tempVoid;
   }

   private String nextByteName() {
      ++this.tempByte;
      return "$b" + this.tempByte;
   }

   private String nextShortName() {
      ++this.tempShort;
      return "$s" + this.tempShort;
   }

   private String nextBooleanName() {
      ++this.tempBoolean;
      return "$z" + this.tempBoolean;
   }

   private String nextDoubleName() {
      ++this.tempDouble;
      return "$d" + this.tempDouble;
   }

   private String nextFloatName() {
      ++this.tempFloat;
      return "$f" + this.tempFloat;
   }

   private String nextLongName() {
      ++this.tempLong;
      return "$l" + this.tempLong;
   }

   private String nextRefLikeTypeName() {
      ++this.tempRefLikeType;
      return "$r" + this.tempRefLikeType;
   }

   private String nextUnknownTypeName() {
      ++this.tempUnknownType;
      return "$u" + this.tempUnknownType;
   }

   protected Local createLocal(String name, Type sootType) {
      Local sootLocal = Jimple.v().newLocal(name, sootType);
      this.body.getLocals().add(sootLocal);
      return sootLocal;
   }
}

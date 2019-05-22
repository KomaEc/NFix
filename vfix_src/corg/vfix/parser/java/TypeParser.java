package corg.vfix.parser.java;

import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;

public class TypeParser {
   public static void main(Type type) {
      if (type.isArrayType()) {
         parseArrayType((ArrayType)type);
      } else if (type.isPrimitiveType()) {
         parseArrayType((PrimitiveType)type);
      } else if (type.isReferenceType()) {
         parseRefType((ReferenceType)type);
      }

   }

   private static void parseRefType(ReferenceType type) {
      System.out.println("RefType");
   }

   private static void parseArrayType(PrimitiveType type) {
      System.out.println("PrimitiveType");
   }

   private static void parseArrayType(ArrayType type) {
      System.out.println("ArrayType");
      System.out.println("Origin: " + type.getOrigin());
      System.out.println("ComponentType: " + type.getComponentType());
   }
}

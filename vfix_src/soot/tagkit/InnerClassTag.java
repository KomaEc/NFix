package soot.tagkit;

import java.io.UnsupportedEncodingException;

public class InnerClassTag implements Tag {
   String innerClass;
   String outerClass;
   String name;
   int accessFlags;

   public InnerClassTag(String innerClass, String outerClass, String name, int accessFlags) {
      this.innerClass = innerClass;
      this.outerClass = outerClass;
      this.name = name;
      this.accessFlags = accessFlags;
      if (innerClass != null && innerClass.startsWith("L") && innerClass.endsWith(";")) {
         throw new RuntimeException("InnerClass annotation type string must be of the form a/b/ClassName not '" + innerClass + "'");
      } else if (outerClass != null && outerClass.startsWith("L") && outerClass.endsWith(";")) {
         throw new RuntimeException("OuterType annotation type string must be of the form a/b/ClassName not '" + innerClass + "'");
      } else if (name != null && name.endsWith(";")) {
         throw new RuntimeException("InnerClass name cannot end with ';', got '" + name + "'");
      }
   }

   public String getName() {
      return "InnerClassTag";
   }

   public byte[] getValue() {
      try {
         return this.innerClass.getBytes("UTF8");
      } catch (UnsupportedEncodingException var2) {
         return new byte[0];
      }
   }

   public String getInnerClass() {
      return this.innerClass;
   }

   public String getOuterClass() {
      return this.outerClass;
   }

   public String getShortName() {
      return this.name;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   public String toString() {
      return "[inner=" + this.innerClass + ", outer=" + this.outerClass + ", name=" + this.name + ",flags=" + this.accessFlags + "]";
   }
}

package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.Option;

public final class TestInfo {
   private final String name;
   private final String definingClass;
   private final int time;
   private final int blocks;
   private final Option<ClassName> testee;

   public TestInfo(String definingClass, String name, int time, Option<ClassName> testee, int blocksCovered) {
      this.definingClass = this.internIfNotNull(definingClass);
      this.name = name;
      this.time = time;
      this.testee = testee;
      this.blocks = blocksCovered;
   }

   private String internIfNotNull(String string) {
      return string == null ? null : string.intern();
   }

   public String getName() {
      return this.name;
   }

   public int getTime() {
      return this.time;
   }

   public int getNumberOfBlocksCovered() {
      return this.blocks;
   }

   public String toString() {
      return this.name;
   }

   public static F<TestInfo, String> toName() {
      return new F<TestInfo, String>() {
         public String apply(TestInfo a) {
            return a.getName();
         }
      };
   }

   public static F<TestInfo, ClassName> toDefiningClassName() {
      return new F<TestInfo, ClassName>() {
         public ClassName apply(TestInfo a) {
            return new ClassName(a.definingClass);
         }
      };
   }

   public boolean directlyHits(ClassName targetClass) {
      return this.testee.hasSome() && ((ClassName)this.testee.value()).equals(targetClass);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.definingClass == null ? 0 : this.definingClass.hashCode());
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         TestInfo other = (TestInfo)obj;
         if (this.definingClass == null) {
            if (other.definingClass != null) {
               return false;
            }
         } else if (!this.definingClass.equals(other.definingClass)) {
            return false;
         }

         if (this.name == null) {
            if (other.name != null) {
               return false;
            }
         } else if (!this.name.equals(other.name)) {
            return false;
         }

         return true;
      }
   }
}

package com.google.inject.internal.util;

import com.google.common.collect.MapMaker;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Map;

public class StackTraceElements {
   private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
   private static final StackTraceElements.InMemoryStackTraceElement[] EMPTY_INMEMORY_STACK_TRACE = new StackTraceElements.InMemoryStackTraceElement[0];
   private static Map<Object, Object> cache = (new MapMaker()).makeMap();
   private static final String UNKNOWN_SOURCE = "Unknown Source";

   public static Object forMember(Member member) {
      if (member == null) {
         return SourceProvider.UNKNOWN_SOURCE;
      } else {
         Class declaringClass = member.getDeclaringClass();
         String fileName = null;
         int lineNumber = -1;
         Class<? extends Member> memberType = Classes.memberType(member);
         String memberName = memberType == Constructor.class ? "<init>" : member.getName();
         return new StackTraceElement(declaringClass.getName(), memberName, (String)fileName, lineNumber);
      }
   }

   public static Object forType(Class<?> implementation) {
      String fileName = null;
      int lineNumber = -1;
      return new StackTraceElement(implementation.getName(), "class", (String)fileName, lineNumber);
   }

   public static void clearCache() {
      cache.clear();
   }

   public static StackTraceElements.InMemoryStackTraceElement[] convertToInMemoryStackTraceElement(StackTraceElement[] stackTraceElements) {
      if (stackTraceElements.length == 0) {
         return EMPTY_INMEMORY_STACK_TRACE;
      } else {
         StackTraceElements.InMemoryStackTraceElement[] inMemoryStackTraceElements = new StackTraceElements.InMemoryStackTraceElement[stackTraceElements.length];

         for(int i = 0; i < stackTraceElements.length; ++i) {
            inMemoryStackTraceElements[i] = weakIntern(new StackTraceElements.InMemoryStackTraceElement(stackTraceElements[i]));
         }

         return inMemoryStackTraceElements;
      }
   }

   public static StackTraceElement[] convertToStackTraceElement(StackTraceElements.InMemoryStackTraceElement[] inMemoryStackTraceElements) {
      if (inMemoryStackTraceElements.length == 0) {
         return EMPTY_STACK_TRACE;
      } else {
         StackTraceElement[] stackTraceElements = new StackTraceElement[inMemoryStackTraceElements.length];

         for(int i = 0; i < inMemoryStackTraceElements.length; ++i) {
            String declaringClass = inMemoryStackTraceElements[i].getClassName();
            String methodName = inMemoryStackTraceElements[i].getMethodName();
            int lineNumber = inMemoryStackTraceElements[i].getLineNumber();
            stackTraceElements[i] = new StackTraceElement(declaringClass, methodName, "Unknown Source", lineNumber);
         }

         return stackTraceElements;
      }
   }

   private static StackTraceElements.InMemoryStackTraceElement weakIntern(StackTraceElements.InMemoryStackTraceElement inMemoryStackTraceElement) {
      StackTraceElements.InMemoryStackTraceElement cached = (StackTraceElements.InMemoryStackTraceElement)cache.get(inMemoryStackTraceElement);
      if (cached != null) {
         return cached;
      } else {
         inMemoryStackTraceElement = new StackTraceElements.InMemoryStackTraceElement(weakIntern(inMemoryStackTraceElement.getClassName()), weakIntern(inMemoryStackTraceElement.getMethodName()), inMemoryStackTraceElement.getLineNumber());
         cache.put(inMemoryStackTraceElement, inMemoryStackTraceElement);
         return inMemoryStackTraceElement;
      }
   }

   private static String weakIntern(String s) {
      String cached = (String)cache.get(s);
      if (cached != null) {
         return cached;
      } else {
         cache.put(s, s);
         return s;
      }
   }

   public static class InMemoryStackTraceElement {
      private String declaringClass;
      private String methodName;
      private int lineNumber;

      InMemoryStackTraceElement(StackTraceElement ste) {
         this(ste.getClassName(), ste.getMethodName(), ste.getLineNumber());
      }

      InMemoryStackTraceElement(String declaringClass, String methodName, int lineNumber) {
         this.declaringClass = declaringClass;
         this.methodName = methodName;
         this.lineNumber = lineNumber;
      }

      String getClassName() {
         return this.declaringClass;
      }

      String getMethodName() {
         return this.methodName;
      }

      int getLineNumber() {
         return this.lineNumber;
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof StackTraceElements.InMemoryStackTraceElement)) {
            return false;
         } else {
            StackTraceElements.InMemoryStackTraceElement e = (StackTraceElements.InMemoryStackTraceElement)obj;
            return e.declaringClass.equals(this.declaringClass) && e.lineNumber == this.lineNumber && this.methodName.equals(e.methodName);
         }
      }

      public int hashCode() {
         int result = 31 * this.declaringClass.hashCode() + this.methodName.hashCode();
         result = 31 * result + this.lineNumber;
         return result;
      }

      public String toString() {
         String var1 = String.valueOf(String.valueOf(this.declaringClass));
         String var2 = String.valueOf(String.valueOf(this.methodName));
         int var3 = this.lineNumber;
         return (new StringBuilder(14 + var1.length() + var2.length())).append(var1).append(".").append(var2).append("(").append(var3).append(")").toString();
      }
   }
}

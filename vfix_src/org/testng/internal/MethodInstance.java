package org.testng.internal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.testng.IMethodInstance;
import org.testng.ITestNGMethod;
import org.testng.collections.Objects;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlTest;

public class MethodInstance implements IMethodInstance {
   private ITestNGMethod m_method;
   public static final Comparator<IMethodInstance> SORT_BY_INDEX = new Comparator<IMethodInstance>() {
      public int compare(IMethodInstance o1, IMethodInstance o2) {
         XmlTest test1 = o1.getMethod().getTestClass().getXmlTest();
         XmlTest test2 = o2.getMethod().getTestClass().getXmlTest();
         if (!test1.getName().equals(test2.getName())) {
            return 0;
         } else {
            int result = 0;
            XmlClass class1 = o1.getMethod().getTestClass().getXmlClass();
            XmlClass class2 = o2.getMethod().getTestClass().getXmlClass();
            if (class1 != null && class2 != null) {
               if (!class1.getName().equals(class2.getName())) {
                  int index1 = class1.getIndex();
                  int index2 = class2.getIndex();
                  result = index1 - index2;
               } else {
                  XmlInclude include1 = this.findXmlInclude(class1.getIncludedMethods(), o1.getMethod().getMethodName());
                  XmlInclude include2 = this.findXmlInclude(class2.getIncludedMethods(), o2.getMethod().getMethodName());
                  if (include1 != null && include2 != null) {
                     result = include1.getIndex() - include2.getIndex();
                  }
               }

               return result;
            } else if (class1 != null) {
               return -1;
            } else {
               return class2 != null ? 1 : 0;
            }
         }
      }

      private XmlInclude findXmlInclude(List<XmlInclude> includedMethods, String methodName) {
         Iterator i$ = includedMethods.iterator();

         XmlInclude xi;
         do {
            if (!i$.hasNext()) {
               return null;
            }

            xi = (XmlInclude)i$.next();
         } while(!xi.getName().equals(methodName));

         return xi;
      }
   };

   public MethodInstance(ITestNGMethod method) {
      this.m_method = method;
   }

   public ITestNGMethod getMethod() {
      return this.m_method;
   }

   public Object[] getInstances() {
      return new Object[]{this.getInstance()};
   }

   public Object getInstance() {
      return this.m_method.getInstance();
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("method", (Object)this.m_method).add("instance", this.getInstance()).toString();
   }
}

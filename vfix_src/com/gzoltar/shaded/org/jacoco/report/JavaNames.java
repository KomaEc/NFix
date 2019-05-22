package com.gzoltar.shaded.org.jacoco.report;

import com.gzoltar.shaded.org.objectweb.asm.Type;

public class JavaNames implements ILanguageNames {
   public String getPackageName(String vmname) {
      return vmname.length() == 0 ? "default" : vmname.replace('/', '.');
   }

   private String getClassName(String vmname) {
      int pos = vmname.lastIndexOf(47);
      String name = pos == -1 ? vmname : vmname.substring(pos + 1);
      return name.replace('$', '.');
   }

   private boolean isAnonymous(String vmname) {
      int dollarPosition = vmname.lastIndexOf(36);
      if (dollarPosition == -1) {
         return false;
      } else {
         int internalPosition = dollarPosition + 1;
         if (internalPosition == vmname.length()) {
            return false;
         } else {
            char start = vmname.charAt(internalPosition);
            return !Character.isJavaIdentifierStart(start);
         }
      }
   }

   public String getClassName(String vmname, String vmsignature, String vmsuperclass, String[] vminterfaces) {
      if (this.isAnonymous(vmname)) {
         String vmsupertype;
         if (vminterfaces != null && vminterfaces.length > 0) {
            vmsupertype = vminterfaces[0];
         } else if (vmsuperclass != null) {
            vmsupertype = vmsuperclass;
         } else {
            vmsupertype = null;
         }

         if (vmsupertype != null) {
            StringBuilder builder = new StringBuilder();
            String vmenclosing = vmname.substring(0, vmname.lastIndexOf(36));
            builder.append(this.getClassName(vmenclosing)).append(".new ").append(this.getClassName(vmsupertype)).append("() {...}");
            return builder.toString();
         }
      }

      return this.getClassName(vmname);
   }

   public String getQualifiedClassName(String vmname) {
      return vmname.replace('/', '.').replace('$', '.');
   }

   public String getMethodName(String vmclassname, String vmmethodname, String vmdesc, String vmsignature) {
      return this.getMethodName(vmclassname, vmmethodname, vmdesc, false);
   }

   public String getQualifiedMethodName(String vmclassname, String vmmethodname, String vmdesc, String vmsignature) {
      return this.getQualifiedClassName(vmclassname) + "." + this.getMethodName(vmclassname, vmmethodname, vmdesc, true);
   }

   private String getMethodName(String vmclassname, String vmmethodname, String vmdesc, boolean qualifiedParams) {
      if ("<clinit>".equals(vmmethodname)) {
         return "static {...}";
      } else {
         StringBuilder result = new StringBuilder();
         if ("<init>".equals(vmmethodname)) {
            if (this.isAnonymous(vmclassname)) {
               return "{...}";
            }

            result.append(this.getClassName(vmclassname));
         } else {
            result.append(vmmethodname);
         }

         result.append('(');
         Type[] arguments = Type.getArgumentTypes(vmdesc);
         boolean comma = false;
         Type[] arr$ = arguments;
         int len$ = arguments.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Type arg = arr$[i$];
            if (comma) {
               result.append(", ");
            } else {
               comma = true;
            }

            if (qualifiedParams) {
               result.append(this.getQualifiedClassName(arg.getClassName()));
            } else {
               result.append(this.getShortTypeName(arg));
            }
         }

         result.append(')');
         return result.toString();
      }
   }

   private String getShortTypeName(Type type) {
      String name = type.getClassName();
      int pos = name.lastIndexOf(46);
      String shortName = pos == -1 ? name : name.substring(pos + 1);
      return shortName.replace('$', '.');
   }
}

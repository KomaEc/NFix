package org.jf.dexlib2.analysis;

import javax.annotation.Nonnull;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.analysis.util.TypeProtoUtils;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.util.TypeUtils;

public class AnalyzedMethodUtil {
   public static boolean canAccess(@Nonnull TypeProto type, @Nonnull Method virtualMethod, boolean checkPackagePrivate, boolean checkProtected, boolean checkClass) {
      if (checkPackagePrivate && MethodUtil.isPackagePrivate(virtualMethod)) {
         String otherPackage = TypeUtils.getPackage(virtualMethod.getDefiningClass());
         String thisPackage = TypeUtils.getPackage(type.getType());
         if (!otherPackage.equals(thisPackage)) {
            return false;
         }
      }

      if (checkProtected && (virtualMethod.getAccessFlags() & AccessFlags.PROTECTED.getValue()) != 0 && !TypeProtoUtils.extendsFrom(type, virtualMethod.getDefiningClass())) {
         return false;
      } else {
         if (checkClass) {
            ClassPath classPath = type.getClassPath();
            ClassDef methodClassDef = classPath.getClassDef(virtualMethod.getDefiningClass());
            if (!TypeUtils.canAccessClass(type.getType(), methodClassDef)) {
               return false;
            }
         }

         return true;
      }
   }
}

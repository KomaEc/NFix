package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GLBTypeFactory {
   public static TypeDecl glb(ArrayList typeList) {
      TypeDecl retType = ((TypeDecl)typeList.get(0)).unknownType();
      TypeDecl cls = mostSpecificSuperClass(typeList);
      if (cls != null) {
         ArrayList intersectInterfaceList = new ArrayList();
         ArrayList allInterfaceList = new ArrayList();
         Iterator itera = typeList.iterator();

         while(itera.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)itera.next();
            addInterfaces(intersectInterfaceList, allInterfaceList, typeDecl);
         }

         greatestLowerBounds(intersectInterfaceList);
         if (checkInterfaceCompatibility(allInterfaceList) && checkClassInterfaceCompatibility(cls, intersectInterfaceList)) {
            greatestLowerBounds(typeList);
            if (typeList.size() == 1) {
               retType = (TypeDecl)typeList.iterator().next();
            } else {
               retType = ((TypeDecl)retType).lookupGLBType(typeList);
            }
         }
      }

      return (TypeDecl)retType;
   }

   private static void addInterfaces(ArrayList intersectInterfaceList, ArrayList allInterfaceList, TypeDecl typeDecl) {
      if (typeDecl.isInterfaceDecl()) {
         intersectInterfaceList.add((InterfaceDecl)typeDecl);
         allInterfaceList.add((InterfaceDecl)typeDecl);
      } else if (typeDecl instanceof TypeVariable) {
         TypeVariable varTD = (TypeVariable)typeDecl;
         intersectInterfaceList.add(varTD.toInterface());
         allInterfaceList.addAll(varTD.implementedInterfaces());
      } else if (typeDecl instanceof LUBType) {
         allInterfaceList.addAll(typeDecl.implementedInterfaces());
      } else if (typeDecl instanceof GLBType) {
         allInterfaceList.addAll(typeDecl.implementedInterfaces());
      }

   }

   public static final TypeDecl mostSpecificSuperClass(ArrayList types) {
      ArrayList csList = new ArrayList();
      Iterator iter = types.iterator();

      while(iter.hasNext()) {
         csList.add(mostSpecificSuperClass((TypeDecl)iter.next()));
      }

      greatestLowerBounds(csList);
      return csList.size() == 1 ? (TypeDecl)csList.get(0) : null;
   }

   private static final TypeDecl mostSpecificSuperClass(TypeDecl t) {
      HashSet superTypes = new HashSet();
      addSuperClasses(t, superTypes);
      if (superTypes.isEmpty()) {
         return t.typeObject();
      } else {
         ArrayList result = new ArrayList(superTypes.size());
         result.addAll(superTypes);
         greatestLowerBounds(result);
         return result.size() == 1 ? (TypeDecl)result.get(0) : t.typeObject();
      }
   }

   private static final void addSuperClasses(TypeDecl t, HashSet result) {
      if (t != null) {
         if (t.isClassDecl() && !result.contains(t)) {
            result.add((ClassDecl)t);
         } else if (t.isTypeVariable()) {
            TypeVariable var = (TypeVariable)t;

            for(int i = 0; i < var.getNumTypeBound(); ++i) {
               addSuperClasses(var.getTypeBound(i).type(), result);
            }
         } else if (!(t instanceof LUBType) && !(t instanceof GLBType)) {
            if (t.isInterfaceDecl()) {
               result.add((ClassDecl)t.typeObject());
            }
         } else {
            result.add(t);
         }

      }
   }

   private static boolean checkInterfaceCompatibility(ArrayList ifaceList) {
      label50:
      for(int i = 0; i < ifaceList.size(); ++i) {
         HashSet superISet_i = Constraints.parameterizedSupertypes((TypeDecl)ifaceList.get(i));
         Iterator iter1 = superISet_i.iterator();

         while(true) {
            InterfaceDecl superIface_i;
            do {
               if (!iter1.hasNext()) {
                  continue label50;
               }

               superIface_i = (InterfaceDecl)iter1.next();
            } while(!(superIface_i instanceof ParInterfaceDecl));

            ParInterfaceDecl pi = (ParInterfaceDecl)superIface_i;

            for(int j = i + 1; j < ifaceList.size(); ++j) {
               HashSet superISet_j = Constraints.parameterizedSupertypes((TypeDecl)ifaceList.get(j));
               Iterator iter2 = superISet_j.iterator();

               while(iter2.hasNext()) {
                  InterfaceDecl superIface_j = (InterfaceDecl)iter2.next();
                  if (superIface_j instanceof ParInterfaceDecl) {
                     ParInterfaceDecl pj = (ParInterfaceDecl)superIface_j;
                     if (pi != pj && pi.genericDecl() == pj.genericDecl() && !pi.sameArgument(pj)) {
                        return false;
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   private static boolean checkClassInterfaceCompatibility(TypeDecl cls, ArrayList ifaceList) {
      HashSet implementedInterfaces = cls.implementedInterfaces();
      Iterator iter1 = implementedInterfaces.iterator();

      while(true) {
         InterfaceDecl impInterface;
         do {
            if (!iter1.hasNext()) {
               return true;
            }

            impInterface = (InterfaceDecl)iter1.next();
         } while(!(impInterface instanceof ParInterfaceDecl));

         ParInterfaceDecl impParIface = (ParInterfaceDecl)impInterface;
         Iterator iter2 = ifaceList.iterator();

         while(iter2.hasNext()) {
            InterfaceDecl iface = (InterfaceDecl)iter2.next();
            if (iface instanceof ParInterfaceDecl) {
               ParInterfaceDecl parIface = (ParInterfaceDecl)iface;
               if (parIface != impParIface && parIface.genericDecl() == impParIface.genericDecl() && !parIface.sameArgument(impParIface)) {
                  return false;
               }
            }
         }
      }
   }

   public static final void greatestLowerBounds(ArrayList types) {
      for(int i = 0; i < types.size(); ++i) {
         TypeDecl U = (TypeDecl)types.get(i);

         for(int j = i + 1; j < types.size(); ++j) {
            TypeDecl V = (TypeDecl)types.get(j);
            if (U != null && V != null) {
               if (U.instanceOf(V)) {
                  types.set(j, (Object)null);
               } else if (V.instanceOf(U)) {
                  types.set(i, (Object)null);
               }
            }
         }
      }

      removeNullValues(types);
   }

   public static final void removeNullValues(ArrayList types) {
      ArrayList filter = new ArrayList(1);
      filter.add((Object)null);
      types.removeAll(filter);
   }
}

package soot.JastAddJ;

import java.util.ArrayList;

public interface GenericTypeDecl {
   TypeDecl original();

   int getNumTypeParameter();

   TypeVariable getTypeParameter(int var1);

   List getTypeParameterList();

   String fullName();

   String typeName();

   TypeDecl makeGeneric(Signatures.ClassSignature var1);

   SimpleSet addTypeVariables(SimpleSet var1, String var2);

   List createArgumentList(ArrayList var1);

   boolean isGenericType();

   TypeDecl rawType();

   TypeDecl lookupParTypeDecl(ParTypeAccess var1);

   TypeDecl lookupParTypeDecl(ArrayList var1);
}

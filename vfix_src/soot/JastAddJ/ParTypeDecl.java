package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;

public interface ParTypeDecl extends Parameterization {
   int getNumArgument();

   Access getArgument(int var1);

   String typeName();

   SimpleSet localFields(String var1);

   HashMap localMethodsSignatureMap();

   TypeDecl substitute(TypeVariable var1);

   int numTypeParameter();

   TypeVariable typeParameter(int var1);

   Access substitute(Parameterization var1);

   Access createQualifiedAccess();

   void transformation();

   boolean isParameterizedType();

   boolean isRawType();

   boolean sameArgument(ParTypeDecl var1);

   boolean sameSignature(Access var1);

   boolean sameSignature(ArrayList var1);

   String nameWithArgs();

   TypeDecl genericDecl();
}

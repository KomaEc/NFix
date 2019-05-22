package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;

public interface MemberSubstitutor extends Parameterization {
   TypeDecl original();

   void addBodyDecl(BodyDecl var1);

   TypeDecl substitute(TypeVariable var1);

   HashMap localMethodsSignatureMap();

   SimpleSet localFields(String var1);

   SimpleSet localTypeDecls(String var1);

   Collection constructors();
}

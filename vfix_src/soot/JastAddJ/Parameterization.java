package soot.JastAddJ;

public interface Parameterization {
   boolean isRawType();

   TypeDecl substitute(TypeVariable var1);
}

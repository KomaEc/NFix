package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Constraints {
   private Collection typeVariables = new ArrayList(4);
   private Map constraintsMap = new HashMap();
   public boolean rawAccess = false;

   public void addTypeVariable(TypeVariable T) {
      if (!this.typeVariables.contains(T)) {
         this.typeVariables.add(T);
         this.constraintsMap.put(T, new Constraints.ConstraintSet());
      }

   }

   public boolean unresolvedTypeArguments() {
      Iterator iter = this.typeVariables.iterator();

      Constraints.ConstraintSet set;
      do {
         if (!iter.hasNext()) {
            return false;
         }

         TypeVariable T = (TypeVariable)iter.next();
         set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
      } while(set.typeArgument != null);

      return true;
   }

   public void printConstraints() {
      System.err.println("Current constraints:");
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         Iterator i2 = set.supertypeConstraints.iterator();

         TypeDecl U;
         while(i2.hasNext()) {
            U = (TypeDecl)i2.next();
            System.err.println("  " + T.fullName() + " :> " + U.fullName());
         }

         i2 = set.subtypeConstraints.iterator();

         while(i2.hasNext()) {
            U = (TypeDecl)i2.next();
            System.err.println("  " + T.fullName() + " <: " + U.fullName());
         }

         i2 = set.equaltypeConstraints.iterator();

         while(i2.hasNext()) {
            U = (TypeDecl)i2.next();
            System.err.println("  " + T.fullName() + " = " + U.fullName());
         }
      }

   }

   public void resolveBounds() {
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         if (set.typeArgument == null) {
            set.typeArgument = T.getTypeBound(0).type();
         }
      }

   }

   public void resolveEqualityConstraints() {
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         boolean done = false;
         Iterator i2 = set.equaltypeConstraints.iterator();

         while(!done && i2.hasNext()) {
            TypeDecl U = (TypeDecl)i2.next();
            if (!this.typeVariables.contains(U)) {
               this.replaceEqualityConstraints(T, U);
               set.equaltypeConstraints.clear();
               set.equaltypeConstraints.add(U);
               set.typeArgument = U;
               done = true;
            } else if (T != U) {
               this.replaceAllConstraints(T, U);
               done = true;
            }
         }

         if (set.typeArgument == null && set.equaltypeConstraints.size() == 1 && set.equaltypeConstraints.contains(T)) {
            set.typeArgument = T;
         }
      }

   }

   public void replaceEqualityConstraints(TypeDecl before, TypeDecl after) {
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         this.replaceConstraints(set.equaltypeConstraints, before, after);
      }

   }

   public void replaceAllConstraints(TypeDecl before, TypeDecl after) {
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         this.replaceConstraints(set.supertypeConstraints, before, after);
         this.replaceConstraints(set.subtypeConstraints, before, after);
         this.replaceConstraints(set.equaltypeConstraints, before, after);
      }

   }

   private void replaceConstraints(Collection constraints, TypeDecl before, TypeDecl after) {
      Collection newConstraints = new ArrayList();
      Iterator i2 = constraints.iterator();

      while(i2.hasNext()) {
         TypeDecl U = (TypeDecl)i2.next();
         if (U == before) {
            i2.remove();
            newConstraints.add(after);
         }
      }

      constraints.addAll(newConstraints);
   }

   public void resolveSubtypeConstraints() {
      Iterator iter = this.typeVariables.iterator();

      while(true) {
         TypeVariable T;
         Constraints.ConstraintSet set;
         do {
            do {
               if (!iter.hasNext()) {
                  return;
               }

               T = (TypeVariable)iter.next();
               set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
            } while(set.subtypeConstraints.isEmpty() && T.getNumTypeBound() <= 0);
         } while(set.typeArgument != null);

         ArrayList bounds = new ArrayList();
         Iterator i2 = set.subtypeConstraints.iterator();

         while(i2.hasNext()) {
            bounds.add(i2.next());
         }

         for(int i = 0; i < T.getNumTypeBound(); ++i) {
            bounds.add(T.getTypeBound(i).type());
         }

         set.typeArgument = GLBTypeFactory.glb(bounds);
      }
   }

   public void resolveSupertypeConstraints() {
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         if (!set.supertypeConstraints.isEmpty() && set.typeArgument == null) {
            TypeDecl typeDecl = T.lookupLUBType(set.supertypeConstraints).lub();
            set.typeArgument = typeDecl;
         }
      }

   }

   public static HashSet directSupertypes(TypeDecl t) {
      HashSet set;
      int i;
      if (t instanceof ClassDecl) {
         ClassDecl type = (ClassDecl)t;
         set = new HashSet();
         if (type.hasSuperclass()) {
            set.add(type.superclass());
         }

         for(i = 0; i < type.getNumImplements(); ++i) {
            set.add(type.getImplements(i).type());
         }

         return set;
      } else if (t instanceof InterfaceDecl) {
         InterfaceDecl type = (InterfaceDecl)t;
         set = new HashSet();

         for(i = 0; i < type.getNumSuperInterfaceId(); ++i) {
            set.add(type.getSuperInterfaceId(i).type());
         }

         return set;
      } else if (!(t instanceof TypeVariable)) {
         throw new Error("Operation not supported for " + t.fullName() + ", " + t.getClass().getName());
      } else {
         TypeVariable type = (TypeVariable)t;
         set = new HashSet();

         for(i = 0; i < type.getNumTypeBound(); ++i) {
            set.add(type.getTypeBound(i).type());
         }

         return set;
      }
   }

   public static HashSet parameterizedSupertypes(TypeDecl t) {
      HashSet result = new HashSet();
      addParameterizedSupertypes(t, new HashSet(), result);
      return result;
   }

   public static void addParameterizedSupertypes(TypeDecl t, HashSet processed, HashSet result) {
      if (!processed.contains(t)) {
         processed.add(t);
         if (t.isParameterizedType()) {
            result.add(t);
         }

         Iterator iter = directSupertypes(t).iterator();

         while(iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)iter.next();
            addParameterizedSupertypes(typeDecl, processed, result);
         }
      }

   }

   public Collection typeArguments() {
      ArrayList list = new ArrayList(this.typeVariables.size());
      Iterator iter = this.typeVariables.iterator();

      while(iter.hasNext()) {
         TypeVariable T = (TypeVariable)iter.next();
         Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
         list.add(set.typeArgument);
      }

      return list;
   }

   public void addSupertypeConstraint(TypeDecl T, TypeDecl A) {
      Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
      set.supertypeConstraints.add(A);
   }

   public void addSubtypeConstraint(TypeDecl T, TypeDecl A) {
      Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
      set.subtypeConstraints.add(A);
   }

   public void addEqualConstraint(TypeDecl T, TypeDecl A) {
      Constraints.ConstraintSet set = (Constraints.ConstraintSet)this.constraintsMap.get(T);
      set.equaltypeConstraints.add(A);
   }

   public void convertibleTo(TypeDecl A, TypeDecl F) {
      if (F.involvesTypeParameters()) {
         if (!A.isNull()) {
            TypeDecl U;
            if (A.isUnboxedPrimitive()) {
               U = A.boxed();
               this.convertibleTo(U, F);
            } else if (F instanceof TypeVariable) {
               if (this.typeVariables.contains(F)) {
                  this.addSupertypeConstraint(F, A);
               }
            } else {
               TypeDecl T;
               if (F.isArrayDecl()) {
                  U = ((ArrayDecl)F).componentType();
                  if (!U.involvesTypeParameters()) {
                     return;
                  }

                  if (A.isArrayDecl()) {
                     TypeDecl V = ((ArrayDecl)A).componentType();
                     if (V.isReferenceType()) {
                        this.convertibleTo(V, U);
                     }
                  } else if (A.isTypeVariable()) {
                     TypeVariable t = (TypeVariable)A;

                     for(int i = 0; i < t.getNumTypeBound(); ++i) {
                        TypeDecl typeBound = t.getTypeBound(i).type();
                        if (typeBound.isArrayDecl() && ((ArrayDecl)typeBound).componentType().isReferenceType()) {
                           T = ((ArrayDecl)typeBound).componentType();
                           this.convertibleTo(T, U);
                        }
                     }
                  }
               } else if (F instanceof ParTypeDecl && !F.isRawType()) {
                  Iterator iter = parameterizedSupertypes(A).iterator();

                  while(true) {
                     while(true) {
                        ParTypeDecl PF;
                        ParTypeDecl PA;
                        do {
                           if (!iter.hasNext()) {
                              return;
                           }

                           PF = (ParTypeDecl)F;
                           PA = (ParTypeDecl)iter.next();
                        } while(PF.genericDecl() != PA.genericDecl());

                        if (A.isRawType()) {
                           this.rawAccess = true;
                        } else {
                           for(int i = 0; i < PF.getNumArgument(); ++i) {
                              T = PF.getArgument(i).type();
                              if (T.involvesTypeParameters()) {
                                 TypeDecl S;
                                 if (!T.isWildcard()) {
                                    S = PA.getArgument(i).type();
                                    this.constraintEqual(S, T);
                                 } else {
                                    TypeDecl U;
                                    TypeDecl V;
                                    if (T instanceof WildcardExtendsType) {
                                       U = ((WildcardExtendsType)T).getAccess().type();
                                       S = PA.getArgument(i).type();
                                       if (!S.isWildcard()) {
                                          this.convertibleTo(S, U);
                                       } else if (S instanceof WildcardExtendsType) {
                                          V = ((WildcardExtendsType)S).getAccess().type();
                                          this.convertibleTo(V, U);
                                       }
                                    } else if (T instanceof WildcardSuperType) {
                                       U = ((WildcardSuperType)T).getAccess().type();
                                       S = PA.getArgument(i).type();
                                       if (!S.isWildcard()) {
                                          this.convertibleFrom(S, U);
                                       } else if (S instanceof WildcardSuperType) {
                                          V = ((WildcardSuperType)S).getAccess().type();
                                          this.convertibleFrom(V, U);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public void convertibleFrom(TypeDecl A, TypeDecl F) {
      if (F.involvesTypeParameters()) {
         if (!A.isNull()) {
            if (F instanceof TypeVariable) {
               if (this.typeVariables.contains(F)) {
                  this.addSubtypeConstraint(F, A);
               }
            } else {
               TypeDecl H;
               if (F.isArrayDecl()) {
                  TypeDecl U = ((ArrayDecl)F).componentType();
                  if (A.isArrayDecl()) {
                     TypeDecl V = ((ArrayDecl)A).componentType();
                     this.convertibleFrom(V, U);
                  } else if (A.isTypeVariable()) {
                     TypeVariable t = (TypeVariable)A;

                     for(int i = 0; i < t.getNumTypeBound(); ++i) {
                        H = t.getTypeBound(i).type();
                        if (H.isArrayDecl() && ((ArrayDecl)H).componentType().isReferenceType()) {
                           TypeDecl V = ((ArrayDecl)H).componentType();
                           this.convertibleFrom(V, U);
                        }
                     }
                  }
               } else if (F instanceof ParTypeDecl && !F.isRawType() && A instanceof ParTypeDecl && !A.isRawType()) {
                  ParTypeDecl PF = (ParTypeDecl)F;
                  ParTypeDecl PA = (ParTypeDecl)A;
                  TypeDecl G = PF.genericDecl();
                  H = PA.genericDecl();

                  label168:
                  for(int i = 0; i < PF.getNumArgument(); ++i) {
                     TypeDecl T = PF.getArgument(i).type();
                     if (T.involvesTypeParameters()) {
                        Iterator iter;
                        TypeDecl V;
                        TypeDecl X;
                        if (!T.isWildcard()) {
                           if (G.instanceOf(H)) {
                              if (H != G) {
                                 iter = parameterizedSupertypes(F).iterator();

                                 while(iter.hasNext()) {
                                    V = (TypeDecl)iter.next();
                                    if (!V.isRawType() && ((ParTypeDecl)V).genericDecl() == H && F.instanceOf(V)) {
                                       this.convertibleFrom(A, V);
                                    }
                                 }
                              } else if (PF.getNumArgument() == PA.getNumArgument()) {
                                 X = PA.getArgument(i).type();
                                 if (!X.isWildcard()) {
                                    this.constraintEqual(X, T);
                                 } else if (X instanceof WildcardExtendsType) {
                                    V = ((WildcardExtendsType)X).getAccess().type();
                                    this.convertibleFrom(V, T);
                                 } else if (X instanceof WildcardSuperType) {
                                    V = ((WildcardSuperType)X).getAccess().type();
                                    this.convertibleTo(V, T);
                                 }
                              }
                           }
                        } else {
                           TypeDecl U;
                           ArrayList list;
                           int j;
                           if (T instanceof WildcardExtendsType) {
                              U = ((WildcardExtendsType)T).getAccess().type();
                              if (G.instanceOf(H)) {
                                 if (H == G) {
                                    if (PF.getNumArgument() == PA.getNumArgument()) {
                                       X = PA.getArgument(i).type();
                                       if (X instanceof WildcardExtendsType) {
                                          V = ((WildcardExtendsType)X).getAccess().type();
                                          this.convertibleFrom(V, U);
                                       }
                                    }
                                 } else {
                                    iter = parameterizedSupertypes(F).iterator();

                                    while(true) {
                                       do {
                                          do {
                                             if (!iter.hasNext()) {
                                                continue label168;
                                             }

                                             V = (TypeDecl)iter.next();
                                          } while(V.isRawType());
                                       } while(((ParTypeDecl)V).genericDecl() != H);

                                       list = new ArrayList();

                                       for(j = 0; j < ((ParTypeDecl)V).getNumArgument(); ++j) {
                                          list.add(((ParTypeDecl)V).getArgument(j).type().asWildcardExtends());
                                       }

                                       V = ((GenericTypeDecl)H).lookupParTypeDecl(list);
                                       this.convertibleFrom(A, V);
                                    }
                                 }
                              }
                           } else if (T instanceof WildcardSuperType) {
                              U = ((WildcardSuperType)T).getAccess().type();
                              if (G.instanceOf(H)) {
                                 if (H == G) {
                                    if (PF.getNumArgument() == PA.getNumArgument()) {
                                       X = PA.getArgument(i).type();
                                       if (X instanceof WildcardSuperType) {
                                          V = ((WildcardSuperType)X).getAccess().type();
                                          this.convertibleTo(V, U);
                                       }
                                    }
                                 } else {
                                    iter = parameterizedSupertypes(F).iterator();

                                    while(true) {
                                       do {
                                          do {
                                             if (!iter.hasNext()) {
                                                continue label168;
                                             }

                                             V = (TypeDecl)iter.next();
                                          } while(V.isRawType());
                                       } while(((ParTypeDecl)V).genericDecl() != H);

                                       list = new ArrayList();

                                       for(j = 0; j < ((ParTypeDecl)V).getNumArgument(); ++j) {
                                          list.add(((ParTypeDecl)V).getArgument(j).type().asWildcardExtends());
                                       }

                                       V = ((GenericTypeDecl)H).lookupParTypeDecl(list);
                                       this.convertibleFrom(A, V);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               } else if (F.isRawType()) {
                  this.rawAccess = true;
               }
            }

         }
      }
   }

   public void constraintEqual(TypeDecl A, TypeDecl F) {
      if (F.involvesTypeParameters()) {
         if (!A.isNull()) {
            if (F instanceof TypeVariable) {
               if (this.typeVariables.contains(F)) {
                  this.addEqualConstraint(F, A);
               }
            } else {
               int i;
               TypeDecl T;
               TypeDecl U;
               if (F.isArrayDecl()) {
                  TypeDecl U = ((ArrayDecl)F).componentType();
                  if (A.isArrayDecl()) {
                     TypeDecl V = ((ArrayDecl)A).componentType();
                     this.constraintEqual(V, U);
                  } else if (A.isTypeVariable()) {
                     TypeVariable t = (TypeVariable)A;

                     for(i = 0; i < t.getNumTypeBound(); ++i) {
                        T = t.getTypeBound(i).type();
                        if (T.isArrayDecl() && ((ArrayDecl)T).componentType().isReferenceType()) {
                           U = ((ArrayDecl)T).componentType();
                           this.constraintEqual(U, U);
                        }
                     }
                  }
               } else if (F instanceof ParTypeDecl && !F.isRawType() && A instanceof ParTypeDecl) {
                  ParTypeDecl PF = (ParTypeDecl)F;
                  ParTypeDecl PA = (ParTypeDecl)A;
                  if (PF.genericDecl() == PA.genericDecl()) {
                     if (A.isRawType()) {
                        this.rawAccess = true;
                     } else {
                        for(i = 0; i < PF.getNumArgument(); ++i) {
                           T = PF.getArgument(i).type();
                           if (T.involvesTypeParameters()) {
                              TypeDecl S;
                              if (!T.isWildcard()) {
                                 S = PA.getArgument(i).type();
                                 this.constraintEqual(S, T);
                              } else {
                                 TypeDecl V;
                                 if (T instanceof WildcardExtendsType) {
                                    U = ((WildcardExtendsType)T).getAccess().type();
                                    S = PA.getArgument(i).type();
                                    if (S instanceof WildcardExtendsType) {
                                       V = ((WildcardExtendsType)S).getAccess().type();
                                       this.constraintEqual(V, U);
                                    }
                                 } else if (T instanceof WildcardSuperType) {
                                    U = ((WildcardSuperType)T).getAccess().type();
                                    S = PA.getArgument(i).type();
                                    if (S instanceof WildcardSuperType) {
                                       V = ((WildcardSuperType)S).getAccess().type();
                                       this.constraintEqual(V, U);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   static class ConstraintSet {
      public Collection supertypeConstraints = new HashSet(4);
      public Collection subtypeConstraints = new HashSet(4);
      public Collection equaltypeConstraints = new HashSet(4);
      public TypeDecl typeArgument;
   }
}

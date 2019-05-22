package soot;

import java.util.ArrayDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;

public class AbstractSootFieldRef implements SootFieldRef {
   private static final Logger logger = LoggerFactory.getLogger(AbstractSootFieldRef.class);
   private final SootClass declaringClass;
   private final String name;
   private final Type type;
   private final boolean isStatic;

   public AbstractSootFieldRef(SootClass declaringClass, String name, Type type, boolean isStatic) {
      this.declaringClass = declaringClass;
      this.name = name;
      this.type = type;
      this.isStatic = isStatic;
      if (declaringClass == null) {
         throw new RuntimeException("Attempt to create SootFieldRef with null class");
      } else if (name == null) {
         throw new RuntimeException("Attempt to create SootFieldRef with null name");
      } else if (type == null) {
         throw new RuntimeException("Attempt to create SootFieldRef with null type");
      }
   }

   public SootClass declaringClass() {
      return this.declaringClass;
   }

   public String name() {
      return this.name;
   }

   public Type type() {
      return this.type;
   }

   public boolean isStatic() {
      return this.isStatic;
   }

   public String getSignature() {
      return SootField.getSignature(this.declaringClass, this.name, this.type);
   }

   public SootField resolve() {
      return this.resolve((StringBuffer)null);
   }

   private SootField checkStatic(SootField ret) {
      if ((Options.v().wrong_staticness() == 1 || Options.v().wrong_staticness() == 4) && ret.isStatic() != this.isStatic() && !ret.isPhantom()) {
         throw new ResolutionFailedException("Resolved " + this + " to " + ret + " which has wrong static-ness");
      } else {
         return ret;
      }
   }

   private SootField resolve(StringBuffer trace) {
      SootClass cl = this.declaringClass;

      while(true) {
         if (trace != null) {
            trace.append("Looking in " + cl + " which has fields " + cl.getFields() + "\n");
         }

         SootField sf = cl.getFieldUnsafe(this.name, this.type);
         if (sf != null) {
            return this.checkStatic(sf);
         }

         SootField ifaceField;
         SootField clField;
         if (Scene.v().allowsPhantomRefs() && cl.isPhantom()) {
            synchronized(cl) {
               sf = cl.getFieldUnsafe(this.name, this.type);
               if (sf != null) {
                  return this.checkStatic(sf);
               }

               clField = cl.getFieldByNameUnsafe(this.name);
               if (clField != null) {
                  return this.handleFieldTypeMismatch(sf);
               }

               ifaceField = Scene.v().makeSootField(this.name, this.type, this.isStatic() ? 8 : 0);
               ifaceField.setPhantom(true);
               cl.addField(ifaceField);
               return ifaceField;
            }
         }

         ArrayDeque<SootClass> queue = new ArrayDeque();
         queue.addAll(cl.getInterfaces());

         while(true) {
            SootClass iface = (SootClass)queue.poll();
            if (iface == null) {
               if (!cl.hasSuperclass()) {
                  if (Options.v().allow_phantom_refs()) {
                     sf = Scene.v().makeSootField(this.name, this.type, this.isStatic ? 8 : 0);
                     sf.setPhantom(true);
                     synchronized(this.declaringClass) {
                        clField = this.declaringClass.getFieldByNameUnsafe(this.name);
                        if (clField != null) {
                           if (clField.getType().equals(this.type)) {
                              return this.checkStatic(clField);
                           }

                           return this.handleFieldTypeMismatch(clField);
                        }

                        this.declaringClass.addField(sf);
                        return sf;
                     }
                  }

                  if (trace == null) {
                     AbstractSootFieldRef.FieldResolutionFailedException e = new AbstractSootFieldRef.FieldResolutionFailedException();
                     if (!Options.v().ignore_resolution_errors()) {
                        throw e;
                     }

                     logger.debug("" + e.getMessage());
                  }

                  return null;
               }

               cl = cl.getSuperclass();
               break;
            }

            if (trace != null) {
               trace.append("Looking in " + iface + " which has fields " + iface.getFields() + "\n");
            }

            ifaceField = iface.getFieldUnsafe(this.name, this.type);
            if (ifaceField != null) {
               return this.checkStatic(ifaceField);
            }

            queue.addAll(iface.getInterfaces());
         }
      }
   }

   protected SootField handleFieldTypeMismatch(SootField clField) {
      switch(Options.v().field_type_mismatches()) {
      case 1:
         throw new ConflictingFieldRefException(clField, this.type);
      case 2:
         return this.checkStatic(clField);
      case 3:
         return null;
      default:
         throw new RuntimeException(String.format("Unsupported option for handling field type mismatches: %d", Options.v().field_type_mismatches()));
      }
   }

   public String toString() {
      return this.getSignature();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.declaringClass == null ? 0 : this.declaringClass.hashCode());
      result = 31 * result + (this.isStatic ? 1231 : 1237);
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         AbstractSootFieldRef other = (AbstractSootFieldRef)obj;
         if (this.declaringClass == null) {
            if (other.declaringClass != null) {
               return false;
            }
         } else if (!this.declaringClass.equals(other.declaringClass)) {
            return false;
         }

         if (this.isStatic != other.isStatic) {
            return false;
         } else {
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            if (this.type == null) {
               if (other.type != null) {
                  return false;
               }
            } else if (!this.type.equals(other.type)) {
               return false;
            }

            return true;
         }
      }
   }

   public class FieldResolutionFailedException extends ResolutionFailedException {
      private static final long serialVersionUID = -4657113720516199499L;

      public FieldResolutionFailedException() {
         super("Class " + AbstractSootFieldRef.this.declaringClass + " doesn't have field " + AbstractSootFieldRef.this.name + " : " + AbstractSootFieldRef.this.type + "; failed to resolve in superclasses and interfaces");
      }

      public String toString() {
         StringBuffer ret = new StringBuffer();
         ret.append(super.toString());
         AbstractSootFieldRef.this.resolve(ret);
         return ret.toString();
      }
   }
}

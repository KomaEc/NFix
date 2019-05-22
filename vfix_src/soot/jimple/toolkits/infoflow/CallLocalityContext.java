package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.List;
import soot.EquivalentValue;
import soot.RefLikeType;
import soot.jimple.InstanceFieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.ThisRef;

public class CallLocalityContext {
   List<EquivalentValue> nodes = new ArrayList();
   List<Boolean> isNodeLocal;

   public CallLocalityContext(List<EquivalentValue> nodes) {
      this.nodes.addAll(nodes);
      this.isNodeLocal = new ArrayList(nodes.size());

      for(int i = 0; i < nodes.size(); ++i) {
         this.isNodeLocal.add(i, Boolean.FALSE);
      }

   }

   public void setFieldLocal(EquivalentValue fieldRef) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         if (fieldRef.equals(this.nodes.get(i))) {
            this.isNodeLocal.remove(i);
            this.isNodeLocal.add(i, Boolean.TRUE);
            return;
         }
      }

      this.nodes.add(fieldRef);
      this.isNodeLocal.add(Boolean.TRUE);
   }

   public void setFieldShared(EquivalentValue fieldRef) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         if (fieldRef.equals(this.nodes.get(i))) {
            this.isNodeLocal.remove(i);
            this.isNodeLocal.add(i, Boolean.FALSE);
            return;
         }
      }

      this.nodes.add(fieldRef);
      this.isNodeLocal.add(Boolean.FALSE);
   }

   public void setAllFieldsLocal() {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof InstanceFieldRef) {
            this.isNodeLocal.remove(i);
            this.isNodeLocal.add(i, Boolean.TRUE);
         }
      }

   }

   public void setAllFieldsShared() {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof InstanceFieldRef) {
            this.isNodeLocal.remove(i);
            this.isNodeLocal.add(i, Boolean.FALSE);
         }
      }

   }

   public void setParamLocal(int index) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof ParameterRef) {
            ParameterRef pr = (ParameterRef)r;
            if (pr.getIndex() == index) {
               this.isNodeLocal.remove(i);
               this.isNodeLocal.add(i, Boolean.TRUE);
            }
         }
      }

   }

   public void setParamShared(int index) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof ParameterRef) {
            ParameterRef pr = (ParameterRef)r;
            if (pr.getIndex() == index) {
               this.isNodeLocal.remove(i);
               this.isNodeLocal.add(i, Boolean.FALSE);
            }
         }
      }

   }

   public void setAllParamsLocal() {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof ParameterRef) {
            ParameterRef pr = (ParameterRef)r;
            if (pr.getIndex() != -1) {
               this.isNodeLocal.remove(i);
               this.isNodeLocal.add(i, Boolean.TRUE);
            }
         }
      }

   }

   public void setAllParamsShared() {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof ParameterRef) {
            ParameterRef pr = (ParameterRef)r;
            if (pr.getIndex() != -1) {
               this.isNodeLocal.remove(i);
               this.isNodeLocal.add(i, Boolean.FALSE);
            }
         }
      }

   }

   public void setThisLocal() {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof ThisRef) {
            this.isNodeLocal.remove(i);
            this.isNodeLocal.add(i, Boolean.TRUE);
         }
      }

   }

   public void setThisShared() {
      for(int i = 0; i < this.nodes.size(); ++i) {
         Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
         if (r instanceof ThisRef) {
            this.isNodeLocal.remove(i);
            this.isNodeLocal.add(i, Boolean.FALSE);
         }
      }

   }

   public void setReturnLocal() {
      this.setParamLocal(-1);
   }

   public void setReturnShared() {
      this.setParamShared(-1);
   }

   public List<Object> getLocalRefs() {
      List<Object> ret = new ArrayList();

      for(int i = 0; i < this.nodes.size(); ++i) {
         if ((Boolean)this.isNodeLocal.get(i)) {
            ret.add(this.nodes.get(i));
         }
      }

      return ret;
   }

   public List<Object> getSharedRefs() {
      List<Object> ret = new ArrayList();

      for(int i = 0; i < this.nodes.size(); ++i) {
         if (!(Boolean)this.isNodeLocal.get(i)) {
            ret.add(this.nodes.get(i));
         }
      }

      return ret;
   }

   public boolean isFieldLocal(EquivalentValue fieldRef) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         if (fieldRef.equals(this.nodes.get(i))) {
            return (Boolean)this.isNodeLocal.get(i);
         }
      }

      return false;
   }

   public boolean containsField(EquivalentValue fieldRef) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         if (fieldRef.equals(this.nodes.get(i))) {
            return true;
         }
      }

      return false;
   }

   public boolean merge(CallLocalityContext other) {
      boolean isChanged = false;
      int i;
      if (other.nodes.size() > this.nodes.size()) {
         isChanged = true;

         for(i = this.nodes.size(); i < other.nodes.size(); ++i) {
            this.nodes.add(other.nodes.get(i));
            this.isNodeLocal.add(other.isNodeLocal.get(i));
         }
      }

      for(i = 0; i < other.nodes.size(); ++i) {
         Boolean temp = new Boolean((Boolean)this.isNodeLocal.get(i) && (Boolean)other.isNodeLocal.get(i));
         if (!temp.equals(this.isNodeLocal.get(i))) {
            isChanged = true;
         }

         this.isNodeLocal.remove(i);
         this.isNodeLocal.add(i, temp);
      }

      return isChanged;
   }

   public boolean equals(Object o) {
      if (o instanceof CallLocalityContext) {
         CallLocalityContext other = (CallLocalityContext)o;
         return this.isNodeLocal.equals(other.isNodeLocal);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.isNodeLocal.hashCode();
   }

   public boolean isAllShared(boolean refsOnly) {
      for(int i = 0; i < this.nodes.size(); ++i) {
         if (!refsOnly && (Boolean)this.isNodeLocal.get(i)) {
            return false;
         }

         if (((EquivalentValue)this.nodes.get(i)).getValue().getType() instanceof RefLikeType && (Boolean)this.isNodeLocal.get(i)) {
            return false;
         }
      }

      return true;
   }

   public String toString() {
      String fieldrefs = "";
      String staticrefs = "";
      String paramrefs = "";
      String thisref = "";
      if (this.nodes.size() == 0) {
         return "Call Locality Context: NO NODES\n";
      } else {
         for(int i = 0; i < this.nodes.size(); ++i) {
            Ref r = (Ref)((EquivalentValue)this.nodes.get(i)).getValue();
            if (r instanceof InstanceFieldRef) {
               fieldrefs = fieldrefs + r + ": " + ((Boolean)this.isNodeLocal.get(i) ? "local" : "shared") + "\n";
            } else if (r instanceof StaticFieldRef) {
               staticrefs = staticrefs + r + ": " + ((Boolean)this.isNodeLocal.get(i) ? "local" : "shared") + "\n";
            } else if (r instanceof ParameterRef) {
               paramrefs = paramrefs + r + ": " + ((Boolean)this.isNodeLocal.get(i) ? "local" : "shared") + "\n";
            } else {
               if (!(r instanceof ThisRef)) {
                  return "Call Locality Context: HAS STRANGE NODE " + r + "\n";
               }

               thisref = thisref + r + ": " + ((Boolean)this.isNodeLocal.get(i) ? "local" : "shared") + "\n";
            }
         }

         return "Call Locality Context: \n" + fieldrefs + paramrefs + thisref + staticrefs;
      }
   }

   public String toShortString() {
      String ret = "[";

      for(int i = 0; i < this.nodes.size(); ++i) {
         ret = ret + ((Boolean)this.isNodeLocal.get(i) ? "L" : "S");
      }

      return ret + "]";
   }
}

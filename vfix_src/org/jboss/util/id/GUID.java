package org.jboss.util.id;

public class GUID implements ID, Comparable {
   static final long serialVersionUID = 3289509836244263718L;
   protected final VMID vmid;
   protected final UID uid;
   protected final int hashCode;
   private transient String toString;

   public GUID() {
      this.vmid = VMID.getInstance();
      this.uid = new UID();
      int code = this.vmid.hashCode();
      code ^= this.uid.hashCode();
      this.hashCode = code;
   }

   protected GUID(GUID guid) {
      this.vmid = guid.vmid;
      this.uid = guid.uid;
      this.hashCode = guid.hashCode;
   }

   public final VMID getVMID() {
      return this.vmid;
   }

   public final UID getUID() {
      return this.uid;
   }

   public String toString() {
      if (this.toString == null) {
         this.toString = this.vmid.toString() + "-" + this.uid.toString();
      }

      return this.toString;
   }

   public int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         GUID guid = (GUID)obj;
         return guid.vmid.equals(this.vmid) && guid.uid.equals(this.uid);
      } else {
         return false;
      }
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public static String asString() {
      return (new GUID()).toString();
   }

   public int compareTo(Object o) {
      GUID guid = (GUID)o;
      return this.toString().compareTo(guid.toString());
   }
}

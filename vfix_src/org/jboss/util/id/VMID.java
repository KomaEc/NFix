package org.jboss.util.id;

import java.net.InetAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import org.jboss.util.HashCode;
import org.jboss.util.platform.PID;

public class VMID implements ID {
   private static final long serialVersionUID = -4339675822939194520L;
   protected final byte[] address;
   protected final PID pid;
   protected final UID uid;
   protected final int hashCode;
   private static VMID instance = null;
   public static final byte[] UNKNOWN_HOST = new byte[]{0, 0, 0, 0};

   protected VMID(byte[] address, PID pid, UID uid) {
      this.address = address;
      this.pid = pid;
      this.uid = uid;
      int code = pid.hashCode();
      code ^= uid.hashCode();
      code ^= HashCode.generate(address);
      this.hashCode = code;
   }

   protected VMID(VMID vmid) {
      this.address = vmid.address;
      this.pid = vmid.pid;
      this.uid = vmid.uid;
      this.hashCode = vmid.hashCode;
   }

   public final byte[] getAddress() {
      return this.address;
   }

   public final PID getProcessID() {
      return this.pid;
   }

   public final UID getUID() {
      return this.uid;
   }

   public String toString() {
      StringBuffer buff = new StringBuffer();

      for(int i = 0; i < this.address.length; ++i) {
         int n = this.address[i] & 255;
         buff.append(Integer.toString(n, 36));
      }

      buff.append("-").append(this.pid.toString(36));
      buff.append("-").append(this.uid);
      return buff.toString();
   }

   public final int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         VMID vmid = (VMID)obj;
         return Arrays.equals(vmid.address, this.address) && vmid.pid.equals(this.pid) && vmid.uid.equals(this.uid);
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
      return getInstance().toString();
   }

   public static synchronized VMID getInstance() {
      if (instance == null) {
         instance = create();
      }

      return instance;
   }

   private static byte[] getHostAddress() {
      return (byte[])((byte[])AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            try {
               return InetAddress.getLocalHost().getAddress();
            } catch (Exception var2) {
               return VMID.UNKNOWN_HOST;
            }
         }
      }));
   }

   private static VMID create() {
      byte[] address = getHostAddress();
      return new VMID(address, PID.getInstance(), new UID());
   }
}

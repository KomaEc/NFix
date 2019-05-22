package org.apache.tools.ant.types;

import java.net.SocketPermission;
import java.security.UnresolvedPermission;
import java.security.cert.Certificate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PropertyPermission;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitException;

public class Permissions {
   private List grantedPermissions;
   private List revokedPermissions;
   private java.security.Permissions granted;
   private SecurityManager origSm;
   private boolean active;
   private boolean delegateToOldSM;

   public Permissions() {
      this(false);
   }

   public Permissions(boolean delegateToOldSM) {
      this.grantedPermissions = new LinkedList();
      this.revokedPermissions = new LinkedList();
      this.granted = null;
      this.origSm = null;
      this.active = false;
      this.delegateToOldSM = delegateToOldSM;
   }

   public void addConfiguredGrant(Permissions.Permission perm) {
      this.grantedPermissions.add(perm);
   }

   public void addConfiguredRevoke(Permissions.Permission perm) {
      this.revokedPermissions.add(perm);
   }

   public synchronized void setSecurityManager() throws BuildException {
      this.origSm = System.getSecurityManager();
      this.init();
      System.setSecurityManager(new Permissions.MySM());
      this.active = true;
   }

   private void init() throws BuildException {
      this.granted = new java.security.Permissions();
      ListIterator i = this.revokedPermissions.listIterator();

      Permissions.Permission p;
      do {
         if (!i.hasNext()) {
            i = this.grantedPermissions.listIterator();

            while(i.hasNext()) {
               p = (Permissions.Permission)i.next();
               if (p.getClassName() == null) {
                  throw new BuildException("Granted permission " + p + " does not contain a class.");
               }

               java.security.Permission perm = new UnresolvedPermission(p.getClassName(), p.getName(), p.getActions(), (Certificate[])null);
               this.granted.add(perm);
            }

            this.granted.add(new SocketPermission("localhost:1024-", "listen"));
            this.granted.add(new PropertyPermission("java.version", "read"));
            this.granted.add(new PropertyPermission("java.vendor", "read"));
            this.granted.add(new PropertyPermission("java.vendor.url", "read"));
            this.granted.add(new PropertyPermission("java.class.version", "read"));
            this.granted.add(new PropertyPermission("os.name", "read"));
            this.granted.add(new PropertyPermission("os.version", "read"));
            this.granted.add(new PropertyPermission("os.arch", "read"));
            this.granted.add(new PropertyPermission("file.encoding", "read"));
            this.granted.add(new PropertyPermission("file.separator", "read"));
            this.granted.add(new PropertyPermission("path.separator", "read"));
            this.granted.add(new PropertyPermission("line.separator", "read"));
            this.granted.add(new PropertyPermission("java.specification.version", "read"));
            this.granted.add(new PropertyPermission("java.specification.vendor", "read"));
            this.granted.add(new PropertyPermission("java.specification.name", "read"));
            this.granted.add(new PropertyPermission("java.vm.specification.version", "read"));
            this.granted.add(new PropertyPermission("java.vm.specification.vendor", "read"));
            this.granted.add(new PropertyPermission("java.vm.specification.name", "read"));
            this.granted.add(new PropertyPermission("java.vm.version", "read"));
            this.granted.add(new PropertyPermission("java.vm.vendor", "read"));
            this.granted.add(new PropertyPermission("java.vm.name", "read"));
            return;
         }

         p = (Permissions.Permission)i.next();
      } while(p.getClassName() != null);

      throw new BuildException("Revoked permission " + p + " does not contain a class.");
   }

   public synchronized void restoreSecurityManager() {
      this.active = false;
      System.setSecurityManager(this.origSm);
   }

   public static class Permission {
      private String className;
      private String name;
      private String actionString;
      private Set actions;

      public void setClass(String aClass) {
         this.className = aClass.trim();
      }

      public String getClassName() {
         return this.className;
      }

      public void setName(String aName) {
         this.name = aName.trim();
      }

      public String getName() {
         return this.name;
      }

      public void setActions(String actions) {
         this.actionString = actions;
         if (actions.length() > 0) {
            this.actions = this.parseActions(actions);
         }

      }

      public String getActions() {
         return this.actionString;
      }

      boolean matches(java.security.Permission perm) {
         if (!this.className.equals(perm.getClass().getName())) {
            return false;
         } else {
            if (this.name != null) {
               if (this.name.endsWith("*")) {
                  if (!perm.getName().startsWith(this.name.substring(0, this.name.length() - 1))) {
                     return false;
                  }
               } else if (!this.name.equals(perm.getName())) {
                  return false;
               }
            }

            if (this.actions != null) {
               Set as = this.parseActions(perm.getActions());
               int size = as.size();
               as.removeAll(this.actions);
               if (as.size() == size) {
                  return false;
               }
            }

            return true;
         }
      }

      private Set parseActions(String actions) {
         Set result = new HashSet();
         StringTokenizer tk = new StringTokenizer(actions, ",");

         while(tk.hasMoreTokens()) {
            String item = tk.nextToken().trim();
            if (!item.equals("")) {
               result.add(item);
            }
         }

         return result;
      }

      public String toString() {
         return "Permission: " + this.className + " (\"" + this.name + "\", \"" + this.actions + "\")";
      }
   }

   private class MySM extends SecurityManager {
      private MySM() {
      }

      public void checkExit(int status) {
         RuntimePermission perm = new RuntimePermission("exitVM", (String)null);

         try {
            this.checkPermission(perm);
         } catch (SecurityException var4) {
            throw new ExitException(var4.getMessage(), status);
         }
      }

      public void checkPermission(java.security.Permission perm) {
         if (Permissions.this.active) {
            if (Permissions.this.delegateToOldSM && !perm.getName().equals("exitVM")) {
               boolean permOK = false;
               if (Permissions.this.granted.implies(perm)) {
                  permOK = true;
               }

               this.checkRevoked(perm);
               if (!permOK && Permissions.this.origSm != null) {
                  Permissions.this.origSm.checkPermission(perm);
               }
            } else {
               if (!Permissions.this.granted.implies(perm)) {
                  throw new SecurityException("Permission " + perm + " was not granted.");
               }

               this.checkRevoked(perm);
            }
         }

      }

      private void checkRevoked(java.security.Permission perm) {
         ListIterator i = Permissions.this.revokedPermissions.listIterator();

         do {
            if (!i.hasNext()) {
               return;
            }
         } while(!((Permissions.Permission)i.next()).matches(perm));

         throw new SecurityException("Permission " + perm + " was revoked.");
      }

      // $FF: synthetic method
      MySM(Object x1) {
         this();
      }
   }
}

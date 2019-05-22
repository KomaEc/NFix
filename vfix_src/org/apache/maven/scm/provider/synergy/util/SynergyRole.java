package org.apache.maven.scm.provider.synergy.util;

public class SynergyRole {
   public static final SynergyRole BUILD_MGR = new SynergyRole("build_mgr");
   public static final SynergyRole CCM_ADMIN = new SynergyRole("ccm_admin");
   private String value;

   private SynergyRole(String value) {
      this.value = value;
   }

   public String toString() {
      return this.value;
   }
}

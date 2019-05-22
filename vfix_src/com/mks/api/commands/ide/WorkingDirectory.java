package com.mks.api.commands.ide;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class WorkingDirectory {
   private File directory = null;
   private Set sandboxes = new HashSet();

   WorkingDirectory(File directory) {
      this.directory = directory;
   }

   public File getDirectory() {
      return this.directory;
   }

   public synchronized File getSandboxLocation() {
      ISandboxInfo example = null;
      if (!this.sandboxes.isEmpty()) {
         example = (ISandboxInfo)this.sandboxes.iterator().next();
      }

      return example == null ? null : example.getSandboxLocation();
   }

   public synchronized Collection getSandboxes() {
      return Collections.unmodifiableCollection(this.sandboxes);
   }

   public boolean isSandboxDirectory() {
      return this.directory.equals(this.getSandboxLocation());
   }

   public synchronized boolean isSandboxAware() {
      return !this.sandboxes.isEmpty();
   }

   public synchronized boolean isAmbiguous() {
      return this.sandboxes.size() > 1;
   }

   synchronized void addSandbox(ISandboxInfo sandbox) {
      File sandboxLocation = this.getSandboxLocation();
      if (sandboxLocation != null) {
         String location = sandbox.getSandboxLocation().getAbsolutePath();
         String current = sandboxLocation.getAbsolutePath();
         if (location.length() > current.length()) {
            this.sandboxes.clear();
         }
      }

      this.sandboxes.add(sandbox);
   }
}

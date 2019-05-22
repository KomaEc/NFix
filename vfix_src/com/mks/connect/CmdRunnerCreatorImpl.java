package com.mks.connect;

import com.mks.api.CmdRunner;
import com.mks.api.CmdRunnerCreator;
import com.mks.api.response.APIException;
import com.mks.api.response.CommandAlreadyRunningException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class CmdRunnerCreatorImpl implements CmdRunnerCreator {
   private Set cmdRunners = new HashSet(5);
   private String defaultHost;
   private int defaultPort;
   private String defaultUser;
   private String defaultPass;
   private String impUser;

   public String getDefaultHostname() {
      return this.defaultHost;
   }

   public void setDefaultHostname(String host) {
      this.defaultHost = host;
   }

   public int getDefaultPort() {
      return this.defaultPort;
   }

   public void setDefaultPort(int port) {
      this.defaultPort = port;
   }

   public String getDefaultUsername() {
      return this.defaultUser;
   }

   public void setDefaultUsername(String user) {
      this.defaultUser = user;
   }

   public String getDefaultPassword() {
      return this.defaultPass;
   }

   public void setDefaultPassword(String pass) {
      this.defaultPass = pass;
   }

   public void setDefaultImpersonationUser(String impUser) {
      this.impUser = impUser;
   }

   public String getDefaultImpersonationUser() {
      return this.impUser;
   }

   protected abstract CmdRunner _createCmdRunner() throws APIException;

   public final CmdRunner createCmdRunner() throws APIException {
      CmdRunner cmdRunner = this._createCmdRunner();
      synchronized(this.cmdRunners) {
         this.cmdRunners.add(cmdRunner);
      }

      if (this.defaultHost != null) {
         cmdRunner.setDefaultHostname(this.defaultHost);
      }

      if (this.defaultPort > 0) {
         cmdRunner.setDefaultPort(this.defaultPort);
      }

      if (this.defaultUser != null) {
         cmdRunner.setDefaultUsername(this.defaultUser);
      }

      if (this.defaultPass != null) {
         cmdRunner.setDefaultPassword(this.defaultPass);
      }

      if (this.impUser != null) {
         cmdRunner.setDefaultImpersonationUser(this.impUser);
      }

      return cmdRunner;
   }

   public final Iterator getCmdRunners() {
      Set s;
      synchronized(this.cmdRunners) {
         s = this.cmdRunners.isEmpty() ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(this.cmdRunners));
      }

      return s.iterator();
   }

   public final void release() throws IOException, APIException {
      this.release(false);
   }

   public void release(boolean force) throws IOException, APIException {
      synchronized(this.cmdRunners) {
         Iterator it = this.cmdRunners.iterator();

         while(it.hasNext()) {
            CmdRunner aci = (CmdRunner)it.next();
            if (!aci.isFinished()) {
               if (!force) {
                  throw new CommandAlreadyRunningException();
               }

               aci.interrupt();
            }
         }

         this.cmdRunners.clear();
      }
   }

   protected void removeCmdRunner(CmdRunner c) {
      synchronized(this.cmdRunners) {
         this.cmdRunners.remove(c);
      }
   }
}

package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.InterruptedException;
import com.mks.api.response.Result;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.response.modifiable.ModifiableAPIExceptionContainer;
import com.mks.api.response.modifiable.ModifiableResultContainer;
import com.mks.api.response.modifiable.ModifiableSubRoutineContainer;
import com.mks.api.response.modifiable.ModifiableWorkItemContainer;

public abstract class DelegatedResponseContainer implements ModifiableSubRoutineContainer, ModifiableWorkItemContainer, ModifiableAPIExceptionContainer, ModifiableResultContainer {
   protected ResponseContainer rc;

   protected DelegatedResponseContainer(ResponseContainer rc) {
      this.rc = rc;
   }

   public boolean getUseInterim() {
      return this.rc.getUseInterim();
   }

   public void setUseInterim(boolean flag) {
      this.rc.setUseInterim(flag);
   }

   public void setCacheContents(boolean flag) {
      this.rc.setCacheContents(flag);
   }

   public boolean getCacheContents() {
      return this.rc.getCacheContents();
   }

   public String getWorkItemSelectionType() {
      return this.rc.getWorkItemSelectionType();
   }

   public void setWorkItemSelectionType(String type) {
      this.rc.setWorkItemSelectionType(type);
   }

   public SubRoutineIterator getSubRoutines() {
      return this.rc.getSubRoutines();
   }

   public boolean containsSubRoutine(String name) {
      return this.rc.containsSubRoutine(name);
   }

   public SubRoutine getSubRoutine(String name) {
      return this.rc.getSubRoutine(name);
   }

   public int getSubRoutineListSize() {
      return this.rc.getSubRoutineListSize();
   }

   public void add(SubRoutine sr) {
      this.rc.add(sr);
   }

   public void setConnectionHostname(String hostname) {
      this.rc.setConnectionHostname(hostname);
   }

   public String getConnectionHostname() {
      return this.rc.getConnectionHostname();
   }

   public void setConnectionPort(int port) {
      this.rc.setConnectionPort(port);
   }

   public int getConnectionPort() {
      return this.rc.getConnectionPort();
   }

   public void setConnectionUsername(String username) {
      this.rc.setConnectionUsername(username);
   }

   public String getConnectionUsername() {
      return this.rc.getConnectionUsername();
   }

   public WorkItem getWorkItem(String id) {
      return this.rc.getWorkItem(id);
   }

   public WorkItem getWorkItem(String id, String context) {
      return this.rc.getWorkItem(id, context);
   }

   public boolean containsWorkItem(String id) {
      return this.rc.containsWorkItem(id);
   }

   public boolean containsWorkItem(String id, String context) {
      return this.rc.containsWorkItem(id, context);
   }

   public int getWorkItemListSize() {
      return this.rc.getWorkItemListSize();
   }

   public WorkItemIterator getWorkItems() {
      return this.rc.getWorkItems();
   }

   public void add(WorkItem wi) {
      this.rc.add(wi);
   }

   public void setAPIException(APIException ae) {
      this.rc.setAPIException(ae);
   }

   public APIException getAPIException() throws InterruptedException {
      return this.rc.getAPIException();
   }

   public void setResult(Result res) {
      this.rc.setResult(res);
   }

   public int getExitCode() throws InterruptedException {
      return this.rc.getExitCode();
   }

   public void setExitCode(int exitCode) {
      this.rc.setExitCode(exitCode);
   }
}

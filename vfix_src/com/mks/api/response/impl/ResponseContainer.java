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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class ResponseContainer implements ModifiableSubRoutineContainer, ModifiableWorkItemContainer, ModifiableAPIExceptionContainer, ModifiableResultContainer {
   protected Result result;
   protected APIException apiException;
   protected int exitCode = Integer.MIN_VALUE;
   protected List workItemList = new ArrayList();
   protected List subRoutineList = new ArrayList();
   protected String wiSelectionType;
   protected boolean interimResults;
   protected boolean cacheResults;
   private String hostname;
   private int port = -1;
   private String username;

   protected ResponseContainer() {
   }

   public void setWorkItemSelectionType(String workItemSelectionType) {
      this.wiSelectionType = workItemSelectionType;
   }

   public String getWorkItemSelectionType() {
      return this.wiSelectionType;
   }

   public boolean getUseInterim() {
      return this.interimResults;
   }

   public void setUseInterim(boolean flag) {
      this.interimResults = flag;
   }

   public boolean getCacheContents() {
      return this.cacheResults;
   }

   public void setCacheContents(boolean flag) {
      this.cacheResults = flag;
   }

   public void add(WorkItem wi) {
      this.workItemList.add(wi);
      if (this.wiSelectionType == null) {
         this.setWorkItemSelectionType(wi.getModelType());
      }

   }

   public WorkItem getWorkItem(String id) {
      Iterator it = this.workItemList.iterator();

      WorkItem wi;
      do {
         if (!it.hasNext()) {
            throw new NoSuchElementException(id);
         }

         wi = (WorkItem)it.next();
      } while(!wi.getId().equals(id));

      return wi;
   }

   public WorkItem getWorkItem(String id, String context) {
      Iterator it = this.workItemList.iterator();

      WorkItem wi;
      do {
         if (!it.hasNext()) {
            throw new NoSuchElementException(id);
         }

         wi = (WorkItem)it.next();
      } while(!wi.getId().equals(id) || !wi.getContext().equals(context));

      return wi;
   }

   public boolean containsWorkItem(String id) {
      try {
         return this.getWorkItem(id) != null;
      } catch (NoSuchElementException var3) {
         return false;
      }
   }

   public boolean containsWorkItem(String id, String context) {
      try {
         return this.getWorkItem(id, context) != null;
      } catch (NoSuchElementException var4) {
         return false;
      }
   }

   public int getWorkItemListSize() {
      return this.workItemList.size();
   }

   public WorkItemIterator getWorkItems() {
      return new WorkItemIteratorImpl(this.workItemList.iterator());
   }

   public void setAPIException(APIException ae) {
      this.apiException = ae;
   }

   public APIException getAPIException() throws InterruptedException {
      return this.apiException;
   }

   public void add(SubRoutine sr) {
      this.subRoutineList.add(sr);
   }

   public boolean containsSubRoutine(String name) {
      try {
         return this.getSubRoutine(name) != null;
      } catch (NoSuchElementException var3) {
         return false;
      }
   }

   public SubRoutine getSubRoutine(String name) {
      Iterator it = this.subRoutineList.iterator();

      SubRoutine sr;
      do {
         if (!it.hasNext()) {
            throw new NoSuchElementException(name);
         }

         sr = (SubRoutine)it.next();
      } while(!name.equals(sr.getRoutine()));

      return sr;
   }

   public int getSubRoutineListSize() {
      return this.subRoutineList.size();
   }

   public SubRoutineIterator getSubRoutines() {
      return new SubRoutineIteratorImpl(this.subRoutineList.iterator());
   }

   public Result getResult() throws InterruptedException {
      return this.result;
   }

   public void setResult(Result res) {
      this.result = res;
   }

   public int getExitCode() throws InterruptedException {
      return this.exitCode;
   }

   public void setExitCode(int exitCode) {
      this.exitCode = exitCode;
   }

   public void setConnectionHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getConnectionHostname() {
      return this.hostname;
   }

   public void setConnectionPort(int port) {
      this.port = port;
   }

   public int getConnectionPort() {
      return this.port;
   }

   public void setConnectionUsername(String username) {
      this.username = username;
   }

   public String getConnectionUsername() {
      return this.username;
   }
}

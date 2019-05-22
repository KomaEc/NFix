package com.mks.api.response.impl;

import com.mks.api.IntegrationPointFactory;
import com.mks.api.response.APIError;
import com.mks.api.response.APIException;
import com.mks.api.response.APIExceptionFactory;
import com.mks.api.response.APIInternalError;
import com.mks.api.response.InterruptedException;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.text.MessageFormat;
import java.util.List;

class XMLResponseContainer extends ResponseContainer implements ModifiableXMLResponseContainer {
   private boolean haveResult;
   private boolean haveException;
   private boolean haveExitCode;
   private boolean subRoutinesPrimed;
   private boolean workItemsPrimed;
   private String wiSelectionType;
   private XMLResponseHandler xrh;
   private static final String INVALID_STATE_MSG = "Unable to retrieve {0} data at this point.";
   private static final String BAD_EXIT_CODE_ERROR_MSG = "Exit code not properly read from the stream.";

   public void setXMLResponseHandler(XMLResponseHandler xrh) {
      this.xrh = xrh;
   }

   public String getWorkItemSelectionType() {
      return this.wiSelectionType;
   }

   public boolean containsSubRoutine(String name) {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeSubRoutineList();
      }

      return super.containsSubRoutine(name);
   }

   public int getSubRoutineListSize() {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeSubRoutineList();
      }

      return super.getSubRoutineListSize();
   }

   public SubRoutine getSubRoutine(String name) {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeSubRoutineList();
      }

      return super.getSubRoutine(name);
   }

   public SubRoutineIterator getSubRoutines() {
      if (this.getUseInterim()) {
         if (this.getCacheContents()) {
            return new InterimSubRoutineIterator(this.xrh, this.subRoutineList);
         } else {
            if (this.xrh.getReadPreSubRoutines() && (!this.xrh.getReadWorkItems() || this.xrh.getReadPostSubRoutines())) {
               String msg = MessageFormat.format("Unable to retrieve {0} data at this point.", "SubRoutine");
               APIExceptionFactory.createAPIException("APIError", msg);
            }

            return new InterimSubRoutineIterator(this.xrh, (List)null);
         }
      } else {
         return super.getSubRoutines();
      }
   }

   public String getConnectionHostname() {
      if (this.getUseInterim()) {
         this.primeConnectionAttributes();
      }

      return super.getConnectionHostname();
   }

   public int getConnectionPort() {
      if (this.getUseInterim()) {
         this.primeConnectionAttributes();
      }

      return super.getConnectionPort();
   }

   public String getConnectionUsername() {
      if (this.getUseInterim()) {
         this.primeConnectionAttributes();
      }

      return super.getConnectionUsername();
   }

   public WorkItemIterator getWorkItems() {
      if (this.getUseInterim()) {
         this.primeConnectionAttributes();
         if (this.getCacheContents()) {
            return new InterimWorkItemIterator(this.xrh, this.workItemList);
         } else {
            if (this.xrh.getReadWorkItems()) {
               String msg = MessageFormat.format("Unable to retrieve {0} data at this point.", "WorkItem");
               APIExceptionFactory.createAPIException("APIError", msg);
            }

            return new InterimWorkItemIterator(this.xrh, (List)null);
         }
      } else {
         return super.getWorkItems();
      }
   }

   public int getWorkItemListSize() {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeWorkItemList();
      }

      return super.getWorkItemListSize();
   }

   public WorkItem getWorkItem(String id, String context) {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeWorkItemList();
      }

      return super.getWorkItem(id, context);
   }

   public WorkItem getWorkItem(String id) {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeWorkItemList();
      }

      return super.getWorkItem(id);
   }

   public boolean containsWorkItem(String id) {
      if (this.getUseInterim()) {
         if (!this.getCacheContents()) {
            throw new UnsupportedOperationException();
         }

         this.primeWorkItemList();
      }

      try {
         return super.containsWorkItem(id);
      } catch (APIInternalError var3) {
         IntegrationPointFactory.getLogger().exception((Object)this, "API", 0, var3);
         return false;
      }
   }

   public boolean containsWorkItem(String id, String context) {
      if (this.getUseInterim()) {
         if (this.getCacheContents()) {
            this.primeWorkItemList();
         }

         throw new UnsupportedOperationException();
      } else {
         try {
            return super.containsWorkItem(id, context);
         } catch (APIInternalError var4) {
            IntegrationPointFactory.getLogger().exception((Object)this, "API", 0, var4);
            return false;
         }
      }
   }

   public APIException getAPIException() throws InterruptedException {
      if (this.getUseInterim() && !this.haveException) {
         if (this.xrh.isInterrupted()) {
            APIException ex = APIExceptionFactory.createAPIException("InterruptedException", (Response)null);
            throw (InterruptedException)ex;
         }

         this.primeLists();
         this.getResult();
         this.setAPIException(this.xrh.getException());
         this.haveException = true;
      }

      return super.getAPIException();
   }

   public Result getResult() throws InterruptedException {
      if (this.getUseInterim() && !this.haveResult) {
         if (this.xrh.isInterrupted()) {
            APIException ex = APIExceptionFactory.createAPIException("InterruptedException", (Response)null);
            throw (InterruptedException)ex;
         }

         this.primeLists();
         this.setResult(this.xrh.getResult(true));
         this.haveResult = true;
      }

      return super.getResult();
   }

   public int getExitCode() throws InterruptedException {
      if (this.getUseInterim() && !this.haveExitCode) {
         if (this.xrh.isInterrupted()) {
            APIException ex = APIExceptionFactory.createAPIException("InterruptedException", (Response)null);
            throw (InterruptedException)ex;
         }

         this.primeLists();
         this.getResult();
         this.getAPIException();
         this.setExitCode(this.xrh.getExitCode());
         this.haveExitCode = true;
      }

      int exitCode = super.getExitCode();
      if (exitCode == Integer.MIN_VALUE) {
         String msg = "Exit code not properly read from the stream.";
         APIExceptionFactory.createAPIException("APIInternalError", msg);
      }

      return exitCode;
   }

   public void interrupt() {
      this.xrh.interrupt();
   }

   private void primeSubRoutineList() {
      if (!this.subRoutinesPrimed) {
         SubRoutineIterator it = this.getSubRoutines();

         while(it.hasNext()) {
            try {
               it.next();
            } catch (APIException var3) {
               IntegrationPointFactory.getLogger().exception((Object)this, "API", 0, var3);
            }
         }

         this.subRoutinesPrimed = true;
      }

   }

   private void primeConnectionAttributes() {
      if (!this.subRoutinesPrimed && !this.xrh.getReadPreSubRoutines()) {
         this.primeSubRoutineList();
      }

      this.xrh.readResponseConnectionAttributes();
   }

   private void primeWorkItemList() {
      if (!this.workItemsPrimed) {
         WorkItemIterator it = this.getWorkItems();

         while(it.hasNext()) {
            try {
               it.next();
            } catch (APIException var3) {
               IntegrationPointFactory.getLogger().exception((Object)this, "API", 0, var3);
            }
         }

         this.workItemsPrimed = true;
      }

   }

   private void primeLists() {
      try {
         this.primeSubRoutineList();
      } catch (APIError var4) {
         if (this.getCacheContents()) {
            throw var4;
         }
      }

      try {
         this.primeWorkItemList();
      } catch (APIError var3) {
         if (this.getCacheContents()) {
            throw var3;
         }
      }

      try {
         this.primeSubRoutineList();
      } catch (APIError var2) {
         if (this.getCacheContents()) {
            throw var2;
         }
      }

   }
}

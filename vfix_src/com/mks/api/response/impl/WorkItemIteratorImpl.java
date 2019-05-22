package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.util.Iterator;

public class WorkItemIteratorImpl implements WorkItemIterator {
   private Iterator it;
   private WorkItem workItem;

   WorkItemIteratorImpl(Iterator it) {
      this.it = it;
   }

   public synchronized WorkItem next() throws APIException {
      this.workItem = (WorkItem)this.it.next();
      if (this.workItem.getAPIException() != null) {
         throw this.workItem.getAPIException();
      } else {
         WorkItem wio = this.workItem;
         this.workItem = null;
         return wio;
      }
   }

   public synchronized boolean hasNext() {
      return this.it.hasNext();
   }

   public WorkItem getLast() {
      return this.workItem;
   }
}

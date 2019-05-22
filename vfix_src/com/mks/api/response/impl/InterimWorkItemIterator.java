package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.util.List;

public class InterimWorkItemIterator extends AbstractInterimIterator implements WorkItemIterator {
   InterimWorkItemIterator(XMLResponseHandler xrh, List wil) {
      super(xrh, wil);
   }

   public synchronized WorkItem next() throws APIException {
      return (WorkItem)super.getNext();
   }

   public WorkItem getLast() {
      WorkItem wio = (WorkItem)this.lastReadObj;
      return wio;
   }

   protected Object nextObject() {
      return this.xrh.readResponseWorkItemsTagAttributes() ? this.xrh.getWorkItem() : null;
   }
}

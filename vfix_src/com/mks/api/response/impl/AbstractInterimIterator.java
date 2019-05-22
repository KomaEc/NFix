package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.APIExceptionContainer;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractInterimIterator {
   protected XMLResponseHandler xrh;
   protected Object lastReadObj;
   private Object nextObj;
   private List list;
   private int listIndex;

   protected AbstractInterimIterator(XMLResponseHandler xrh, List l) {
      this.xrh = xrh;
      this.list = l;
      this.listIndex = 0;
   }

   protected Object getNext() throws APIException {
      APIExceptionContainer obj = null;
      if (this.list != null && this.listIndex < this.list.size()) {
         obj = (APIExceptionContainer)this.list.get(this.listIndex++);
      } else if (this.nextObj != null) {
         if (this.list != null) {
            this.list.add(this.nextObj);
            ++this.listIndex;
         }

         obj = (APIExceptionContainer)this.nextObj;
         this.nextObj = null;
      } else if ((obj = (APIExceptionContainer)this.nextObject()) != null && this.list != null) {
         this.list.add(obj);
         ++this.listIndex;
      }

      this.lastReadObj = null;
      if (obj != null) {
         APIException ex = obj.getAPIException();
         if (ex != null) {
            this.lastReadObj = obj;
            throw ex;
         } else {
            return obj;
         }
      } else {
         throw new NoSuchElementException();
      }
   }

   public synchronized boolean hasNext() {
      if (this.nextObj == null && (this.list == null || this.listIndex >= this.list.size())) {
         this.nextObj = this.nextObject();
         return this.nextObj != null;
      } else {
         return true;
      }
   }

   protected abstract Object nextObject();
}

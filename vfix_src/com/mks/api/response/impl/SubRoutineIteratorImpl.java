package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import java.util.Iterator;

public class SubRoutineIteratorImpl implements SubRoutineIterator {
   private Iterator it;
   private SubRoutine subRoutine;

   SubRoutineIteratorImpl(Iterator it) {
      this.it = it;
   }

   public synchronized SubRoutine next() throws APIException {
      this.subRoutine = (SubRoutine)this.it.next();
      if (this.subRoutine.getAPIException() != null) {
         throw this.subRoutine.getAPIException();
      } else {
         SubRoutine sro = this.subRoutine;
         this.subRoutine = null;
         return sro;
      }
   }

   public synchronized boolean hasNext() {
      return this.it.hasNext();
   }

   public SubRoutine getLast() {
      return this.subRoutine;
   }
}

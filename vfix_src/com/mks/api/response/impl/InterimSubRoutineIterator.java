package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import java.util.List;

public class InterimSubRoutineIterator extends AbstractInterimIterator implements SubRoutineIterator {
   InterimSubRoutineIterator(XMLResponseHandler xrh, List srl) {
      super(xrh, srl);
   }

   public synchronized SubRoutine next() throws APIException {
      return (SubRoutine)super.getNext();
   }

   public SubRoutine getLast() {
      SubRoutine wio = (SubRoutine)this.lastReadObj;
      return wio;
   }

   protected Object nextObject() {
      return this.xrh.getSubRoutine();
   }
}

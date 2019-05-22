package com.mks.api.response.impl;

public final class XMLSubRoutineImpl extends SubRoutineImpl implements ModifiableXMLSubRoutine {
   private XMLResponseContainer xrc;

   XMLSubRoutineImpl(XMLResponseContainer xrc, String routine) {
      super(xrc, routine);
      this.xrc = xrc;
   }

   public void setXMLResponseHandler(XMLResponseHandler xrh) {
      this.xrc.setXMLResponseHandler(xrh);
   }

   public String getWorkItemSelectionType() {
      return this.xrc.getWorkItemSelectionType();
   }
}

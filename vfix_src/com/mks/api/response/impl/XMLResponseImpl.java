package com.mks.api.response.impl;

import com.mks.api.CmdRunner;

public final class XMLResponseImpl extends ResponseImpl implements ModifiableXMLResponse {
   private XMLResponseContainer xrc;

   XMLResponseImpl(CmdRunner cmdRunner, XMLResponseContainer xrc, String app, String cmdName) {
      super(cmdRunner, xrc, app, cmdName);
      this.xrc = xrc;
   }

   public void interrupt() {
      this.xrc.interrupt();
   }

   public void setXMLResponseHandler(XMLResponseHandler xrh) {
      this.xrc.setXMLResponseHandler(xrh);
   }
}

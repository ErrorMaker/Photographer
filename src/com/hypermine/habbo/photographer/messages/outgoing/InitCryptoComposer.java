package com.hypermine.habbo.photographer.messages.outgoing;

import com.hypermine.habbo.photographer.messages.ServerMessage;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class InitCryptoComposer extends MessageComposer {
    @Override
    public ServerMessage compose() {
        this.response.init(3618);
        return this.response;
    }
}

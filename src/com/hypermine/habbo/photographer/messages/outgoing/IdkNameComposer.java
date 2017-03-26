package com.hypermine.habbo.photographer.messages.outgoing;

import com.hypermine.habbo.photographer.messages.ServerMessage;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class IdkNameComposer extends MessageComposer {
    private String sharedKey;

    public IdkNameComposer(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    @Override
    public ServerMessage compose() {
        this.response.init(539);
        this.response.appendString(sharedKey);
        return this.response;
    }
}

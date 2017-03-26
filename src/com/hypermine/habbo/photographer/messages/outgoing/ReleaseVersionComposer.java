package com.hypermine.habbo.photographer.messages.outgoing;

import com.hypermine.habbo.photographer.messages.ServerMessage;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class ReleaseVersionComposer extends MessageComposer {
    @Override
    public ServerMessage compose() {
        this.response.init(4000);
        this.response.appendString("PRODUCTION-201703211204-245648941");
        this.response.appendString("FLASH");
        this.response.appendInt32(1);
        this.response.appendInt32(0);

        return this.response;
    }
}

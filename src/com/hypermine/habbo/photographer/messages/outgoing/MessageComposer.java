package com.hypermine.habbo.photographer.messages.outgoing;

import com.hypermine.habbo.photographer.messages.ServerMessage;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public abstract class MessageComposer
{
    protected final ServerMessage response;

    protected MessageComposer()
    {
        this.response = new ServerMessage();
    }

    public abstract ServerMessage compose();
}
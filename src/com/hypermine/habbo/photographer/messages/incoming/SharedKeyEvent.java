package com.hypermine.habbo.photographer.messages.incoming;

import com.hypermine.habbo.photographer.messages.ServerMessage;

/**
 * Created by scott on 2017-03-27.
 */
public class SharedKeyEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String cKey = packet.readString();
        client.habboEncryption.InitCrypto(cKey);

        ServerMessage msg = new ServerMessage(895);
        msg.appendInt32(401);
        msg.appendString("https://images.habbo.com/gordon/PRODUCTION-201703211204-245648941/");
        msg.appendString("https://www.habbo.com/gamedata/external_variables/595de281707e45dbab45d698fb6e951941569571");
        client.sendMessage(msg);

        msg = new ServerMessage(1284);
        msg.appendString("");
        msg.appendString("~2bab4ea0c412744d9a1be7de0b7df3c1");
        msg.appendString("WIN/25,0,0,127");
//        client.sendMessage(msg);
    }
}

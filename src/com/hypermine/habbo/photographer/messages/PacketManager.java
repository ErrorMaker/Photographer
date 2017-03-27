package com.hypermine.habbo.photographer.messages;

import com.hypermine.habbo.photographer.Session;
import com.hypermine.habbo.photographer.messages.incoming.GenerateSecretKeyEvent;
import com.hypermine.habbo.photographer.messages.incoming.MessageHandler;
import com.hypermine.habbo.photographer.messages.incoming.SharedKeyEvent;
import gnu.trove.map.hash.THashMap;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class PacketManager {
    private final THashMap<Integer, Class<? extends MessageHandler>> incoming;

    public PacketManager() throws Exception {
        this.incoming = new THashMap<>();

        registerHandler(3840, GenerateSecretKeyEvent.class);
        registerHandler(3224, SharedKeyEvent.class);
    }

    public void registerHandler(Integer header, Class<? extends MessageHandler> handler) throws Exception {
        if (header < 0) return;

        if (this.incoming.containsKey(header))
            throw new Exception("Header already registered. Failed to register " + handler.getName() + " with header " + header);

        this.incoming.putIfAbsent(header, handler);
    }

    public void handlePacket(Session ctx, ClientMessage packet) {
        if (ctx == null) return;

        try {
            if (this.isRegistered(packet.getMessageId())) {
                final MessageHandler handler = this.incoming.get(packet.getMessageId()).newInstance();

                handler.client = ctx;
                handler.packet = packet;
                handler.handle();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            packet.getBuffer().resetReaderIndex();
            System.out.println(String.format("INCOMING [%s] - %s", packet.getMessageId(), packet.getMessageBody()));
        }
    }

    boolean isRegistered(int header) {
        return this.incoming.containsKey(header);
    }
}

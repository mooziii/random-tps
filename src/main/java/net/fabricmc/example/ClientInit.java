package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInit implements ClientModInitializer {

    public static double tickSpeed = 1.0;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SharedData.CHANNEL_ID, ((client, handler, buf, responseSender) -> {
            tickSpeed = buf.readDouble();
        }));
    }
}

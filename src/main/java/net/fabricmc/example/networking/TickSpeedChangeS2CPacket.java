package net.fabricmc.example.networking;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;

public class TickSpeedChangeS2CPacket implements Packet<ClientPlayPacketListener> {

    private final float tickSpeed;

    public TickSpeedChangeS2CPacket(float tickSpeed) {
        this.tickSpeed = tickSpeed;
    }

    public TickSpeedChangeS2CPacket(PacketByteBuf buf) {
        this.tickSpeed = buf.readFloat();
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.tickSpeed);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {

    }

    public float getTickSpeed() {
        return this.tickSpeed;
    }
}

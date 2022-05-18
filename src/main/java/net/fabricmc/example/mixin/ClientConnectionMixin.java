package net.fabricmc.example.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.ClientInit;
import net.fabricmc.example.networking.TickSpeedChangeS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(packet instanceof TickSpeedChangeS2CPacket) {
            ci.cancel();
            ClientInit.tickSpeed = ((TickSpeedChangeS2CPacket) packet).getTickSpeed();
        }
    }

}

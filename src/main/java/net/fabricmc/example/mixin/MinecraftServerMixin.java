package net.fabricmc.example.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.SharedData;
import net.fabricmc.example.networking.TickSpeedChangeS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;
import java.util.UUID;
import java.util.function.BooleanSupplier;

@Environment(EnvType.SERVER)
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract void sendSystemMessage(Text message, UUID sender);

    @Shadow private PlayerManager playerManager;

    private int tick = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHalfAsFast(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(tick == 600) {
            tick = 0;
            Random random = new Random();
            double tps = random.nextDouble();
            if(tps < 0.08) {
                tps = 1;
            }
            if(random.nextBoolean()) {
                tps*=2;
                if(random.nextInt(4) == 1) {
                    tps*=2;
                }
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeDouble(tps);
            for (ServerPlayerEntity player : playerManager.getPlayerList()) {
                ServerPlayNetworking.send(player, SharedData.CHANNEL_ID, buf);
                player.sendMessage(Text.of("Game speed changed to " + tps + " :)"), false);
            }
        }
        tick++;
    }

}

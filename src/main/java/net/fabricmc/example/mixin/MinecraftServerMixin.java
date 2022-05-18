package net.fabricmc.example.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.networking.TickSpeedChangeS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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

    @Shadow private int ticks;

    @Shadow public abstract void sendSystemMessage(Text message, UUID sender);

    @Shadow private PlayerManager playerManager;

    private int tick = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHalfAsFast(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(tick == 600) {
            tick = 0;
            Random random = new Random();
            double tps = random.nextDouble();
            if(random.nextBoolean()) {
                tps*=2;
            }
            for (ServerPlayerEntity player : playerManager.getPlayerList()) {
                player.networkHandler.sendPacket(new TickSpeedChangeS2CPacket((float) tps));
            }
            sendSystemMessage(Text.of("Game speed changed to " + tps + " :)"), UUID.randomUUID());
        }
        tick++;
    }

}

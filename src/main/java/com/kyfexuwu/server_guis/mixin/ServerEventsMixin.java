package com.kyfexuwu.server_guis.mixin;

import com.kyfexuwu.server_guis.ServerGuiHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerEventsMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method="onRenameItem",at=@At("HEAD"))
    public void onRenameItem__serverguis(RenameItemC2SPacket packet, CallbackInfo ci) {
        NetworkThreadUtils.forceMainThread(packet, (ServerPlayNetworkHandler)(Object)this,
                this.player.getServerWorld());
        ScreenHandler var3 = this.player.currentScreenHandler;
        if (var3 instanceof ServerGuiHandler handler) {
            handler.gui.onAnvilType(packet.getName());
        }
    }
}

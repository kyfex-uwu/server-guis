package com.kyfexuwu.server_guis.consumers;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface ButtonClickConsumer<T> {
    boolean consume(ServerPlayerEntity player, InvGUI<?> thisInv, T argument, int buttonIndex);
}

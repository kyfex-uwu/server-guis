package com.kyfexuwu.server_guis;

import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface CloseConsumer<T> {
    void consume(ServerPlayerEntity player, InvGUI<?> thisInv, T argument);
}

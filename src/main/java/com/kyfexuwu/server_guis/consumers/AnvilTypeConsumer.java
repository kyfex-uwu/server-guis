package com.kyfexuwu.server_guis.consumers;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface AnvilTypeConsumer<T> {
    void consume(ServerPlayerEntity player, InvGUI<?> thisInv, T argument, String newText);
}

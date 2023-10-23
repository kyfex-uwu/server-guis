package com.kyfexuwu.server_guis.consumers;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface PropertyUpdateConsumer<T> {
    void consume(ServerPlayerEntity player, InvGUI<?> thisInv, T argument, int property, int value);
}

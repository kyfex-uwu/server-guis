package com.kyfexuwu.server_guis.consumers;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

@FunctionalInterface
public interface BeaconInteractionConsumer<T> {
    void consume(ServerPlayerEntity player, InvGUI<?> thisInv, T argument,
                 Optional<StatusEffect> effect1, Optional<StatusEffect> effect2);
}

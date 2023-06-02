package com.kyfexuwu.server_guis;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;

@FunctionalInterface
public interface ClickConsumer<T> {
    ClickConsumer<?> NOTHING = (_1,_2,_3,_4,_5,_6)->{};
    void consume(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, InvGUI<?> thisInv, T argument);
}

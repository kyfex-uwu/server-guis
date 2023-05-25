package com.kyfexuwu.server_guis.mixin;

import com.kyfexuwu.server_guis.InvGUI;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GlazedTerracottaBlock.class)
public class ExampleUseCase {

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        InvGUI.get("screen1").build(player).open(player);
        return ActionResult.success(world.isClient);
    }
}

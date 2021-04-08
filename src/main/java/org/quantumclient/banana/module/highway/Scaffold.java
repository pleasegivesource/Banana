package org.quantumclient.banana.module.highway;

import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;
import org.quantumclient.banana.event.EventSingleTick;
import org.quantumclient.banana.event.EventTwelvetupleTick;
import org.quantumclient.banana.module.Category;
import org.quantumclient.banana.module.Feature;
import org.quantumclient.banana.settings.Setting;
import org.quantumclient.energy.Subscribe;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Feature {

    final static List<String> eventMode = Arrays.asList("twelve-tuple", "single");

    public static final Setting eventM = new Setting("event", "single", eventMode);

    public Scaffold() {
        super("scaffold", Category.highway, GLFW.GLFW_KEY_I);
    }

    private int prevSlot = 0;

    @Subscribe
    public void onTick(EventTwelvetupleTick event) {
        if (eventM.getValString().equals("twelve-tuple")) {
            if (mc.world.getBlockState(mc.player.getBlockPos().down()).isAir()) {
                searchBlocks();
                placeBlock(mc.player.getBlockPos().down());
                mc.player.inventory.selectedSlot = prevSlot;
            }
        }
    }

    @Subscribe
    public void onTick2(EventSingleTick event) {
        if (eventM.getValString().equals("single")) {
            if (mc.world.getBlockState(mc.player.getBlockPos().down()).isAir()) {
                searchBlocks();
                placeBlock(mc.player.getBlockPos().down());
                mc.player.inventory.selectedSlot = prevSlot;
            }
        }
    }

    private void placeBlock(BlockPos pos) {
        BlockHitResult result = mc.world.raycast(new RaycastContext(
                new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ()),
                new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, result);
    }

    private void searchBlocks() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getMainHandStack().getItem() instanceof BlockItem) break;
            else {
                if (mc.player.inventory.getStack(i).getItem() instanceof BlockItem && !(((BlockItem) mc.player.inventory.getStack(i).getItem()).getBlock() instanceof FallingBlock)) {
                    prevSlot = mc.player.inventory.selectedSlot;
                    mc.player.inventory.selectedSlot = i;
                    break;
                }
            }
        }
    }

}

package ml.pluto7073.icu.bundles.client.events;

import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.multiplayer.ICUServerboundPackets;
import ml.pluto7073.icu.bundles.tooltip.BundleSelection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ICUClientEvents {

    private static double accScroll = 0;

    public static void register() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AbstractContainerScreen<?> menuScreen) {
                ScreenMouseEvents.afterMouseScroll(menuScreen).register((scr, mouseX, mouseY, h, v) ->
                        onMouseScrolled(menuScreen, mouseX, mouseY, v));
            }
        });
    }

    private static boolean onMouseScrolled(AbstractContainerScreen<?> screen, double x, double y, double scroll) {
        Slot slot = screen.findSlot(x, y);
        if (slot == null) return true;
        ItemStack stack = slot.getItem();
        if (!(stack.getItem() instanceof BundleItem)) return true;
        if (accScroll * scroll < 0) accScroll = 0;
        accScroll += scroll;
        int diff = (int) accScroll;
        accScroll -= diff;
        if (diff == 0) return true;
        BundleSelection.add(stack, -diff);

        FriendlyByteBuf buf = PacketByteBufs.create();

        buf.writeInt(screen.getMenu().containerId);
        buf.writeInt(screen.getMenu().getStateId());
        buf.writeInt(slot.index);
        buf.writeInt(-diff);

        ClientPlayNetworking.send(ICUServerboundPackets.BUNDLE_SCROLL_PACKET, buf);
        return false;
    }

}

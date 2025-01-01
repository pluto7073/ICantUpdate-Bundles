package ml.pluto7073.icu.bundles.multiplayer;

import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.tooltip.BundleSelection;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

public class ICUServerboundPackets {

    public static final ResourceLocation BUNDLE_SCROLL_PACKET = BundlesOfBravery.asId("scroll_selection");

    public static void registerReceivers() {
        ServerPlayConnectionEvents.INIT.register((h, s) ->
                ServerPlayNetworking.registerGlobalReceiver(BUNDLE_SCROLL_PACKET, (server, player, handler, buf, sender) -> {
                    int container = buf.readInt();
                    int state = buf.readInt();
                    int slotId = buf.readInt();
                    int difference = buf.readInt();
                    server.execute(() -> {
                        player.resetLastActionTime();
                        AbstractContainerMenu menu = player.containerMenu;

                        if (menu.containerId != container) return;
                        if (player.isSpectator()) {
                            menu.sendAllDataToRemote();
                            return;
                        }
                        if (!menu.stillValid(player)) return;
                        if (!menu.isValidSlotIndex(slotId)) return;

                        boolean flag = state == menu.getStateId();
                        menu.suppressRemoteUpdates();
                        Slot slot = menu.getSlot(slotId);
                        ItemStack stack = slot.getItem();
                        if (stack.getItem() instanceof BundleItem) {
                            BundleSelection.add(stack, difference);
                        }
                        menu.resumeRemoteUpdates();

                        if (flag) {
                            menu.broadcastFullState();
                        } else {
                            menu.broadcastChanges();
                        }
                    });
                }));
    }

}

package com.zenesta.itemtagconverter.common.keybind;

import com.zenesta.itemtagconverter.client.config.ClientConfig;
import com.zenesta.itemtagconverter.common.config.CommonConfig;
import com.zenesta.itemtagconverter.common.network.ConvertMessage;
import com.zenesta.itemtagconverter.common.network.NetworkManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.zenesta.itemtagconverter.ItemTagConverter;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public class KeyBinder {
    public static final Lazy<KeyMapping> KEYBINDING_CONVERT = Lazy.of(() -> new KeyMapping("key.itemtagconverter.convert", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_END,
            "key.itemtagconverter.category"));

    @Mod.EventBusSubscriber(modid = ItemTagConverter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerKeyMapping(final RegisterKeyMappingsEvent event) {
            event.register(KEYBINDING_CONVERT.get());
        }
    }

    @Mod.EventBusSubscriber(modid = ItemTagConverter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(final TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
                if (KEYBINDING_CONVERT.get().consumeClick() && ClientConfig.CLIENT.enableKeypress.get()) {
                    NetworkManager.sendToServer(new ConvertMessage());
                }
            }
        }
    }
}

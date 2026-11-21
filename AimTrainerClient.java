package com.example.aimtrainer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class AimTrainerClient implements ClientModInitializer {
    private static KeyBinding toggleHud;
    private static boolean enabled = true;
    private static String status = "Practice HUD: ON";

    @Override
    public void onInitializeClient() {
        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aimtrainer.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.aimtrainer"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHud.wasPressed()) {
                enabled = !enabled;
                status = enabled ? "Practice HUD: ON" : "Practice HUD: OFF";
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("[AimTrainer] " + status), true);
                }
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (!enabled) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            drawHud(drawContext);
        });
    }

    private static void drawHud(DrawContext context) {
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                "AimTrainer | Local practice", 8, 8, 0xFFFFFF);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                "Press H to toggle HUD", 8, 20, 0xAAAAAA);
    }
}

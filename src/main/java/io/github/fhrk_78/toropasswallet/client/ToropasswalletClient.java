package io.github.fhrk_78.toropasswallet.client;

import org.lwjgl.glfw.GLFW;

import io.github.fhrk_78.toropasswallet.Toropasswallet;
import io.github.fhrk_78.toropasswallet.client.renderer.ToropassWalletApp;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class ToropasswalletClient implements ClientModInitializer {
    private KeyBinding openWalletAppKey;

    public static Integer beforeRide = -1;
    public static String beforeStationName = "";

    @Override
    public void onInitializeClient() {
        openWalletAppKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.toropasswallet.open_wallet_app",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "key.toropasswallet.category"
        ));

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String string = message.getString();
            if (string == null) return;
            ToroPassResponseParser.processIt(string);
        });

        ClientTickEvents.START_CLIENT_TICK.register(
        client -> {
            if (Toropasswallet.isJoiningTOROServer()) {
                if (openWalletAppKey.wasPressed())
                    client.setScreen(new ToropassWalletApp());
            }
        });
    }
}

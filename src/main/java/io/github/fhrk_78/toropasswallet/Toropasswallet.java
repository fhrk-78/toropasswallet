package io.github.fhrk_78.toropasswallet;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class Toropasswallet implements ModInitializer {
    // TODO: 完了時はfalseにするのを忘れずに
    public static boolean isDEBUG = true;
    public static final String MOD_ID = "toropasswallet";

    @Override
    public void onInitialize() {
    }

    public static boolean isJoiningTOROServer() {
        if (isDEBUG) return true;
        var serverEntry = MinecraftClient.getInstance().getCurrentServerEntry();
        if (serverEntry == null) return false;
        return serverEntry.address.equals("torosaba.net");
    }
}

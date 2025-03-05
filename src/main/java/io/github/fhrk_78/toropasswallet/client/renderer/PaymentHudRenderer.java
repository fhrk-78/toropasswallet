package io.github.fhrk_78.toropasswallet.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class PaymentHudRenderer {
    private static final short MARGIN = 7;
    private static int tickCounter = 0;
    private static boolean shouldIShow = false;
    private static float percentage = 0;
    private static final Identifier paymentT =
            Identifier.tryParse("toropasswallet", "textures/misc/payment.png");

    public static void render(DrawContext ctx, RenderTickCounter renderTickCounter) {
        int height = ctx.getScaledWindowHeight();

        int overlaySize = (int) (percentage / 100f * (height >> 1));

        ctx.drawTexture(paymentT, MARGIN, height - overlaySize, 0, 0,
                overlaySize, overlaySize, overlaySize, overlaySize);
    }

    public static void tick(MinecraftClient client) {
        if (tickCounter >= 1) tickCounter--;
        else shouldIShow = false;

        percentage += ((shouldIShow ? 100 : 0) - percentage) / 12;
    }

    public static void show() {
        tickCounter = 20;
        shouldIShow = true;
    }
}

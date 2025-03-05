package io.github.fhrk_78.toropasswallet.client.renderer;

import io.github.fhrk_78.toropasswallet.client.DataLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static io.github.fhrk_78.toropasswallet.Toropasswallet.MOD_ID;

public final class ToropassWalletApp extends Screen {
    public final short MARGIN = 7;
    public Identifier toropassT;

    public final short balanceAndCardGap = 5;
    public TextWidget balanceTextWidget;

    public PaymentHistoryWidget paymentHistoryWidget;

    final int CONTENT_GAP = 5;
    int CONTENT_WIDTH, CONTNT_HEIGHT, CARD_HEIGHT;

    public ToropassWalletApp() {
        super(Text.literal("ToropassWallet"));
    }

    @Override
    protected void init() {
        toropassT = Identifier.tryParse(MOD_ID, "textures/misc/toropass.png");

        CONTENT_WIDTH = (width - (MARGIN << 1)) / 3 - (CONTENT_GAP << 1);
        CONTNT_HEIGHT = height - (MARGIN << 1);
        CARD_HEIGHT = (int) (CONTENT_WIDTH * 81f / 128f);

        final int balanceTextWidgetHeight = height - MARGIN - CARD_HEIGHT;
        balanceTextWidget = new TextWidget(MARGIN, height - MARGIN - balanceTextWidgetHeight, CONTENT_WIDTH,
                balanceTextWidgetHeight, Text.literal(""), textRenderer);

        paymentHistoryWidget = new PaymentHistoryWidget(client, CONTENT_WIDTH, CONTNT_HEIGHT,
                height - MARGIN - CONTENT_WIDTH, MARGIN);

        addDrawableChild(balanceTextWidget);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        final int balance = DataLoader.getInstance().getBalance();
        balanceTextWidget.setMessage(Text.literal("残高: " + balance + "トロポ"));

        paymentHistoryWidget.updateEntries(DataLoader.getInstance().histories);

        super.render(ctx, mouseX, mouseY, delta);

        ctx.drawTexture(toropassT, MARGIN, MARGIN, 0, 0,
                CONTENT_WIDTH, CARD_HEIGHT, CONTENT_WIDTH, CARD_HEIGHT);
    }
}

package io.github.fhrk_78.toropasswallet.client.renderer;

import io.github.fhrk_78.toropasswallet.client.data.RideHistory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

import java.util.List;

public final class PaymentHistoryWidget extends AlwaysSelectedEntryListWidget<PaymentHistoryWidget.Entry> {
    public PaymentHistoryWidget(MinecraftClient minecraftClient, int width, int height, int x, int y) {
        super(minecraftClient, width, height, y,
                (Entry.MARGIN << 1) + (minecraftClient.textRenderer.fontHeight << 1) + Entry.GAP);
        setX(x);
    }

    public void updateEntries(List<RideHistory> entries) {
        clearEntries();
        entries.forEach(v -> addEntryToTop(new Entry(v, client.textRenderer)));
    }

    public static final class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        private final RideHistory history;
        private final TextRenderer textRenderer;
        private static final int MARGIN = 5;
        private static final int GAP = 3;

        public Entry(RideHistory h, TextRenderer textRenderer) {
            history = h;
            this.textRenderer = textRenderer;
        }

        @Override
        public Text getNarration() {
            return Text.empty();
        }

        @Override
        public void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float tickDelta) {
            String showResult = "差額: " + history.amount() + "トロポ / 残高: " +
                    (history.before() + history.amount()) + "トロポ";
            if (history.from().equals("#AUTOCHARGE")) {
                if (history.amount() <= 0) return;
                ctx.drawTextWrapped(textRenderer, Text.literal("オートチャージ"), x + MARGIN, y + MARGIN,
                        entryWidth - (MARGIN << 1), 0xffffffff);
                ctx.drawTextWrapped(textRenderer, Text.literal(showResult), x + MARGIN,
                        y + MARGIN + textRenderer.fontHeight + GAP, entryWidth - (MARGIN << 1),
                        0xff888888);
            } else if (history.from().equals("#CHARGE")) {
                ctx.drawTextWrapped(textRenderer, Text.literal("チャージ"), x + MARGIN, y + MARGIN,
                        entryWidth - (MARGIN << 1), 0xffffffff);
                ctx.drawTextWrapped(textRenderer, Text.literal(showResult), x + MARGIN,
                        y + MARGIN + textRenderer.fontHeight + GAP, entryWidth - (MARGIN << 1),
                        0xff888888);
            } else if (history.to().equals("#FORCEEXIT")) {
                ctx.drawTextWrapped(textRenderer, Text.literal(history.from() + " (強制出場)"), x + MARGIN,
                        y + MARGIN, entryWidth - (MARGIN << 1), 0xffffffff);
            } else {
                ctx.drawTextWrapped(textRenderer, Text.literal(history.from() + "→" + history.to()),
                        x + MARGIN, y + MARGIN, entryWidth - (MARGIN << 1), 0xffffffff);
                ctx.drawTextWrapped(textRenderer, Text.literal(showResult), x + MARGIN,
                        y + MARGIN + textRenderer.fontHeight + GAP, entryWidth - (MARGIN << 1),
                        0xff888888);
            }
        }
    }
}

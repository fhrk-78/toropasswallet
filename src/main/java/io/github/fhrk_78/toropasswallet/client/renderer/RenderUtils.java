package io.github.fhrk_78.toropasswallet.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public final class RenderUtils {
    public static void triangle(MatrixStack mtx, float x1, float y1, float x2, float y2, float x3, float y3, int color)
    {
        Matrix4f transformationMatrix = mtx.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP,
                VertexFormats.POSITION_COLOR);

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        buffer.vertex(transformationMatrix, x1, y1, 0).color(color);
        buffer.vertex(transformationMatrix, x2, y2, 0).color(color);
        buffer.vertex(transformationMatrix, x3, y3, 0).color(color);
        buffer.vertex(transformationMatrix, x1, y1, 0).color(color);
        buffer.vertex(transformationMatrix, x2, y2, 0).color(color);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }
}

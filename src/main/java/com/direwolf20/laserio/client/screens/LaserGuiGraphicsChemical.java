package com.direwolf20.laserio.client.screens;

import com.direwolf20.laserio.common.items.filters.FilterCount;
import com.direwolf20.laserio.util.MiscTools;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import static com.direwolf20.laserio.integration.mekanism.MekanismStatics.getFirstChemicalOnItemStack;

public class LaserGuiGraphicsChemical extends GuiGraphics {
    public Minecraft minecraft;
    protected final AbstractContainerScreen screen;

    public LaserGuiGraphicsChemical(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource, AbstractContainerScreen screen) {
        super(minecraft, bufferSource);
        this.minecraft = minecraft;
        this.screen = screen;
    }

    private void fillRect(BufferBuilder p_115153_, int p_115154_, int p_115155_, int p_115156_, int p_115157_, int p_115158_, int p_115159_, int p_115160_, int p_115161_) {
        Matrix4f matrix4f = pose().last().pose();
        VertexConsumer vertexconsumer = bufferSource().getBuffer(RenderType.guiOverlay());
        vertexconsumer.vertex(matrix4f, (float) (p_115154_ + 0), (float) (p_115155_ + 0), (float) 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        vertexconsumer.vertex(matrix4f, (float) (p_115154_ + 0), (float) (p_115155_ + p_115157_), (float) 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        vertexconsumer.vertex(matrix4f, (float) (p_115154_ + p_115156_), (float) (p_115155_ + p_115157_), (float) 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        vertexconsumer.vertex(matrix4f, (float) (p_115154_ + p_115156_), (float) (p_115155_ + 0), (float) 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        this.flush();
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack itemstack, int x, int y, @Nullable String altText) {
        if (shouldRenderChemical(itemstack, x, y, true, false)) {
            CardChemicalScreen cardChemicalScreen = (CardChemicalScreen) screen;
            int sloty = (int) Math.floor((y - cardChemicalScreen.filterStartY) / 18);
            int slotx = (int) Math.floor((x - cardChemicalScreen.filterStartX) / 18);
            int slot = ((5 * sloty) + slotx);
            ItemStack filter = ((CardChemicalScreen) screen).filter;
            int totalmbAmt = FilterCount.getSlotAmount(filter, slot);
            int count = (int) Math.floor(totalmbAmt / 1000);
            int mbAmt = totalmbAmt % 1000;
            PoseStack posestack = pose();
            if (count != 0 || mbAmt != 0) {
                String textToDraw;
                textToDraw = count + "b";
                posestack.pushPose();
                posestack.translate(x, y, 200);
                posestack.scale(0.5f, 0.5f, 0.5f);
                if (mbAmt == 0) {
                    this.drawString(font, textToDraw, (float) (17 - font.width(textToDraw) * 0.5f), (float) (24), 16777215, true);
                } else {
                    String textToDraw2 = mbAmt + "mb";
                    this.drawString(font, textToDraw, (float) (17 - font.width(textToDraw) * 0.5f), (float) (14), 16777215, true);
                    this.drawString(font, textToDraw2, (float) (17 - font.width(textToDraw2) * 0.5f), (float) (24), 16777215, true);
                }
                posestack.popPose();
            }
        } else {
            if (!itemstack.isEmpty()) {
                PoseStack posestack = pose();
                if (itemstack.getCount() != 1 || altText != null) {
                    String textToDraw = altText == null ? String.valueOf(itemstack.getCount()) : altText;
                    posestack.translate(0.0D, 0.0D, (double) (200.0F));
                    //MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    if (itemstack.getCount() > 99) {
                        posestack.pushPose();
                        posestack.translate(x, y, 300);
                        posestack.scale(0.65f, 0.65f, 0.65f);
                        this.drawString(font, textToDraw, (float) (17 - font.width(textToDraw) * 0.65f), (float) (17), 16777215, true);
                        posestack.popPose();
                    } else {
                        this.drawString(font, textToDraw, (float) (x + 19 - 2 - font.width(textToDraw)), (float) (y + 6 + 3), 16777215, true);
                    }
                    //multibuffersource$buffersource.endBatch();
                }

                if (!shouldRenderChemical(itemstack, x, y, true, true)) {
                    RenderSystem.disableDepthTest();
                    //RenderSystem.disableTexture();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    Tesselator tesselator1 = Tesselator.getInstance();
                    BufferBuilder bufferbuilder1 = tesselator1.getBuilder();
                    this.fillRect(bufferbuilder1, x, y, 16, Mth.ceil(16.0F), 255, 0, 0, 127);
                    //RenderSystem.enableTexture();
                    //RenderSystem.enableDepthTest();
                }

                if (itemstack.isBarVisible()) {
                    RenderSystem.disableDepthTest();
                    //RenderSystem.disableTexture();
                    RenderSystem.disableBlend();
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferbuilder = tesselator.getBuilder();
                    int i = itemstack.getBarWidth();
                    int j = itemstack.getBarColor();
                    this.fillRect(bufferbuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
                    this.fillRect(bufferbuilder, x + 2, y + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                    //RenderSystem.enableBlend();
                    ////RenderSystem.enableTexture();
                    //RenderSystem.enableDepthTest();
                }

                LocalPlayer localplayer = Minecraft.getInstance().player;
                float f = localplayer == null ? 0.0F : localplayer.getCooldowns().getCooldownPercent(itemstack.getItem(), Minecraft.getInstance().getFrameTime());
                if (f > 0.0F) {
                    RenderSystem.disableDepthTest();
                    //RenderSystem.disableTexture();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    Tesselator tesselator1 = Tesselator.getInstance();
                    BufferBuilder bufferbuilder1 = tesselator1.getBuilder();
                    this.fillRect(bufferbuilder1, x, y + Mth.floor(16.0F * (1.0F - f)), 16, Mth.ceil(16.0F * f), 255, 255, 255, 127);
                    //RenderSystem.enableTexture();
                    RenderSystem.enableDepthTest();
                }
            }
        }
    }

    public boolean shouldRenderChemical(ItemStack pStack, int pX, int pY, boolean includeCarried, boolean reverseBounds) {
        if (!(screen instanceof CardChemicalScreen)) {
            return reverseBounds;
        }
        CardChemicalScreen cardChemicalScreen = (CardChemicalScreen) screen;
        if (cardChemicalScreen.getMenu().getCarried().equals(pStack)) {
            if (includeCarried)
                return reverseBounds;
        }
        if (reverseBounds) {
            return !(MiscTools.inBounds(cardChemicalScreen.filterStartX, cardChemicalScreen.filterStartY, cardChemicalScreen.filterEndX - cardChemicalScreen.filterStartX, cardChemicalScreen.filterEndY - cardChemicalScreen.filterStartY, pX, pY));
        } else {
            if (!MiscTools.inBounds(cardChemicalScreen.filterStartX, cardChemicalScreen.filterStartY, cardChemicalScreen.filterEndX - cardChemicalScreen.filterStartX, cardChemicalScreen.filterEndY - cardChemicalScreen.filterStartY, pX, pY)) {
                return reverseBounds;
            }
        }
        ChemicalStack<?> chemicalStack = getFirstChemicalOnItemStack(pStack);
        if (chemicalStack.isEmpty()) {
            return reverseBounds;
        }

        ResourceLocation chemicalStill = chemicalStack.getType().getIcon();
        TextureAtlasSprite chemicalStillSprite = null;
        if (chemicalStill != null) {
            chemicalStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(chemicalStill);
        }

        if (chemicalStillSprite == null) {
            return reverseBounds;
        }
        return !reverseBounds;
    }

    @Override
    public void renderItem(ItemStack pStack, int pX, int pY, int something) {
        if (!shouldRenderChemical(pStack, pX, pY, true, false)) {
            super.renderItem(pStack, pX, pY, something);
            return;
        }
        ChemicalStack<?> chemicalStack = getFirstChemicalOnItemStack(pStack); //We checked above to ensure this isn't empty
        ResourceLocation chemicalStill = chemicalStack.getType().getIcon();
        TextureAtlasSprite chamicalStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(chemicalStill);
        int chemicalColor = chemicalStack.getChemicalColorRepresentation();

        float red = (float) (chemicalColor >> 16 & 255) / 255.0F;
        float green = (float) (chemicalColor >> 8 & 255) / 255.0F;
        float blue = (float) (chemicalColor & 255) / 255.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        PoseStack posestack = pose();
        posestack.pushPose();
        RenderSystem.setShaderColor(red, green, blue, 1.0f);
        int zLevel = 100;
        float uMin = chamicalStillSprite.getU0();
        float uMax = chamicalStillSprite.getU1();
        float vMin = chamicalStillSprite.getV0();
        float vMax = chamicalStillSprite.getV1();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuilder();

        vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        vertexBuffer.vertex(posestack.last().pose(), pX, pY + 16.0F, zLevel).uv(uMin, vMax).endVertex();
        vertexBuffer.vertex(posestack.last().pose(), pX + 16.0F, pY + 16.0F, zLevel).uv(uMax, vMax).endVertex();
        vertexBuffer.vertex(posestack.last().pose(), pX + 16.0F, pY, zLevel).uv(uMax, vMin).endVertex();
        vertexBuffer.vertex(posestack.last().pose(), pX, pY, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
        posestack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
    }
}
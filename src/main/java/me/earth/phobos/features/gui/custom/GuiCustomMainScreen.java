//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.gui.custom;

import net.minecraft.util.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.*;
import org.lwjgl.opengl.*;
import java.awt.image.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import java.awt.*;
import me.earth.phobos.util.*;

public class GuiCustomMainScreen extends GuiScreen
{
    private ResourceLocation resourceLocation;
    private final String backgroundURL = "https://i.imgur.com/GCJRhiA.png";
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int exitWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;
    
    public GuiCustomMainScreen() {
        this.resourceLocation = new ResourceLocation("textures/background.png");
    }
    
    public void initGui() {
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        this.buttonList.add(new TextButton(0, this.x, this.y + 20, "Singleplayer"));
        this.buttonList.add(new TextButton(1, this.x, this.y + 44, "Multiplayer"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 66, "Settings"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 88, "Exit"));
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public void updateScreen() {
        super.updateScreen();
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (isHovered(this.x - Phobos.textManager.getStringWidth("Singleplayer") / 2, this.y + 20, Phobos.textManager.getStringWidth("Singleplayer"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        }
        else if (isHovered(this.x - Phobos.textManager.getStringWidth("Multiplayer") / 2, this.y + 44, Phobos.textManager.getStringWidth("Multiplayer"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        else if (isHovered(this.x - Phobos.textManager.getStringWidth("Settings") / 2, this.y + 66, Phobos.textManager.getStringWidth("Settings"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        else if (isHovered(this.x - Phobos.textManager.getStringWidth("Exit") / 2, this.y + 88, Phobos.textManager.getStringWidth("Exit"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.xOffset = -1.0f * ((mouseX - this.width / 2.0f) / (this.width / 32.0f));
        this.yOffset = -1.0f * ((mouseY - this.height / 2.0f) / (this.height / 18.0f));
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.resourceLocation);
        drawCompleteImage(-16.0f + this.xOffset, -9.0f + this.yOffset, (float)(this.width + 32), (float)(this.height + 18));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public static void drawCompleteImage(final float posX, final float posY, final float width, final float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(width, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public BufferedImage parseBackground(final BufferedImage background) {
        int width = 1920;
        int height = 1080;
        for (int srcWidth = background.getWidth(), srcHeight = background.getHeight(); width < srcWidth || height < srcHeight; width *= 2, height *= 2) {}
        final BufferedImage imgNew = new BufferedImage(width, height, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        return imgNew;
    }
    
    public static boolean isHovered(final int x, final int y, final int width, final int height, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }
    
    private static class TextButton extends GuiButton
    {
        public TextButton(final int buttonId, final int x, final int y, final String buttonText) {
            super(buttonId, x, y, Phobos.textManager.getStringWidth(buttonText), Phobos.textManager.getFontHeight(), buttonText);
        }
        
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (mouseX >= this.x - Phobos.textManager.getStringWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
                Phobos.textManager.drawStringWithShadow(this.displayString, this.x - Phobos.textManager.getStringWidth(this.displayString) / 2.0f, (float)this.y, Color.WHITE.getRGB());
                if (this.hovered) {
                    RenderUtil.drawLine(this.x - 1 - Phobos.textManager.getStringWidth(this.displayString) / 2.0f, (float)(this.y + 2 + Phobos.textManager.getFontHeight()), this.x + Phobos.textManager.getStringWidth(this.displayString) / 2.0f + 1.0f, (float)(this.y + 2 + Phobos.textManager.getFontHeight()), 1.0f, Color.WHITE.getRGB());
                }
            }
        }
        
        public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
            return this.enabled && this.visible && mouseX >= this.x - Phobos.textManager.getStringWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }
}

//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.gui;

import net.minecraft.client.gui.*;
import me.earth.phobos.features.gui.components.*;
import me.earth.phobos.*;
import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.gui.components.items.buttons.*;
import java.util.*;
import me.earth.phobos.features.gui.components.items.*;
import org.lwjgl.input.*;
import java.io.*;

public class PhobosGui extends GuiScreen
{
    private static PhobosGui phobosGui;
    private final ArrayList<Component> components;
    private static PhobosGui INSTANCE;
    
    public PhobosGui() {
        this.components = new ArrayList<Component>();
        this.setInstance();
        this.load();
    }
    
    public static PhobosGui getInstance() {
        if (PhobosGui.INSTANCE == null) {
            PhobosGui.INSTANCE = new PhobosGui();
        }
        return PhobosGui.INSTANCE;
    }
    
    private void setInstance() {
        PhobosGui.INSTANCE = this;
    }
    
    public static PhobosGui getClickGui() {
        return getInstance();
    }
    
    private void load() {
        int x = -84;
        for (final Module.Category category : Phobos.moduleManager.getCategories()) {
            final ArrayList<Component> components2 = this.components;
            final String name = category.getName();
            x += 90;
            components2.add(new Component(name, x, 4, true) {
                @Override
                public void setupItems() {
                    Phobos.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort((item1, item2) -> item1.getName().compareTo(item2.getName())));
    }
    
    public void updateModule(final Module module) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (item instanceof ModuleButton) {
                    final ModuleButton button = (ModuleButton)item;
                    final Module mod = button.getModule();
                    if (module != null && module.equals(mod)) {
                        button.initSettings();
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public final ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        }
        else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }
    
    public int getTextOffset() {
        return -6;
    }
    
    public Component getComponentByName(final String name) {
        for (final Component component : this.components) {
            if (component.getName().equalsIgnoreCase(name)) {
                return component;
            }
        }
        return null;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    
    static {
        PhobosGui.INSTANCE = new PhobosGui();
    }
}

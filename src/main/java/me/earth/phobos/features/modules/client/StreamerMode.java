//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.*;
import me.earth.phobos.*;
import java.text.*;
import net.minecraft.potion.*;
import me.earth.phobos.util.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class StreamerMode extends Module
{
    private SecondScreenFrame window;
    public Setting<Integer> width;
    public Setting<Integer> height;
    
    public StreamerMode() {
        super("StreamerMode", "Displays client info in a second window.", Category.CLIENT, false, false, false);
        this.window = null;
        this.width = (Setting<Integer>)this.register(new Setting("Width", 600, 100, 3160));
        this.height = (Setting<Integer>)this.register(new Setting("Height", 900, 100, 2140));
    }
    
    @Override
    public void onEnable() {
        EventQueue.invokeLater(() -> {
            if (this.window == null) {
                this.window = new SecondScreenFrame();
            }
            this.window.setVisible(true);
        });
    }
    
    @Override
    public void onDisable() {
        if (this.window != null) {
            this.window.setVisible(false);
        }
        this.window = null;
    }
    
    @Override
    public void onLogout() {
        if (this.window != null) {
            final ArrayList<String> drawInfo = new ArrayList<String>();
            drawInfo.add("Phobos v1.9.0");
            drawInfo.add("");
            drawInfo.add("No Connection.");
            this.window.setToDraw(drawInfo);
        }
    }
    
    @Override
    public void onUnload() {
        this.disable();
    }
    
    @Override
    public void onLoad() {
        this.disable();
    }
    
    @Override
    public void onUpdate() {
        if (this.window != null) {
            final ArrayList<String> drawInfo = new ArrayList<String>();
            drawInfo.add("Phobos v1.9.0");
            drawInfo.add("");
            drawInfo.add("Fps: " + Minecraft.debugFPS);
            drawInfo.add("TPS: " + Phobos.serverManager.getTPS());
            drawInfo.add("Ping: " + Phobos.serverManager.getPing() + "ms");
            drawInfo.add("Speed: " + Phobos.speedManager.getSpeedKpH() + "km/h");
            drawInfo.add("Time: " + new SimpleDateFormat("h:mm a").format(new Date()));
            final boolean inHell = StreamerMode.mc.world.getBiome(StreamerMode.mc.player.getPosition()).getBiomeName().equals("Hell");
            final int posX = (int)StreamerMode.mc.player.posX;
            final int posY = (int)StreamerMode.mc.player.posY;
            final int posZ = (int)StreamerMode.mc.player.posZ;
            final float nether = inHell ? 8.0f : 0.125f;
            final int hposX = (int)(StreamerMode.mc.player.posX * nether);
            final int hposZ = (int)(StreamerMode.mc.player.posZ * nether);
            final String coordinates = "XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
            final String text = Phobos.rotationManager.getDirection4D(false);
            drawInfo.add("");
            drawInfo.add(text);
            drawInfo.add(coordinates);
            drawInfo.add("");
            for (final Module module : Phobos.moduleManager.sortedModules) {
                final String moduleName = TextUtil.stripColor(module.getFullArrayString());
                drawInfo.add(moduleName);
            }
            drawInfo.add("");
            for (final PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
                final String potionText = TextUtil.stripColor(Phobos.potionManager.getColoredPotionString(effect));
                drawInfo.add(potionText);
            }
            drawInfo.add("");
            final Map<String, Integer> map = EntityUtil.getTextRadarPlayers();
            if (!map.isEmpty()) {
                for (final Map.Entry<String, Integer> player : map.entrySet()) {
                    final String playerText = TextUtil.stripColor(player.getKey());
                    drawInfo.add(playerText);
                }
            }
            this.window.setToDraw(drawInfo);
        }
    }
    
    public class SecondScreenFrame extends JFrame
    {
        private SecondScreen panel;
        
        public SecondScreenFrame() {
            this.initUI();
        }
        
        private void initUI() {
            this.add(this.panel = new SecondScreen());
            this.setResizable(true);
            this.pack();
            this.setTitle("Phobos - Info");
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(2);
        }
        
        public void setToDraw(final ArrayList<String> list) {
            this.panel.setToDraw(list);
        }
    }
    
    public class SecondScreen extends JPanel
    {
        private final int B_WIDTH;
        private final int B_HEIGHT;
        private Font font;
        private ArrayList<String> toDraw;
        
        public void setToDraw(final ArrayList<String> list) {
            this.toDraw = list;
            this.repaint();
        }
        
        @Override
        public void setFont(final Font font) {
            this.font = font;
        }
        
        public SecondScreen() {
            this.B_WIDTH = StreamerMode.this.width.getValue();
            this.B_HEIGHT = StreamerMode.this.height.getValue();
            this.font = new Font("Verdana", 0, 20);
            this.toDraw = new ArrayList<String>();
            this.initBoard();
        }
        
        public void setWindowSize(final int width, final int height) {
            this.setPreferredSize(new Dimension(width, height));
        }
        
        private void initBoard() {
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.setPreferredSize(new Dimension(this.B_WIDTH, this.B_HEIGHT));
        }
        
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            this.drawScreen(g);
        }
        
        private void drawScreen(final Graphics g) {
            final Font small = this.font;
            final FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(small);
            int y = 40;
            for (final String msg : this.toDraw) {
                g.drawString(msg, (this.getWidth() - metr.stringWidth(msg)) / 2, y);
                y += 20;
            }
            Toolkit.getDefaultToolkit().sync();
        }
    }
}

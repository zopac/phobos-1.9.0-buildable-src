package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.*;

public class BaritoneNoStop extends Command
{
    public BaritoneNoStop() {
        super("noStop", new String[] { "<prefix>", "<x>", "<y>", "<z>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 5) {
            Phobos.baritoneManager.setPrefix(commands[0]);
            int x = 0;
            int y = 0;
            int z = 0;
            try {
                x = Integer.parseInt(commands[1]);
                y = Integer.parseInt(commands[2]);
                z = Integer.parseInt(commands[3]);
            }
            catch (NumberFormatException e) {
                Command.sendMessage("Invalid Input for x, y or z!");
                Phobos.baritoneManager.stop();
                return;
            }
            Phobos.baritoneManager.start(x, y, z);
            return;
        }
        Command.sendMessage("Stoping Baritone-Nostop.");
        Phobos.baritoneManager.stop();
    }
}

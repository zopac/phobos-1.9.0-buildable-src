package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.*;
import org.lwjgl.input.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.features.modules.*;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", new String[] { "<module>", "<bind>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("Please specify a module.");
            return;
        }
        final String rkey = commands[1];
        final String moduleName = commands[0];
        final Module module = Phobos.moduleManager.getModuleByName(moduleName);
        if (module == null) {
            Command.sendMessage("Unknown module '" + module + "'!");
            return;
        }
        if (rkey == null) {
            Command.sendMessage(module.getName() + " is bound to &b" + module.getBind().toString());
            return;
        }
        int key = Keyboard.getKeyIndex(rkey.toUpperCase());
        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        if (key == 0) {
            Command.sendMessage("Unknown key '" + rkey + "'!");
            return;
        }
        module.bind.setValue(new Bind(key));
        Command.sendMessage("Bind for &b" + module.getName() + "&r set to &b" + rkey.toUpperCase());
    }
}

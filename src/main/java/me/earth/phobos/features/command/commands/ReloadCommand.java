package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.*;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        Phobos.reload();
    }
}

/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.systems.commands;

import org.bukkit.command.CommandSender;

public class MonkeyCommand extends Command {

    public MonkeyCommand(){
        super("monkey");
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        sendMessage(sender, "https://www.youtube.com/watch?v=8CemskuOg2g");
    }
}

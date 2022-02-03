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

package com.egirlsnation.swissknife.listeners.player;

import com.egirlsnation.swissknife.utils.entity.player.HeadsUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onPlayerDeath implements Listener {

    private final HeadsUtil headsUtil = new HeadsUtil();

    @EventHandler
    private void PlayerDeath(PlayerDeathEvent e){
        if(e.getEntity().getKiller() == null) return;
        if(headsUtil.isAncientOrDraconite(e.getEntity().getKiller().getInventory().getItemInMainHand())){
            headsUtil.dropHeadIfLucky(e.getEntity(), e.getEntity().getKiller(), e.getEntity().getKiller().getInventory().getItemInMainHand());
        }
    }
}

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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.entity.EntityUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

public class onXpBottle implements Listener {

    private final EntityUtil entityUtil = new EntityUtil();

    @EventHandler
    public void xpBottleListener(ExpBottleEvent e){
        if(!OldConfig.instance.preventXpBottleLag) return;
        if(entityUtil.countEntities(EntityType.THROWN_EXP_BOTTLE, e.getEntity().getLocation().getChunk().getEntities()) > OldConfig.instance.xpBottleLimit){
            e.setCancelled(true);
            e.getEntity().remove();
        }
    }
}

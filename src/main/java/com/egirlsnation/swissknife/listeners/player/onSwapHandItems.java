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

import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.entity.player.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.HashMap;

public class onSwapHandItems implements Listener {

    public static final HashMap<Player, Long> handSwapDelay = new HashMap<>();

    @EventHandler
    private void SwapHandItems(PlayerSwapHandItemsEvent e){
        if(e.getOffHandItem() != null){
            if(e.getOffHandItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                if(e.getOffHandItem().getAmount() > OldConfig.instance.maxTotemStack){
                    e.getOffHandItem().setAmount(OldConfig.instance.maxTotemStack);
                }
            }
        }

        if(e.getMainHandItem() != null){
            if(e.getMainHandItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                if(e.getMainHandItem().getAmount() > OldConfig.instance.maxTotemStack){
                    e.getMainHandItem().setAmount(OldConfig.instance.maxTotemStack);
                }
            }
        }

        if(OldConfig.instance.handSwitchCrash){
            if(!handSwapDelay.containsKey(e.getPlayer())){
                handSwapDelay.put(e.getPlayer(), 0L);
            }
            if(System.currentTimeMillis() < handSwapDelay.get(e.getPlayer())){
                e.setCancelled(true);
                if(OldConfig.instance.kickOnHandSwitchCrash){
                    PlayerUtil.kickPlayer(e.getPlayer());
                }
            }else{
                handSwapDelay.put(e.getPlayer(), System.currentTimeMillis() + OldConfig.instance.handSwitchDelay);
            }
        }


    }
}

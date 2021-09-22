/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.listener.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.HashMap;

import static com.egirlsnation.swissknife.SwissKnife.Config.handSwitchCrash;
import static com.egirlsnation.swissknife.SwissKnife.Config.kickOnHandSwitchCrash;

public class onSwapHandItems implements Listener {

    public static final HashMap<Player, Long> handSwapDelay = new HashMap<>();

    @EventHandler
    private void SwapHandItems(PlayerSwapHandItemsEvent e){
        if(e.getOffHandItem() != null){
            if(e.getOffHandItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                if(e.getOffHandItem().getAmount() > 2){
                    e.getOffHandItem().setAmount(2);
                }
            }
        }

        if(e.getMainHandItem() != null){
            if(e.getMainHandItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                if(e.getMainHandItem().getAmount() > 2){
                    e.getMainHandItem().setAmount(2);
                }
            }
        }

        if(handSwitchCrash){
            if(handSwapDelay.containsKey(e.getPlayer())){
                handSwapDelay.put(e.getPlayer(), 0L);
            }
            if(System.currentTimeMillis() < handSwapDelay.get(e.getPlayer())){
                e.setCancelled(true);
                if(kickOnHandSwitchCrash){
                    e.getPlayer().kickPlayer("Lost connection to the server");
                }
            }else{
                handSwapDelay.put(e.getPlayer(), System.currentTimeMillis() + 750L);
            }
        }


    }
}

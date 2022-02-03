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

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrystalSpeedLimiter extends Module {

    public CrystalSpeedLimiter(){
        super(Categories.Player, "crystal-speed-limiter", "Limits how many crystals can player break per second");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
            .name("delay")
            .description("How many miliseconds before breaking another")
            .defaultValue(200)
            .build()
    );

    private final static Map<UUID, Long> crystalMap = new HashMap<>();

    @EventHandler
    private void onPlayerDamageEntity(EntityDamageByEntityEvent e){
        if(!isEnabled()) return;
        if(!e.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) return;
        if(!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();

        UUID uuid = player.getUniqueId();
        if(!crystalMap.containsKey(uuid)){
            crystalMap.put(uuid, System.currentTimeMillis());
        }else{
            long timeLeft = System.currentTimeMillis() - crystalMap.get(uuid);
            if(timeLeft < delay.get()){
                e.setCancelled(true);
            }else{
                crystalMap.put(uuid, System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        if(!isEnabled()) return;
        crystalMap.remove(e.getPlayer().getUniqueId());
    }
}

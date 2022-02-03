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

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class NetherRoofDisabler extends Module {
    public NetherRoofDisabler(){
        super(Categories.Player, "nether-roof-disabler", "Prevents players from going onto the nether roof");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> roofHeight = sgGeneral.add(new IntSetting.Builder()
            .name("roof-height")
            .description("The Y coordinate of the highest bedrock block of the nether roof")
            .defaultValue(127)
            .build()
    );

    private final Setting<Boolean> teleportDown = sgGeneral.add(new BoolSetting.Builder()
            .name("teleport-down")
            .description("If players should be teleported down")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> damagePlayer = sgGeneral.add(new BoolSetting.Builder()
            .name("deal-damage")
            .description("If players should get damaged when they're on the roof")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> damage = sgGeneral.add(new IntSetting.Builder()
            .name("damage")
            .description("How much damage should the player get (ignores armor)")
            .defaultValue(1)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should tell the player he can't go on the nether roof")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "You cannot go on the nether roof")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("log")
            .description("If the plugin should log when player attempts to go on the nether roof")
            .defaultValue(true)
            .build()
    );


    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        if(!isEnabled()) return;

        Location l = e.getTo();
        if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if(l.getY() >= roofHeight.get()){
            e.setCancelled(true);

            if(log.get()) info("Player " + e.getPlayer().getName() + " attempted to go above the nether roof");

            if(teleportDown.get()){
                e.getPlayer().teleport(e.getPlayer().getLocation().subtract(0, 3, 0));
            }

            if(alertPlayers.get()){
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
            }

            if(damagePlayer.get()){
                if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                    e.getPlayer().setHealth(e.getPlayer().getHealth() - damage.get());
                }
            }
        }

    }

    @EventHandler
    public void PlayerTeleport(PlayerTeleportEvent e){
        if(!isEnabled()) return;


        Location l = e.getTo();
        if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

        if(l.getBlockY() >= roofHeight.get()){
            e.setCancelled(true);

            if(log.get()) info("Player " + e.getPlayer().getName() + " attempted to go above the nether roof");

            if(teleportDown.get()){
                e.getPlayer().teleport(e.getPlayer().getLocation().subtract(0, 3, 0));
            }

            if(alertPlayers.get()){
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
            }

            if(damagePlayer.get()){
                if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                    e.getPlayer().setHealth(e.getPlayer().getHealth() - damage.get());
                }
            }
        }
    }

    @EventHandler
    public void VehicleEnter(VehicleEnterEvent e){
        if(!isEnabled()) return;

        if(e.getEntered() instanceof Player){
            Location l = e.getVehicle().getLocation();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

            if(l.getBlockY() >= roofHeight.get()){
                e.setCancelled(true);
                e.getVehicle().remove();

                if(alertPlayers.get()){
                    e.getEntered().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
                }

                if(log.get()) info("Player " + e.getEntered().getName() + " attempted to go above the nether roof");

                if(damagePlayer.get()){
                    Player player = (Player) e.getEntered();
                    if(!player.getGameMode().equals(GameMode.CREATIVE)){
                        player.setHealth(player.getHealth() - damage.get());
                    }
                }
            }
        }
    }

    @EventHandler
    public void VehicleExit(VehicleExitEvent e){
        if(!isEnabled()) return;

        if(e.getExited() instanceof Player){
            Location l = e.getVehicle().getLocation();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

            if(l.getBlockY() >= OldConfig.instance.netherRoofHeight){
                e.setCancelled(true);
                e.getVehicle().remove();

                if(alertPlayers.get()){
                    e.getExited().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
                }

                if(log.get()) info("Player " + e.getExited().getName() + " attempted to go above the nether roof");

                if(damagePlayer.get()){
                    if(!((Player) e.getExited()).getGameMode().equals(GameMode.CREATIVE)){
                        e.getExited().setHealth(e.getExited().getHealth() - damage.get());
                    }
                }
            }
        }
    }
}

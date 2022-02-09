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

package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.listeners.block.onBlockDispense;
import com.egirlsnation.swissknife.listeners.block.onBlockPlace;
import com.egirlsnation.swissknife.listeners.entity.*;
import com.egirlsnation.swissknife.listeners.inventory.onCraftItemEvent;
import com.egirlsnation.swissknife.listeners.inventory.onInventoryClick;
import com.egirlsnation.swissknife.listeners.inventory.onInventoryClose;
import com.egirlsnation.swissknife.listeners.inventory.onInventoryOpen;
import com.egirlsnation.swissknife.listeners.player.*;
import com.egirlsnation.swissknife.systems.Systems;
import com.egirlsnation.swissknife.systems.commands.*;
import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.sql.MySQL;
import com.egirlsnation.swissknife.systems.sql.SqlQuery;
import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.SwissLogger;
import com.egirlsnation.swissknife.utils.server.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SwissKnife extends JavaPlugin {

    public static SwissKnife INSTANCE;
    public static SwissLogger swissLogger;

    public MySQL SQL;
    public SqlQuery sqlQuery;


    //Old code
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();
    //Old code end

    @Override
    public void onEnable() {
        if(INSTANCE == null){
            INSTANCE = this;
        }

        if(swissLogger == null){
            swissLogger = new SwissLogger();
        }

        swissLogger.info("Initializing SwissKnife");

        ServerUtil.init();

        Categories.init();

        Systems.init();

        Modules.get().sortModules();

        Systems.load();

        /*
        SwissLogger.info("Loading config handler.");
        Config.init(this);

        if (!Config.instance.webhookURL.isBlank()) {
            if (Bukkit.getServer().getTPS().length == 3) {
                discordUtil.setWebhookURL(Config.instance.webhookURL);
                discordUtil.setTpsArrSize(3);
                initTPSnotifyTask();
            } else if (Bukkit.getServer().getTPS().length == 4) {
                discordUtil.setWebhookURL(Config.instance.webhookURL);
                discordUtil.setTpsArrSize(4);
                initTPSnotifyTask();
            } else {
                SwissLogger.warning("You're running server software that's modifiying TPS results that's not supported by this plugin.\nTPS Notifications won't be sent.\nSupported server software for this feature is Spigot, PaperMC, Tuinity, Airplane, Purpur and Pufferfish");
            }
        }

        correctConfigValues();

        registerEvents();
        registerCommands();
        registerRecipes();

        initPluginHooks();
        initSQL();
         */
    }

    @Override
    public void onDisable() {
        Systems.save();
        swissLogger.info("Swiss Knife plugin disabled.");
    }

    private void registerEvents() {
        pluginManager.registerEvents(new CommandPreProcessor(this), this);
        swissLogger.info("Registering block events");
        pluginManager.registerEvents(new onBlockDispense(), this);
        pluginManager.registerEvents(new onBlockPlace(), this);

        swissLogger.info("Registering entity events");
        pluginManager.registerEvents(new onEntityChangeBlock(), this);
        pluginManager.registerEvents(new onEntityChangeBlock(), this);
        pluginManager.registerEvents(new onEntityDamage(), this);
        pluginManager.registerEvents(new onEntityDamageByBlock(), this);
        pluginManager.registerEvents(new onEntityDamageByEntity(), this);
        pluginManager.registerEvents(new onEntityDeath(), this);
        pluginManager.registerEvents(new onEntityPickupItem(), this);
        pluginManager.registerEvents(new onEntityPortalTeleport(), this);
        pluginManager.registerEvents(new onCreatureSpawn(), this);
        pluginManager.registerEvents(new onProjectileHit(), this);

        swissLogger.info("Registering inventory events");
        pluginManager.registerEvents(new onInventoryClick(), this);
        pluginManager.registerEvents(new onInventoryClose(), this);
        pluginManager.registerEvents(new onInventoryOpen(), this);

        swissLogger.info("Registering player events");
        pluginManager.registerEvents(new onGamemodeSwitch(this), this);
        pluginManager.registerEvents(new onJoin(this), this);
        pluginManager.registerEvents(new onLeave(this), this);
        pluginManager.registerEvents(new onPlayerDeath(), this);
        pluginManager.registerEvents(new onPlayerInteract(this), this);
        pluginManager.registerEvents(new onPlayerInteractEntity(), this);
        pluginManager.registerEvents(new onRespawn(), this);
        pluginManager.registerEvents(new onPlayerPlaceCrystal(), this);
        pluginManager.registerEvents(new onSwapHandItems(), this);
        pluginManager.registerEvents(new EnderCrystalListeners(this), this);
        pluginManager.registerEvents(new onVehicleCreate(), this);
        pluginManager.registerEvents(new onVehicleCollision(), this);
        pluginManager.registerEvents(new onPlayerMove(), this);
        pluginManager.registerEvents(new onPlayerTeleport(), this);
        pluginManager.registerEvents(new onCraftItemEvent(), this);
        pluginManager.registerEvents(new onProjectileLaunch(), this);
        pluginManager.registerEvents(new onPlayerChat(this), this);
    }

    private void registerCommands() { //TODO
        swissLogger.info("Registering commands.");
        Objects.requireNonNull(this.getCommand("kill")).setExecutor(new KillCommand(this));
        Objects.requireNonNull(this.getCommand("ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(this.getCommand("playtime")).setExecutor(new PlaytimeCommand(this));
        if(OldConfig.instance.enableShitlist) {
            Objects.requireNonNull(this.getCommand("shitlist")).setExecutor(new ShitListCommand(this));
        }
        Objects.requireNonNull(this.getCommand("shrug")).setExecutor(new ShrugCommand());
        Objects.requireNonNull(this.getCommand("refreshrank")).setExecutor(new RefreshRankCommand());
        Objects.requireNonNull(this.getCommand("monkey")).setExecutor(new MonkeyCommand());
        Objects.requireNonNull(this.getCommand("tpsalerttest")).setExecutor(new TpsAlertTestCommand(this));
        Objects.requireNonNull(this.getCommand("toggleitemability")).setExecutor(new ToggleItemAbilityCommand());
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    private void registerRecipes(){ //TODO
        swissLogger.info("Registering recipes");
        if(OldConfig.instance.enablePickaxeCraft){
            swissLogger.info("Registering draconite pickaxe recipe");
            NamespacedKey draconitePickKey = new NamespacedKey(this, "draconite_pickaxe");
            ShapedRecipe draconitePick = new ShapedRecipe(draconitePickKey, customItemHandler.getDraconitePickaxe())
                    .shape("GHG", " S ", " S ");
            if(OldConfig.instance.useDraconiteGems){
                draconitePick.setIngredient('G', Material.PLAYER_HEAD).setIngredient('H', Material.END_CRYSTAL);
            }else{
                draconitePick.setIngredient('G', Material.END_CRYSTAL).setIngredient('H', Material.DRAGON_HEAD);
            }
            if(OldConfig.instance.useBedrockSticks){
                draconitePick.setIngredient('S', Material.BEDROCK);
            }else{
                draconitePick.setIngredient('S', Material.STICK);
            }
            if(Bukkit.getRecipe(draconitePickKey) != null){
                Bukkit.removeRecipe(draconitePickKey);
            }
            Bukkit.addRecipe(draconitePick);
        }
    }
}

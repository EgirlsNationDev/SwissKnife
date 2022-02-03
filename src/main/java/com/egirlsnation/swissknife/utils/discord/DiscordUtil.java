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

package com.egirlsnation.swissknife.utils.discord;

import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscordUtil {

    private static int tpsArrSize;

    private static long lastAlertTimeSec;

    private static String webhookURL = "";


    public boolean shouldPostAlert(List<Double> tps){
        if(tps.get(0) <= OldConfig.instance.tpsNotifyAlways){
            return true;
        }
        if (((System.currentTimeMillis() /1000) - lastAlertTimeSec) < OldConfig.instance.notifyDelay) {
            return false;
        }
        if(tpsArrSize == 3){
            if(OldConfig.instance.longerTPSavg && tps.get(2) <= OldConfig.instance.tpsAvgThreshold){
                return true;
            }else return !OldConfig.instance.longerTPSavg && tps.get(1) <= OldConfig.instance.tpsAvgThreshold;
        }else{
            if(OldConfig.instance.longerTPSavg && tps.get(3) <= OldConfig.instance.tpsAvgThreshold){
                return true;
            }else return !OldConfig.instance.longerTPSavg && tps.get(2) <= OldConfig.instance.tpsAvgThreshold;
        }
    }

    public void setTpsArrSize(int size){
        tpsArrSize = size;
    }

    public void setWebhookURL(String URL){
        webhookURL = URL;
    }

    public void postDiscordTPSNotif(@NotNull List<Double> tps, int playercount, int maxSlots, List<String> playerDisplayNames, List<String> lowPtNames) throws IOException {
        DiscordWebhook webhook = new DiscordWebhook(webhookURL);
        if(OldConfig.instance.webhookName.isBlank()){
            webhook.setUsername("TPS Alert");
        }else{
            webhook.setUsername(OldConfig.instance.webhookName);
        }
        if(!OldConfig.instance.webhookAvatarURL.isBlank()){
            webhook.setAvatarUrl(OldConfig.instance.webhookAvatarURL);
        }
        if(!OldConfig.instance.roleIDs.isEmpty()){
            String pings = "";
            for(String id : OldConfig.instance.roleIDs){
                pings = pings + " <@&" + id +">";
            }
            webhook.setContent(pings);
        }

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject()
                .setTitle(":warning: Low TPS")
                .setDescription("Please keep in mind that this feature is still in testing.")
                .addField("Players online", playercount + "/" + maxSlots, false);
        if(tpsArrSize == 3){
            embed.addField("TPS (1m, 5m, 15m)", tps.stream().map(Object::toString).collect(Collectors.joining(", ")), false );
        }else{
            embed.addField("TPS (5s, 1m, 5m, 15m)", tps.stream().map(Object::toString).collect(Collectors.joining(", ")), false);
        }

        if(playerDisplayNames != null){
            if(playerDisplayNames.isEmpty()){
                embed.addField("Players", "No players online", false);
            }else{
                playerDisplayNames.forEach(ChatColor::stripColor);
                embed.addField("Players", playerDisplayNames.stream().map(Objects::toString).collect(Collectors.joining(", ")), false );
            }
        }
        if(lowPtNames != null){
            if(lowPtNames.isEmpty()){
                embed.addField("Low Playtime Players", "No low playtime players online", true);
            }else{
                embed.addField("Low Playtime Players", lowPtNames.stream().map(Objects::toString).collect(Collectors.joining(", ")), false );
            }
        }
        embed.setColor(Color.red);
        webhook.addEmbed(embed);
        webhook.execute();
        lastAlertTimeSec = System.currentTimeMillis() / 1000;
    }
}

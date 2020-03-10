package me.matthewe.atherial.narcos.narcosdrugs.drug.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.event.AtherialListener;
import net.atherial.api.plugin.utilities.GenericUtils;

/**
 * Created by Matthew E on 6/9/2019 at 2:27 PM for the project NarcosDrugs
 */
public class TestListener implements AtherialListener {

    public TestListener() {

    }

    @Override
    public void setup(AtherialEventListener eventListener) {

        eventListener.onChat(event -> {
            if (GenericUtils.isInteger(event.getMessage())) {
                int integer = GenericUtils.getInteger(event.getMessage());

                if (integer>0){
                    DrugPlayer.get(event.getPlayer()).addExperience(event.getPlayer(),integer);
                }
            }

        });
    }
}

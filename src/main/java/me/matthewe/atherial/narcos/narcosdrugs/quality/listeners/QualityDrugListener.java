package me.matthewe.atherial.narcos.narcosdrugs.quality.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.drug.events.DrugCreationEvent;
import me.matthewe.atherial.narcos.narcosdrugs.quality.QualityHandler;
import net.atherial.api.event.AtherialEventListener;

/**
 * Created by Matthew E on 6/20/2019 at 11:48 PM for the project NarcosDrugs
 */
public class QualityDrugListener implements net.atherial.api.event.AtherialListener {
    private QualityHandler qualityHandler;

    public QualityDrugListener() {
        this.qualityHandler = QualityHandler.get();
    }

    @Override
    public void setup(AtherialEventListener eventListener) {

        eventListener.listen((DrugCreationEvent event) -> {
            qualityHandler.onDrugCreate(event.getDrug());
        });
    }
}

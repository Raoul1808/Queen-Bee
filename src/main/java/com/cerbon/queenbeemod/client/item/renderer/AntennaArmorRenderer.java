package com.cerbon.queenbeemod.client.item.renderer;

import com.cerbon.queenbeemod.client.item.model.AntennaArmorModel;
import com.cerbon.queenbeemod.item.custom.AntennaArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AntennaArmorRenderer extends GeoArmorRenderer<AntennaArmorItem> {
    public AntennaArmorRenderer() {
        super(new AntennaArmorModel());
    }
}

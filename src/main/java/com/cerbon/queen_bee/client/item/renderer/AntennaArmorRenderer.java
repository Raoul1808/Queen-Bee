package com.cerbon.queen_bee.client.item.renderer;

import com.cerbon.queen_bee.client.item.model.AntennaArmorModel;
import com.cerbon.queen_bee.item.custom.AntennaArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AntennaArmorRenderer extends GeoArmorRenderer<AntennaArmorItem> {
    public AntennaArmorRenderer() {
        super(new AntennaArmorModel());
    }
}

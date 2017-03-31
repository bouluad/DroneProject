package istic.fr.droneproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.fr.droneproject.R;

public enum TypePoint {

    eau(R.drawable.eau, "eau"),
    eau_np(R.drawable.eau_np, "eau_np"),
    ps_eau(R.drawable.ps_eau, "ps_eau"),
    ps_hu(R.drawable.ps_hu, "ps_hu"),
    ps_in(R.drawable.ps_in, "ps_in"),
    ps_rp(R.drawable.ps_rp, "ps_rp"),
    sd_eau(R.drawable.sd_eau, "sd_eau"),
    sd_hu(R.drawable.sd_hu, "sd_hu"),
    sd_in(R.drawable.sd_in, "sd_in"),
    sd_rp(R.drawable.sd_rp, "sd_rp");

    TypePoint(int drawable, String name) {
        this.drawable = drawable;
        this.name = name;
    }

    private int drawable;
    private String name;

    public static List<TypePoint> getAll() {
        List<TypePoint> all = new ArrayList<>();
        Collections.addAll(all, TypePoint.values());
        return all;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

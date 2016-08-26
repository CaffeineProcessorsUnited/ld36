package de.caffeineaddicted.ld36;

import de.caffeineaddicted.sgl.Attribute;

/**
 * @author Malte Heinzelmann
 */
public class AttributeList {
    public final static Attribute<Integer> WIDTH = new Attribute<Integer>(Integer.class, "width", 800);
    public final static Attribute<Integer> HEIGHT = new Attribute<Integer>(Integer.class, "height", 600);
    public final static Attribute<Boolean> RESIZABLE = new Attribute<Boolean>(Boolean.class, "resizable", true);
}
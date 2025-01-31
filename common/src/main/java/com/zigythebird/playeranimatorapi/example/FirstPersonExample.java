package com.zigythebird.playeranimatorapi.example;

import com.google.gson.JsonObject;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import net.minecraft.resources.ResourceLocation;

public class FirstPersonExample {
    public static final CommonModifier FIRST_PERSON_MODIFIER;

    static {
        JsonObject data = new JsonObject();
        //Look at FirstPersonMode class of the player animator mod for available options.
        //Must use the exact same name as enum, all caps.
        data.addProperty("firstPersonMode", "THIRD_PERSON_MODEL");

        //This would show your right arm in first person.
        //data.addProperty("showRightArm", true);

        //This would show your left arm in first person.
        //data.addProperty("showLeftArm", true);

        //This would stop showing the right hand item in first person.
        //data.addProperty("showRightItem", false);

        //This would stop showing the left hand item in first person.
        //data.addProperty("showLeftItem", false);

        FIRST_PERSON_MODIFIER = new CommonModifier(ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "firstperson"), data);
    }
}

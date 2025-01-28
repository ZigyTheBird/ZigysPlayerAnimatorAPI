package com.zigythebird.playeranimatorapi.modifier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CommonModifier<T extends AbstractModifier> {

    public final T modifier;
    public final ResourceLocation ID;
    public final JsonObject data;

    public static Gson gson = new Gson();
    public static final StreamCodec<FriendlyByteBuf, CommonModifier> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CommonModifier decode(FriendlyByteBuf object) {
            List<String> data = object.readList(FriendlyByteBuf::readUtf);

            if (data.size() == 2) {
                return new CommonModifier(ResourceLocation.parse(data.get(0)), gson.fromJson(data.get(1), TypeToken.get(JsonObject.class)));
            } else if (data.size() == 1) {
                return new CommonModifier(ResourceLocation.parse(data.get(0)), null);
            }
            return nullModifer;
        }

        @Override
        public void encode(FriendlyByteBuf object, CommonModifier modifier) {
            List<String> list = new ArrayList<>();
            if (modifier != null) {
                list.add(modifier.ID.toString());
                if (modifier.data != null) {
                    list.add(modifier.data.toString());
                }
            }
            object.writeCollection(list, FriendlyByteBuf::writeUtf);
        }
    };

    public static final CommonModifier nullModifer = new CommonModifier(null, null);
    public static final List<CommonModifier> emptyList = new ArrayList<>();

    public CommonModifier(ResourceLocation ID, JsonObject json) {
        this.ID = ID;
        this.data = json;
        this.modifier = null;
    }

    public CommonModifier(T modifier) {
        this.modifier = modifier;
        this.ID = null;
        this.data = null;
    }
}

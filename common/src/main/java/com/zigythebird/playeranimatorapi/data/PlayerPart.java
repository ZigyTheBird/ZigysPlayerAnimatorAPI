package com.zigythebird.playeranimatorapi.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public class PlayerPart {

    public static StreamCodec<FriendlyByteBuf, PlayerPart> STEAM_CODEC = new StreamCodec<>() {
        @Override
        public PlayerPart decode(FriendlyByteBuf object) {
            return PlayerPart.readFromList(object.readList(FriendlyByteBuf::readBoolean));
        }

        @Override
        public void encode(FriendlyByteBuf object, PlayerPart object2) {
            object.writeCollection(PlayerPart.toList(object2), FriendlyByteBuf::writeBoolean);
        }
    };

    public boolean x = true;
    public boolean y = true;
    public boolean z = true;
    public boolean pitch = true;
    public boolean yaw = true;
    public boolean roll = true;
    public boolean bend = true;
    public boolean bendDirection = true;
    public boolean isVisible = true;

    public PlayerPart() {
    }

    public void setEnabled(boolean enabled) {
        x = enabled;
        y = enabled;
        z = enabled;
        pitch = enabled;
        yaw = enabled;
        roll = enabled;
        bend = enabled;
        bendDirection = enabled;
    }

    public void setX(boolean x) {
        this.x = x;
    }

    public void setY(boolean y) {
        this.y = y;
    }

    public void setZ(boolean z) {
        this.z = z;
    }

    public void setPitch(boolean pitch) {
        this.pitch = pitch;
    }

    public void setYaw(boolean yaw) {
        this.yaw = yaw;
    }

    public void setRoll(boolean roll) {
        this.roll = roll;
    }

    public void setBend(boolean bend) {
        this.bend = bend;
    }

    public void setBendDirection(boolean bendDirection) {
        this.bendDirection = bendDirection;
    }

    public void isVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public static List<Boolean> toList(PlayerPart part) {
        List<Boolean> list = new ArrayList<>();
        list.add(part.x);
        list.add(part.y);
        list.add(part.z);
        list.add(part.pitch);
        list.add(part.yaw);
        list.add(part.roll);
        list.add(part.bend);
        list.add(part.bendDirection);
        list.add(part.isVisible);
        return list;
    }

    public static PlayerPart readFromList(List<Boolean> list) {
        PlayerPart part = new PlayerPart();
        try {
            part.setX(list.get(0));
            part.setY(list.get(1));
            part.setZ(list.get(2));
            part.setPitch(list.get(3));
            part.setYaw(list.get(4));
            part.setRoll(list.get(5));
            part.setBend(list.get(6));
            part.setBendDirection(list.get(7));
            part.isVisible(list.get(8));
            return part;
        } catch (IndexOutOfBoundsException e) {
            return part;
        }
    }
}

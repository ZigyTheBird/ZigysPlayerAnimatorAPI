package zigy.playeranimatorapi.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.ArrayList;
import java.util.List;

public class PlayerPart {

    public static Codec<PlayerPart> CODEC = Codec.list(Codec.BOOL).comapFlatMap(PlayerPart::readFromList, PlayerPart::toList).stable();

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
        return list;
    }

    public static DataResult<PlayerPart> readFromList(List<Boolean> list) {
        PlayerPart part = new PlayerPart();
        part.setX(list.get(0));
        part.setY(list.get(1));
        part.setZ(list.get(2));
        part.setPitch(list.get(3));
        part.setYaw(list.get(4));
        part.setRoll(list.get(5));
        part.setBend(list.get(6));
        part.setBendDirection(list.get(7));
        return DataResult.success(part);
    }

    public boolean x = true;
    public boolean y = true;
    public boolean z = true;
    public boolean pitch = true;
    public boolean yaw = true;
    public boolean roll = true;
    public boolean bend = true;
    public boolean bendDirection = true;

    public PlayerPart() {}

    public void fromInt(int n) {
        if (n > 255) {
            n -= 256;
        }
        if (n < 0) {
            this.setEnabled(true);
            return;
        }

        String binary2 = String.format("%8s", Integer.toBinaryString(n));
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < binary2.length(); i++) {
            list.add(binary2.charAt(i) == '1');
        }

        this.setX(list.get(0));
        this.setY(list.get(1));
        this.setZ(list.get(2));
        this.setPitch(list.get(3));
        this.setRoll(list.get(4));
        this.setYaw(list.get(5));
        this.setBend(list.get(6));
        this.setBendDirection(list.get(7));
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
}

package zigy.playeranimatorapi.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import zigy.playeranimatorapi.utils.ModMath;

import java.util.ArrayList;
import java.util.List;

public class PlayerParts {

    public static Codec<PlayerParts> CODEC = Codec.list(PlayerPart.CODEC).comapFlatMap(PlayerParts::readFromList, PlayerParts::toList).stable();

    public static DataResult<PlayerParts> readFromList(List<PlayerPart> list) {
        if (list.size() != 7) {
            return DataResult.success(allEnabled);
        }

        PlayerParts parts = new PlayerParts();
        parts.body = list.get(0);
        parts.head = list.get(1);
        parts.torso = list.get(2);
        parts.rightArm = list.get(3);
        parts.leftArm = list.get(4);
        parts.rightLeg = list.get(5);
        parts.leftLeg = list.get(6);
        return DataResult.success(parts);
    }

    public static List<PlayerPart> toList(PlayerParts parts) {
        List<PlayerPart> list = new ArrayList<>();

        if (parts == null) {
            return list;
        }

        list.add(parts.body);
        list.add(parts.head);
        list.add(parts.torso);
        list.add(parts.rightArm);
        list.add(parts.leftArm);
        list.add(parts.rightLeg);
        list.add(parts.leftLeg);
        return list;
    }

    public static PlayerParts fromInt(int n) {
        List<Integer> integers = List.of(ModMath.getDigits(n));
        if (integers.size() != 14) {
            return allEnabled;
        }

        PlayerParts parts = new PlayerParts();

        List<Integer> temp = new ArrayList<>();
        for (int i = 0 ; i + 1 < integers.size() && i % 2 == 0; i++) {
            temp.add(Integer.valueOf(String.valueOf(integers.get(i)) + integers.get(i + 1)));
        }
        integers = temp;

        parts.body.fromInt(integers.get(0));
        parts.head.fromInt(integers.get(1));
        parts.torso.fromInt(integers.get(2));
        parts.rightArm.fromInt(integers.get(3));
        parts.leftArm.fromInt(integers.get(4));
        parts.rightLeg.fromInt(integers.get(5));
        parts.leftLeg.fromInt(integers.get(6));

        return parts;
    }

    public static final PlayerParts allEnabled = new PlayerParts();

    public PlayerPart body = new PlayerPart();
    public PlayerPart head = new PlayerPart();
    public PlayerPart torso = new PlayerPart();
    public PlayerPart rightArm = new PlayerPart();
    public PlayerPart leftArm = new PlayerPart();
    public PlayerPart rightLeg = new PlayerPart();
    public PlayerPart leftLeg = new PlayerPart();

    public PlayerParts() {}
}

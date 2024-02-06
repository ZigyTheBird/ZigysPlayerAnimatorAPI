package zigy.playeranimatorapi.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PlayerParts {

    public static Codec<PlayerParts> CODEC = Codec.list(PlayerPart.CODEC).comapFlatMap(PlayerParts::readFromList, PlayerParts::toList).stable();

    public static DataResult<PlayerParts> readFromList(List<PlayerPart> list) {
        if (list.size() != 9) {
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
        parts.rightItem = list.get(7);
        parts.leftItem = list.get(8);
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
        list.add(parts.rightItem);
        list.add(parts.leftItem);
        return list;
    }

    public static PlayerParts fromBigInteger(BigInteger n) {
        PlayerParts parts = new PlayerParts();

        try {
            String val = new BigInteger(n.toString(Character.MAX_RADIX), Character.MAX_RADIX).toString(2);

            List<Boolean> list = new ArrayList<>();
            for (int i = 0; i < val.length(); i++) {
                list.add(val.charAt(i) == '1');
            }

            try {
                //This part is pretty repetitive, so I actually got ChatGPT to help me with it which was quite a cool experience!
                parts.body.x = list.get(0);
                parts.body.y = list.get(1);
                parts.body.z = list.get(2);
                parts.body.pitch = list.get(3);
                parts.body.yaw = list.get(4);
                parts.body.roll = list.get(5);

                parts.head.x = list.get(9);
                parts.head.y = list.get(10);
                parts.head.z = list.get(11);
                parts.head.pitch = list.get(12);
                parts.head.yaw = list.get(13);
                parts.head.roll = list.get(14);

                parts.torso.x = list.get(18);
                parts.torso.y = list.get(19);
                parts.torso.z = list.get(20);
                parts.torso.pitch = list.get(21);
                parts.torso.yaw = list.get(22);
                parts.torso.roll = list.get(23);

                parts.rightArm.x = list.get(27);
                parts.rightArm.y = list.get(28);
                parts.rightArm.z = list.get(29);
                parts.rightArm.pitch = list.get(30);
                parts.rightArm.yaw = list.get(31);
                parts.rightArm.roll = list.get(32);
                parts.rightArm.bend = list.get(33);
                parts.rightArm.bendDirection = list.get(34);

                parts.leftArm.x = list.get(36);
                parts.leftArm.y = list.get(37);
                parts.leftArm.z = list.get(38);
                parts.leftArm.pitch = list.get(39);
                parts.leftArm.yaw = list.get(40);
                parts.leftArm.roll = list.get(41);
                parts.leftArm.bend = list.get(42);
                parts.leftArm.bendDirection = list.get(43);

                parts.rightLeg.x = list.get(45);
                parts.rightLeg.y = list.get(46);
                parts.rightLeg.z = list.get(47);
                parts.rightLeg.pitch = list.get(48);
                parts.rightLeg.yaw = list.get(49);
                parts.rightLeg.roll = list.get(50);
                parts.rightLeg.bend = list.get(51);
                parts.rightLeg.bendDirection = list.get(52);

                parts.leftLeg.x = list.get(54);
                parts.leftLeg.y = list.get(55);
                parts.leftLeg.z = list.get(56);
                parts.leftLeg.pitch = list.get(57);
                parts.leftLeg.yaw = list.get(58);
                parts.leftLeg.roll = list.get(59);
                parts.leftLeg.bend = list.get(60);
                parts.leftLeg.bendDirection = list.get(61);

                parts.rightItem.x = list.get(63);
                parts.rightItem.y = list.get(64);
                parts.rightItem.z = list.get(65);
                parts.rightItem.pitch = list.get(66);
                parts.rightItem.yaw = list.get(67);
                parts.rightItem.roll = list.get(68);
                parts.rightItem.bend = list.get(69);
                parts.rightItem.bendDirection = list.get(70);

                parts.leftItem.x = list.get(72);
                parts.leftItem.y = list.get(73);
                parts.leftItem.z = list.get(74);
                parts.leftItem.pitch = list.get(75);
                parts.leftItem.yaw = list.get(76);
                parts.leftItem.roll = list.get(77);
                parts.leftItem.bend = list.get(78);
                parts.leftItem.bendDirection = list.get(79);
            } catch (IndexOutOfBoundsException e) {
                return new PlayerParts();
            }
        } catch (NumberFormatException ignore) {
        }

        return parts;
    }

    public static final PlayerParts allEnabled = new PlayerParts();

    public static final PlayerParts allExceptHeadRot() {
        PlayerParts part = new PlayerParts();
        part.head.setPitch(false);
        part.head.setYaw(false);
        part.head.setRoll(false);
        return part;
    }

    public PlayerPart body = new PlayerPart();
    public PlayerPart head = new PlayerPart();
    public PlayerPart torso = new PlayerPart();
    public PlayerPart rightArm = new PlayerPart();
    public PlayerPart leftArm = new PlayerPart();
    public PlayerPart rightLeg = new PlayerPart();
    public PlayerPart leftLeg = new PlayerPart();
    public PlayerPart rightItem = new PlayerPart();
    public PlayerPart leftItem = new PlayerPart();

    public PlayerParts() {
    }
}

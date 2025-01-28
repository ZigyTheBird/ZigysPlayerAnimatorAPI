package com.zigythebird.playeranimatorapi.misc;

import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import dev.kosmx.playerAnim.core.util.Pair;

public interface PlayerInterface {
    void setLastAnim(PlayerAnimationData data);
    Pair<Integer, PlayerAnimationData> paapi$getLastAnim();
}

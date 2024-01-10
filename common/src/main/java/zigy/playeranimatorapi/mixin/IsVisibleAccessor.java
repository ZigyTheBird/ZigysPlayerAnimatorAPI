package zigy.playeranimatorapi.mixin;

public interface IsVisibleAccessor {

    default void zigysPlayerAnimatorAPI$setIsVisible(Boolean value) {}

    default boolean zigysPlayerAnimatorAPI$getIsVisible() {
        return true;
    }
}

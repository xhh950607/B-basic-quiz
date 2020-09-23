package com.example.quiz.validator;

import javax.validation.GroupSequence;

@GroupSequence({
        UserGroup.NameNotBlank.class,
        UserGroup.NameMaxBytes.class,
        UserGroup.AgeMin.class,
        UserGroup.AvatarNotBlank.class,
        UserGroup.AvatarMinBytes.class,
        UserGroup.AvatarMaxBytes.class,
        UserGroup.DescriptionMaxBytes.class
})
public interface UserGroup {
    interface NameNotBlank{};
    interface NameMaxBytes{};

    interface AgeMin{};

    interface AvatarNotBlank{};
    interface AvatarMinBytes{};
    interface AvatarMaxBytes{};

    interface DescriptionMaxBytes{};
}

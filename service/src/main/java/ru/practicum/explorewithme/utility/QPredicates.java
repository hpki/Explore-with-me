package ru.practicum.explorewithme.utility;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {

    private List<Predicate> predicateList = new ArrayList<>();

    public <T> QPredicates add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicateList.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicateList);
    }

    public static QPredicates builder() {
        return new QPredicates();
    }
}

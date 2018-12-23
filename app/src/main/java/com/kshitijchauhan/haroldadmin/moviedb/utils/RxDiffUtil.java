package com.kshitijchauhan.haroldadmin.moviedb.utils;

import androidx.recyclerview.widget.DiffUtil;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import kotlin.Pair;

import java.util.Collections;
import java.util.List;

public class RxDiffUtil {

    public static <T> ObservableTransformer<List<T>, Pair<List<T>, DiffUtil.DiffResult>> calculateDiff(
            BiFunction<List<T>, List<T>, DiffUtil.Callback> diffCallbacks)
    {
        Pair<List<T>, DiffUtil.DiffResult> initialPair = new Pair<>(Collections.emptyList(), null);
        return upstream -> upstream
                .scan(initialPair, (latestPair, nextItems) -> {
                    DiffUtil.Callback callback = diffCallbacks.apply(latestPair.component1(), nextItems);
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback, true);
                    return new Pair<>(nextItems, result);
                })
                .skip(1);  // downstream shouldn't receive seedPair.
    }
}
/*
 *  Created by Surajit Deka on 25/6/19 12:39 PM
 *  Copyright (c) Letstrack 2019 . All rights reserved.
 *  Last modified 27/4/19 2:38 PM
 *
 */

package com.technorapper.coreandroidmvvm.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class EventTask<T> {

    public final Status status;
    public final Task task;
    @Nullable
    public final T data;
    @Nullable
    public final String msg;


    private EventTask(Task task, Status status, @Nullable T data, String msg) {
        this.task = task;
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public static <T> EventTask<T> success(@NonNull T data, Task task) {
        return new EventTask<>(task, Status.SUCCESS, data, null);
    }

    public static <T> EventTask<T> error(Task task) {
        return new EventTask<>(task, Status.ERROR, null, null);
    }

    public static <T> EventTask<T> perform(@Nullable T data, Task task) {
        return new EventTask<>(task, Status.PERFORM, data, null);
    }

    public static <T> EventTask<T> loading(Task task) {
        return new EventTask<>(task, Status.LOADING, null, null);
    }


}

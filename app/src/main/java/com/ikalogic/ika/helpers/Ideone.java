package com.ikalogic.ika.helpers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Ideone {


    @RequiresApi(api = Build.VERSION_CODES.N)
    @FunctionalInterface
    public interface Lazy<T> extends Supplier<T> {
        abstract class Cache {
            private volatile static Map<Integer, Object> instances = new HashMap<>();

            private static synchronized Object getInstance(int instanceId, Supplier<Object> create) {

                Object instance = instances.get(instanceId);
                if (instance == null) {
                    synchronized (Cache.class) {
                        instance = instances.get(instanceId);
                        if (instance == null) {
                            instance = create.get();
                            instances.put(instanceId, instance);
                        }
                    }
                }
                return instance;
            }
        }

        @Override
        default T get() {
            return (T) Cache.getInstance(this.hashCode(), () -> init());
        }

        T init();
    }


    static Lazy<String> name1 = () -> {
        System.out.println("lazy init 1");
        return "name 1";
    };

    static Lazy<String> name2 = () -> {
        System.out.println("lazy init 2");
        return "name 2";
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main (String[] args) throws java.lang.Exception
    {
        System.out.println("start");
        System.out.println(name1.get());
        System.out.println(name1.get());
        System.out.println(name2.get());
        System.out.println(name2.get());
        System.out.println("end");
    }


}

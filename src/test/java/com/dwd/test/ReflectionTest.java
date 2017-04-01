package com.dwd.test;

import com.google.common.base.Preconditions;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.Reflection;
import com.google.common.reflect.TypeToken;

import com.sun.javafx.collections.MappingChange;
import org.apache.log4j.Logger;
import org.junit.Test;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjduanweidong on 2017/4/1.
 */
public class ReflectionTest {

    private static final Logger logger = Logger.getLogger(ReflectionTest.class);

    @Test
    public void test1() {
        TypeToken<Map> token = TypeToken.of(Map.class);
        print(token);
    }

    @Test
    public void test2() {
        TypeToken<Map<String, Integer>> token = new TypeToken<Map<String, Integer>>() {
        };
        print(token);
    }

    @Test
    public void test3() {
        TypeToken<Map<String, Integer>> token = new TypeToken<Map<String, Integer>>() {
        };
        TypeToken<?> typeToken = token.resolveType(Map.class.getTypeParameters()[0]);
        print(typeToken);
        logger.debug(Map.class.getTypeParameters()[0]);
        logger.debug(Map.class.getTypeParameters()[1]);
    }

    @Test
    public void test4() throws NoSuchMethodException {
        Invokable<List<String>, ?> invokable = new TypeToken<List<String>>() {
        }.method(List.class.getMethod("get", int.class));
        logger.debug(invokable.getReturnType());
    }

    @Test
    public void test5() {
        Runnable runnable = Reflection.newProxy(Runnable.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logger.debug("before invoke");
                Object result = method.invoke(new Runnable() {
                    @Override
                    public void run() {
                        logger.debug("run");
                    }
                }, args);
                logger.debug(proxy.getClass());
                logger.debug("invoke complete");
                return result;
            }
        });
        logger.debug(runnable.getClass());
        runnable.run();
    }

    public static void print( @Nullable TypeToken token) {
        Preconditions.checkNotNull(token,"token is null ");
        logger.debug("getType:" + token.getType());
        logger.debug("getType:getClass():" + token.getType().getClass());
        logger.debug("getRawType:" + token.getRawType());
        logger.debug("getRawType:getClass():" + token.getRawType().getClass());
//        logger.debug("getSubType:"+token.getSubtype(HashMap.class));
//        logger.debug("getSubType:getClass():"+token.getSubtype(HashMap.class).getClass());
//        logger.debug("getSuperType:"+token.getSupertype());
        logger.debug("getTypes:" + token.getTypes());
        logger.debug("getTypes:classes():" + token.getTypes().classes());
        logger.debug("getTypes:interfaces():" + token.getTypes().interfaces());
        logger.debug("isArray:" + token.isArray());
        logger.debug("getComponentType:" + token.getComponentType());
    }
}

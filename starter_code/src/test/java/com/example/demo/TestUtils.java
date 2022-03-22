package com.example.demo;

import java.lang.reflect.Field;


//From course
public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {
            Field field_Declared = target.getClass().getDeclaredField(fieldName);
            if (!field_Declared.isAccessible()){
                field_Declared.setAccessible(true);
                wasPrivate = true;
            }


            field_Declared.set(target, toInject);


            if (wasPrivate){
                field_Declared.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e){

            e.printStackTrace();

        }
    }

}

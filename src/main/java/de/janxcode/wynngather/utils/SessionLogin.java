package de.janxcode.wynngather.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.Field;

public class SessionLogin {
    public static void setNewSession() {
        Field declaredSessionField;
        Session session = new Session("janx", "a2bf5b6d82a648959130eeb05cbcd6f6", "eyJraWQiOiJhYzg0YSIsImFsZyI6IkhTMjU2In0.eyJ4dWlkIjoiMjUzNTQ2NDY3NDY1MzQwNSIsImFnZyI6IkFkdWx0Iiwic3ViIjoiZDJiMjdjMTAtM2ZiYS00ODAzLWExMTItZWFlZGQ1YjNhYzQxIiwiYXV0aCI6IlhCT1giLCJucyI6ImRlZmF1bHQiLCJyb2xlcyI6W10sImlzcyI6ImF1dGhlbnRpY2F0aW9uIiwicGxhdGZvcm0iOiJQQ19MQVVOQ0hFUiIsInl1aWQiOiI3ODk4NDAzYjg5NDE1MTI1Y2Q5MTE4ZjMxMGFmYTYxNSIsIm5iZiI6MTY3ODQwMjM5OCwiZXhwIjoxNjc4NDg4Nzk4LCJpYXQiOjE2Nzg0MDIzOTh9.HONhLlnhSxKyYpXlw49t5wjEfpA2_Ys1razc2cprXEM", "MICROSOFT");

        try {
            declaredSessionField = Minecraft.class.getDeclaredField("session");
            declaredSessionField.setAccessible(true);
            declaredSessionField.set(Minecraft.getMinecraft(), session);
            declaredSessionField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
}

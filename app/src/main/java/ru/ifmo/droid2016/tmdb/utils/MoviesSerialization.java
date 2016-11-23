package ru.ifmo.droid2016.tmdb.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Михайлов Никита on 19.11.2016.
 */

public final class MoviesSerialization {

    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutput out;
        byte[] result;
        try {
            out = new ObjectOutputStream(os);
            out.writeObject(object);
            out.flush();
            result = os.toByteArray();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                Log.d("Serialization", "Can't close output stream " + e.getMessage());
            }
        }
        return result;
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object result;
        try {
            in = new ObjectInputStream(is);
            result = in.readObject();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.d("Serialization", "Can't close input stream" + e.getMessage());
            }
        }
        return result;
    }
}
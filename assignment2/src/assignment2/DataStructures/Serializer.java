/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.DataStructures;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 *
 * @author Alvaro e Nelson
 */
public class Serializer {

    /**
     *
     * @param v Variables instance
     * @return v serialized in base64
     * @throws IOException required
     */
    public static String Serialize(Variables v) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjectOut = new ObjectOutputStream(byteArray)) {
            ObjectOut.writeObject(v);
        }
        return Base64.getEncoder().encodeToString(byteArray.toByteArray());
    }

    /**
     *
     * @param v variables instance serialized in base64
     * @return variable v instance
     * @throws IOException required
     * @throws ClassNotFoundException required
     */
    public static Variables Deserialize(String v) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(v);
        ObjectInputStream ObjectIn = new ObjectInputStream(new ByteArrayInputStream(data));
        Object obj = ObjectIn.readObject();
        return (Variables) obj;
    }
}

package com.server.draw.repository;

import com.server.draw.model.PaintingEntity;

import java.util.Hashtable;

public class PaintingRespository {


    private static Hashtable<String, PaintingEntity> paintings;

    static {
        paintings = new Hashtable<>();
    }

    public static PaintingEntity getPainting(String roomId)
    {
        return paintings.get(roomId);
    }


    synchronized public static void addPainting(String userId, String roomId, String targetName)
    {
        if(userId == null || roomId == null || targetName == null)
        {
            return;
        }
        PaintingEntity painting = new PaintingEntity(userId, roomId, targetName);
        paintings.put(roomId, painting);
    }

    synchronized public static void removePainting(String roomId)
    {
        if(roomId == null)
        {
            return;
        }
        paintings.remove(roomId);
    }

}

package com.server.draw.repository;

import com.server.draw.model.PlayerEntity;

import java.util.Hashtable;

// 记录了用户的正在进行的游戏的相关数据
// 可以通过用户id迅速找到房间号等游戏的即时信息
// TODO 它的方法只被包内的其它类调用
public class PlayerRepository {

    private static Hashtable<String, PlayerEntity> players;

    static {
        players = new Hashtable<>();
    }

    // 查询用户是否在玩家记录中（是否在进行游戏）
    public static boolean isPlaying(String userId) {
        return players.get(userId) != null;
    }

    public static PlayerEntity getPlayer(String userId) {
        return players.get(userId);
    }

    // 向玩家记录中加入一项
    public static void addPlayer(String userId, PlayerEntity player) {
        players.put(userId, player);
    }

    // 从玩家记录中删除某一项
    public static void removePlayer(String userId) {
        players.remove(userId);
    }


}

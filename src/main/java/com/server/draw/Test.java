//package com.server.draw;
//
//import com.server.draw.model.RoomEntity;
//import com.server.draw.repository.RoomRepository;
//
//public class Test {
//
//    public static void main(String[] args) {
//
//        RoomEntity room1 = new RoomEntity("room1", "xixi", "wehave");
//        RoomEntity room2 = new RoomEntity("room2", "xixi", "youhave");
//        RoomRepository.addRoom(room1.getRoomId(), room1);
//        System.out.println(RoomRepository.hasRoom("room1"));
//        RoomRepository.removeRoom("room1");
//        System.out.println(RoomRepository.hasRoom("room1"));
//        RoomRepository.addRoom("room1", room1);
//        RoomRepository.addRoom("room2", room2);
//        RoomRepository.addUserToRoom("user1", "room1");
//        RoomRepository.addUserToRoom("user2", "room1");
//        System.out.println(room1.getSize());
//
//    }
//}

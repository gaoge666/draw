package com.server.draw.controller;

import com.server.draw.Util.ControllerUtil;
import com.server.draw.Util.RoomNoUtil;
import com.server.draw.model.*;
import com.server.draw.repository.PlayerRepository;
import com.server.draw.repository.RoomRepository;
import com.server.draw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("room")
@RestController
public class RoomController {

    @Autowired
    private UserRepository userRepository;
    //用于转发数据(sendTo)
    private SimpMessagingTemplate template;

    @Autowired
    public RoomController(SimpMessagingTemplate t) {
        template = t;
    }
    /*
    创建房间
    @POST
    @Param: Room对象
    @Return：成功则返回room对象，失败则返回ERROR
     */
    @RequestMapping("create")
    public RInfo create(@RequestBody RoomEntity room)
    {
        RInfo rInfo = new RInfo();
//        room.setRoomId(ControllerUtil.genRoomId());
        room.setRoomId(RoomNoUtil.getRoomNo());
        if(userRepository.getByUserid(room.getUserId()) == null) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_DOES_NOT_EXIST");
            return rInfo;
        } else if(PlayerRepository.isPlaying(room.getUserId())) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_ALREADY_IN_ROOM");
            return rInfo;
        }
        RoomRepository.addRoom(room.getRoomId(), room);
        rInfo.setStatus("SUCCESS");
        rInfo.setInfo(room.getRoomId());
        return rInfo;
    }


    /*
    查找房间
    @GET
    @Param: 房间id
    @Return：成功则返回房间对象，失败则返回ERROR
     */
    @RequestMapping("find")
    public RInfo find(@RequestParam(name = "roomId") String roomId)
    {
        RInfo rInfo = new RInfo();
        if(!RoomRepository.hasRoom(roomId)) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
            return rInfo;
        }
        rInfo.setStatus("SUCCESS");
        rInfo.setInfo(RoomRepository.getRoom(roomId));
        return rInfo;
    }

    /*
    加入房间
    @POST
    @Param: 用户对象，加入房间id
    @Return：加入成功则返回SUCCESS,失败则返回ERROR
     */
    @RequestMapping("enter")
    public RInfo enter(@RequestParam(name = "roomId") String roomId, @RequestParam(name = "userId") String userId)
    {
        RInfo rInfo = new RInfo();
        RoomEntity room = RoomRepository.getRoom(roomId);
        if(userRepository.getByUserid(userId) == null) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_DOSE_NOT_EXIST");
        } else if(!RoomRepository.hasRoom(roomId)) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
        } else if(PlayerRepository.isPlaying(userId)){
            // 玩家已在某个房间中，不能再重复加入房间
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_IS_PLAYING");
        } else if(room.isStarted()==true){
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_GAME_STARTED");
            //游戏已经开始，不能加入
        }else{
            RoomRepository.addUserToRoom(userId, roomId);
            rInfo.setStatus("SUCCESS");
            //加入成功，通知同房间的其他人
            //WsInfo wsInfo = new WsInfo("ROOM_MESSAGE",RoomRepository.getRoom(roomId));
            //String dest = "/topic/roomId/"+roomId;
            //this.template.convertAndSend(dest,wsInfo);
        }
        return rInfo;
    }

    /*
    解散房间
    @GET
    @Param: 房间id,用户id
    @Return: 成功返回SUCCESS，失败则返回ERROR
     */
    @RequestMapping("dismiss")
    public RInfo dismiss(@RequestParam(name = "roomId") String roomId, @RequestParam(name = "userId") String userId)
    {
        RInfo rInfo = new RInfo();
        if(!RoomRepository.hasRoom(roomId)) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
        } else if(!RoomRepository.isOwner(userId, roomId)) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_PERMISSION_DENIED");
        } else {
            // TODO 通知同一房间内的所有玩家
            RoomRepository.removeRoom(roomId);
            rInfo.setStatus("SUCCESS");
            //解散成功，通知同房间其他人
            WsInfo wsInfo = new WsInfo("ROOM_MESSAGE","ROOM_DISMISS");
            String dest = "/topic/roomId/"+roomId;
            this.template.convertAndSend(dest,wsInfo);
        }
        return rInfo;
    }

    /*
    退出房间
    @GET
    @Param: 房间id、用户id
    @Return: 成功则返回SUCCESS，否则返回ERROR信息
     */
    //
    // TODO !!! 更新前端
    //
    @RequestMapping("quit")
    public RInfo quit(@RequestParam(name = "roomId") String roomId, @RequestParam(name = "userId") String userId)
    {
        RInfo rInfo = new RInfo();
        if(!RoomRepository.inRoom(userId, roomId)) {
            //不在房间中无法退出
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_NOT_IN_THIS_ROOM");
        } else if(RoomRepository.isOwner(userId, roomId)) {
            //房主退出
            if(RoomRepository.getRoom(roomId).getSize()<=1)
            {
                //1.只有房主一人,直接解散
                RoomRepository.removeRoom(roomId);
                rInfo.setStatus("SUCCESS");
                rInfo.setInfo("ROOM_DISMISS");

            }else {
                //2.还有其他人，新选房主
                //update room
                RoomEntity room = RoomRepository.getRoom(roomId);
                String newOwnerId = room.getPlayers().get(0).getUserId();
                room.setUserId(newOwnerId);
                room.removePlayer(newOwnerId);
//                RoomRepository.removeRoom(roomId);
//                RoomRepository.addRoom(roomId,room);
                //update player records
                PlayerRepository.removePlayer(userId);
                rInfo.setStatus("SUCCESS");
                //房间更新，通知其他人
                WsInfo wsInfo = new WsInfo("ROOM_MESSAGE",room);
                String dest = "/topic/roomId/"+roomId;
                this.template.convertAndSend(dest,wsInfo);
            }
        } else {
            //非房主退出，退出即可
            // TODO 通知同一房间内的所有玩家
            RoomRepository.removePlayerFromRoom(userId, roomId);
            rInfo.setStatus("SUCCESS");
            WsInfo wsInfo = new WsInfo("ROOM_MESSAGE",RoomRepository.getRoom(roomId));
            String dest = "/topic/roomId/"+roomId;
            this.template.convertAndSend(dest,wsInfo);
        }
        return rInfo;
    }

    /*
    随机匹配
    @POST
    @Param: 用户id
    @Return: 成功则返回房间信息，否则返回ERROR信息
     */
    //
    //
    // TODO !!! 算法有待进一步优化
    //
    @RequestMapping("match")
    public RInfo match(@RequestParam(value = "userId") String userId)
    {
        RInfo rInfo = new RInfo();
        if(PlayerRepository.isPlaying(userId)) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_IS_PLAYING");
            return rInfo;
        }
        String roomId = RoomRepository.dispatchUser(userId);
        if(roomId == null) {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_NO_PROPER_ROOM");
        } else {
            rInfo.setStatus("SUCCESS");
            rInfo.setInfo(RoomRepository.getRoom(roomId));
            //更新前端
            WsInfo wsInfo = new WsInfo("ROOM_MESSAGE",RoomRepository.getRoom(roomId));
            String dest = "/topic/roomId/"+roomId;
            this.template.convertAndSend(dest,wsInfo);
        }
        return rInfo;
    }


    @RequestMapping("startGame")
    public RInfo start(@RequestParam(value = "roomId") String roomId)
    {
        RInfo rInfo = new RInfo();
        RoomEntity room = RoomRepository.getRoom(roomId);
        if(room==null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
        }
        else if(room.isStarted())
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_GAME_ALREADY_STARTED");
        }
        else{
            room.setStarted(true);
//            RoomRepository.removeRoom(roomId);
//            RoomRepository.addRoom(roomId,room);
            rInfo.setStatus("SUCCESS");
        }
        return rInfo;
    }


    @RequestMapping("endGame")
    public RInfo end(@RequestParam(value = "roomId") String roomId)
    {
        RInfo rInfo = new RInfo();
        RoomEntity room = RoomRepository.getRoom(roomId);
        if(room==null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
        }
        else if(!room.isStarted())
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_GAME_NOT_STARTED");
        }
        else{
            room.setStarted(false);
//            RoomRepository.removeRoom(roomId);
//            RoomRepository.addRoom(roomId,room);
            rInfo.setStatus("SUCCESS");
        }
        return rInfo;
    }



}

package com.server.draw.controller;

import com.server.draw.model.PaintingEntity;
import com.server.draw.model.RInfo;
import com.server.draw.model.RoomEntity;
import com.server.draw.model.UserEntity;
import com.server.draw.repository.PaintingRespository;
import com.server.draw.repository.RoomRepository;
import com.server.draw.repository.TargetRepository;
import com.server.draw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RequestMapping("painting")
@RestController
public class PaintingController {

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("genTarget")
    public RInfo genTarget(@RequestParam(name = "userId") String userId,
                           @RequestParam(name = "roomId") String roomId)
    {
        RInfo rInfo = new RInfo();
        RoomEntity room = RoomRepository.getRoom(roomId);
        if(room == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
            return rInfo;
        }
        if(!RoomRepository.inRoom(userId, roomId))
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_NOT_IN_THIS_ROOM");
            return rInfo;
        }

        rInfo.setStatus("SUCCESS");
        ArrayList<String> excludedTargets = new ArrayList<>();
        for (PaintingEntity painting : room.getPaintings()) {
            excludedTargets.add(painting.getTargetName());
        }
        excludedTargets.add("impossible_things");
        rInfo.setInfo(targetRepository.getByLevel(room.getLevel(), excludedTargets));
        return rInfo;
    }

    @RequestMapping("setTarget")
    public RInfo setTarget(@RequestParam(name = "userId") String userId,
                           @RequestParam("roomId") String roomId,
                           @RequestParam("targetName") String targetName)
    {
        RInfo rInfo = new RInfo();
        if(!RoomRepository.inRoom(userId, roomId))
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_NOT_IN_THIS_ROOM");
            return rInfo;
        }
        RoomEntity room = RoomRepository.getRoom(roomId);
        if(room == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ROOM_DOES_NOT_EXIST");
            return rInfo;
        }
        // 移除先前的绘画记录，将该绘画目标加入到房间的已画过的目标图形，并新建绘画记录
        PaintingEntity painting = PaintingRespository.getPainting(roomId);
        if(painting != null)
        {
            room.addPainting(painting);
            PaintingRespository.removePainting(roomId);
        }
        PaintingRespository.addPainting(userId, roomId, targetName);
        rInfo.setStatus("SUCCESS");
        return rInfo;
    }


    @RequestMapping("isRightGuess")
    public RInfo isRightGuess(@RequestParam(name = "userId") String userId,
                              @RequestParam(name = "roomId") String roomId,
                              @RequestParam(name = "targetName") String targetName)
    {
        RInfo rInfo = new RInfo();
        UserEntity user = userRepository.getByUserid(userId);
        if(user == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_DOES_NOT_EXIST");
            return rInfo;
        }
        PaintingEntity painting = PaintingRespository.getPainting(roomId);
        if(painting == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_NO_PAINTING_IN_THIS_ROOM");
            return rInfo;
        }

        if(painting.getTargetName().equals(targetName))
        {
            rInfo.setInfo("SUCCESS");
            // TODO 获取绘制目标的参数，给用户计算分数
            int correctGuess = user.getCorrectGuess();
            int totalScore = user.getTotalScore();
            correctGuess += 1;
            int scoreAdded = 2 * targetRepository.getByName(targetName).getLevel();
            totalScore += scoreAdded;
            user.setCorrectGuess(correctGuess);
            user.setTotalScore(totalScore);
            userRepository.save(user);
            rInfo.setInfo(scoreAdded);
            return rInfo;
        }
        else
        {
            rInfo.setStatus("SUCCESS");
            // TODO 计入用户的失败猜测次数
            int wrongGuess = user.getWrongGuess();
            wrongGuess += 1;
            user.setWrongGuess(wrongGuess);
            userRepository.save(user);
            rInfo.setInfo(0);
            return rInfo;
        }
    }


}

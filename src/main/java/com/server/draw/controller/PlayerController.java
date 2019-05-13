package com.server.draw.controller;


import com.server.draw.model.PlayerEntity;
import com.server.draw.model.RInfo;
import com.server.draw.repository.PlayerRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("player")
@RestController
public class PlayerController {

    @RequestMapping("get")
    public RInfo getPlayer(@Param("userId") String userId)
    {
        RInfo rInfo = new RInfo();
        PlayerEntity player = PlayerRepository.getPlayer(userId);
        if(player == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_IS_NOT_PLAYING");
        }
        else
        {
            rInfo.setStatus("SUCCESS");
            rInfo.setInfo(player);
        }
        return rInfo;
    }

    @RequestMapping("delete")
    public RInfo deletePlayer(@Param("userId") String userId)
    {
        RInfo rInfo = new RInfo();
        if(!PlayerRepository.isPlaying(userId))
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_IS_NOT_PLAYING");
        }
        else
        {
            PlayerRepository.removePlayer(userId);
            rInfo.setStatus("SUCCESS");
        }
        return rInfo;
    }

}

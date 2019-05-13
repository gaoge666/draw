package com.server.draw.controller;

import com.server.draw.model.RInfo;
import com.server.draw.model.UserEntity;
import com.server.draw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "find",produces = "text/json;charset=UTF-8")
    public List<UserEntity> find(@RequestBody ArrayList<String> userIds)
    {
        List<UserEntity> users = new ArrayList<>();
        for (String userId: userIds) {
            UserEntity user = userRepository.getByUserid(userId);
            if(user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @RequestMapping("isRegistered")
    public RInfo register(@RequestParam("userId") String userid)
    {
        RInfo rInfo = new RInfo();
        rInfo.setStatus("SUCCESS");
        UserEntity user = userRepository.getByUserid(userid);
        if(user == null) {
            rInfo.setInfo("NO");
        } else {
            rInfo.setInfo("YES");
        }
        return rInfo;
    }

    // TODO 更新用户头像和昵称
    @RequestMapping("register")
    public RInfo register(@RequestParam("userId") String userId, @RequestParam("nickName") String nickName, @RequestParam("photo") String photo)
    {
        RInfo rInfo = new RInfo();
        if(nickName.equals("") || nickName == null ||
                photo.equals("") || nickName == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_ILLEGAL_ARGUMENT");
            return rInfo;
        }
        UserEntity user = userRepository.getByUserid(userId);
        if(user == null) {
            userRepository.save(new UserEntity(userId,nickName,photo,0,0,0,0));
            rInfo.setStatus("SUCCESS");
        }else{
            user.setNickName(nickName);
            user.setPhoto(photo);
            userRepository.save(user);
            rInfo.setStatus("SUCCESS");
        }
        return rInfo;
    }

    // 获取用户头像
    @RequestMapping("getPhoto")
    public RInfo getPhoto(@RequestParam("userId") String userId)
    {
        RInfo rInfo = new RInfo();
        UserEntity user = userRepository.getByUserid(userId);
        if(user == null)
        {
            rInfo.setStatus("ERROR");
            rInfo.setInfo("E_USER_DOSE_NOT_EXIST");
            return rInfo;
        }
        rInfo.setStatus("SUCCESS");
        rInfo.setInfo(user.getPhoto());
        return rInfo;
    }


    // 获取排行榜
    @RequestMapping("getRank")
    public RInfo getRank()
    {
        RInfo rInfo = new RInfo();
        rInfo.setStatus("SUCCESS");
        ArrayList<UserEntity> topUsers = userRepository.getTopByTotalScore();
        rInfo.setInfo(topUsers);
        return rInfo;
    }

}



package com.leyou.auth.user.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.Md5Utils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.auth.user.config.UserProperties;
import com.leyou.auth.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
@EnableConfigurationProperties(UserProperties.class)
@Service
public class UserService {

    @Autowired
    private UserProperties prop;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX="user:verify:phone:";

    public Boolean checkData(String data,Integer type){

        User record = new User();//新建一个user对象，设置要查询的用户名

        //判断数据类型
        switch (type){ //因为type数据有2种，如果是用户名的值为1，则走第一个，如果1，2都不是就是其他值则抛出异常
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnums.INVALID_USER_DATA_TYPE_ERROR);
        }

        //通过数量查询有就是返回1，没有就是0，如果等于0返回true，1返回false
        return userMapper.selectCount(record) ==0;
    }

    public Boolean sendCode(String phone) {
        try {
            //生成key
            String key =KEY_PREFIX+phone;

            //生成验证码
            String code = NumberUtils.generateCode(6);

            HashMap<String, String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);

            //发送验证码
            amqpTemplate.convertAndSend(prop.getExchange(),prop.getRoutingKey(),msg);

            //保存验证码--发送验证码后保存是为了和之后用户填写的验证码校验
            redisTemplate.opsForValue().set(key,code,prop.getCode_expire_time(), TimeUnit.MINUTES);//传入key名加值，设置5分钟后消失

            return true;
        } catch (AmqpException e) {
            log.error("[短信服务] 发送短信失败。phone：{}， 异常信息：{}", phone,e);
            return false;
        }

    }

    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        //从redis取出验证码判断是否正确
        String codeCache = redisTemplate.opsForValue().get(key);
        if (!code.equals(codeCache)){
            log.error("[注册服务] 注册失败 用户名:",user.getUsername());
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        //生成盐
        String salt = NumberUtils.generateCode(6);
        user.setSalt(salt);
        //对密码进行加密
        String md5 = Md5Utils.string2MD5(user.getPassword() + salt);
        user.setPassword(md5);
        //将用户信息写入数据库
        Boolean boo = this.userMapper.insert(user)==1;
        return boo;
    }

    public  User queryUser(String username, String password) {
        //设置用户名查询
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        //检查查到的user是否为空--用户名不需要比对，因为是通过用户名查询的
        if (user == null){
            return null;
        }
        //校验密码提取密码后，进行md5加密进行比对
        String pwd1 = Md5Utils.string2MD5(password+user.getSalt());
        String pwd2 = user.getPassword();
        if (!pwd2.equals(pwd1)){
            log.error("[登录服务]密码或用户名错误 用户名:{}",username,password);
            return null;
        }
        return user;

    }

}

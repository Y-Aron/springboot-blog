package com.aron.blog.domain.mapper;

import com.aron.blog.domain.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @author Y-Aron
 * @create 2018-10-08 22:10
 */
public interface UserMapper {

    @Insert("INSERT INTO tb_user SET id=#{id}, username=#{username}, password=#{password}")
    int create(User user);

//    @Select("SELECT `id`, `username`, `password` FROM tb_user WHERE `username`=#{username}")
//    User getUser(String username);

    @Select("SELECT `id`, `username`, `password` FROM tb_user WHERE username=#{username}")
    User findUser(String username);
}
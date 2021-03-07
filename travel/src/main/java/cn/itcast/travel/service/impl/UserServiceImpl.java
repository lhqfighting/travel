package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //根据用户名查询对象
        User u = userDao.findByUsername(user.getUsername());
        //判断是否为null
        if(u != null){
            //用户名存在，注册失败
            return false;
        }

        //保存用户信息
        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //激活邮件发送，邮件正文

        String content="<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【旅游网】</a>";

        MailUtils.sendMail(user.getEmail(),content,"激活邮件");

        return true;
    }

    /**
     * 激活代码
     * @param code
     * @return
     */
    @Override
    public boolean active(String code){
        //根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            //调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        }else{
            return false;
        }

    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public User longin(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}


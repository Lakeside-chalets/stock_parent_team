package com.zit.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zit.stock.Exception.BusinessException;
import com.zit.stock.constant.StockConstant;
import com.zit.stock.mapper.SysRoleMapper;
import com.zit.stock.mapper.SysUserMapper;
import com.zit.stock.mapper.SysUserRoleMapper;
import com.zit.stock.pojo.domain.UserPageListInfoDomain;
import com.zit.stock.pojo.entity.SysPermission;
import com.zit.stock.pojo.entity.SysRole;
import com.zit.stock.pojo.entity.SysUser;
import com.zit.stock.pojo.entity.SysUserRole;
import com.zit.stock.service.PermissionService;
import com.zit.stock.service.UserService;
import com.zit.stock.utils.IdWorker;
import com.zit.stock.vo.req.*;
import com.zit.stock.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: UserServiceImpl
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/8/27 14:24
 */
/**
 * @author by itheima
 * @Date 2022/6/29
 * @Description 定义user服务实现
 */

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据用户名称查询用户信息
     * @param userName 用户名称
     * @return
     */
    @Override
    public SysUser findByUserName(String userName) {
        SysUser user=sysUserMapper.findByUserName(userName);
        return user;
    }

    /**
     * 用户登录
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
       //1.判断是否合法
        if(vo == null || StringUtils.isBlank(vo.getUsername())|| StringUtils.isBlank(vo.getPassword())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断输入的验证码是否存在
        if(StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())){
           return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //判断redis中保存的验证码与输入的验证码是否相同（比较时忽略大小写）
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());
        if (StringUtils.isBlank(redisCode)) {
            //验证码过期
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        //忽略大小写比对
        if (!redisCode.equalsIgnoreCase(vo.getCode())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }

        //2.根据用户名去数据库查询用户信息，获取密码的密文
        SysUser dbUser = sysUserMapper.findByUserName(vo.getUsername());
        if (dbUser == null) {
            //用户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }

        //3.调用密码匹配器，匹配密码
        if (!passwordEncoder.matches(vo.getPassword(),dbUser.getPassword())) {
            //密码不匹配
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }

       //4.响应
        LoginRespVo loginRespvo = new LoginRespVo();

//        reqsVo.setUsername(dbUser.getUsername());
//        reqsVo.setNickName(dbUser.getNickName());
//        reqsVo.setPhone(dbUser.getPhone());
//        reqsVo.setId(dbUser.getId());
        //发现LoginRespVo和SysUser对象属性名称和类型一致
        BeanUtils.copyProperties(dbUser,loginRespvo);
        //获取指定用户的权限集合，添加获取侧边栏数据和按钮权限的结合信息
       List<SysPermission> permissions = permissionService.findPermissionsByUserId(dbUser.getId());
        //获取树状权限菜单数据，调用permissionService方法
       List<LoginRespPermission> menus = permissionService.getTree(permissions,0l,true);
       //获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        //封装按钮
        loginRespvo.setMenus(menus);
        loginRespvo.setPermissions(authBtnPerms);
        //生成token
        loginRespvo.setAccessToken(dbUser.getId()+":"+dbUser.getUsername());

        return R.ok(loginRespvo);
    }

    @Override
    public R<Map> getCaptchaCode() {

        //1.生成图片验证码
        /*
        参数1：图片的宽度
        参数2：图片的高度
        参数3：图片中包含验证的长度
        参数4：图中横线的数量
         */
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色清灰
        captcha.setBackground(Color.lightGray);
        //自定义生成校验码的规则
//        captcha.setGenerator(new CodeGenerator() {
//            @Override
//            public String generate() {
//                //自定义校验码生成的逻辑
//                return null;
//            }
//
//            @Override
//            public boolean verify(String s, String s1) {
//                return false;
//            }
//        });
        //获取校验码
        String checkCode = captcha.getCode();
        //1.获取经过base64编码处理过的图片数据
        String imageData = captcha.getImageBase64();
        //2.生成sessionId,转换成字符串，防止序列码精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的图片校验码：{}.会话id：{}",checkCode,sessionId);
        //3.将sessionId作为key，校验码作为value保存在redis中
        /*
            使用redis模拟session的行为，通过过期时间设置
         */
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+sessionId,checkCode,1, TimeUnit.MINUTES);
      //4.组装数据给前端
        HashMap<String,String> data = new HashMap<>();
        data.put("sessionId",sessionId);
        data.put("imageData",imageData);
    //5.响应数据
        return R.ok(data);

    }

    /**
     * 获取用户信息分页查询条件包含：分页信息 用户创建日期范围
     * @param userPageReqVo 用户信息请求参数
     * @return
     */
    @Override
    public R<PageResult> getUserListPage(UserPageReqVo userPageReqVo) {
        //获取分页参数
        PageHelper.startPage(userPageReqVo.getPageNum(),userPageReqVo.getPageSize());
        //根据多条件进行mapper查询
        List<UserPageListInfoDomain> users = sysUserMapper.findUserAllInfoByPage(userPageReqVo.getUsername(),userPageReqVo.getNickName(),userPageReqVo.getStartTime(),userPageReqVo.getEndTime());
        //将返回的数据封装到PageInfo中
        PageInfo<UserPageListInfoDomain> pageInfo = new PageInfo<>(users);
        PageResult<UserPageListInfoDomain> pageResult = new PageResult<>(pageInfo);
        //响应数据
        return R.ok(pageResult);
    }

    /**
     * 添加用户
     * @param userAddReqVo 请求参数
     * @return
     */
    @Override
    public R<String> addUsers(UserAddReqVo userAddReqVo) {
        //判断用户是否存在
        SysUser sysUser = sysUserMapper.findByUserName(userAddReqVo.getUsername());
        if (sysUser != null) {
            return R.error(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        //创建新用户容器
        SysUser user = new SysUser();
        //将userAddVo中的数据复制到user中
        BeanUtils.copyProperties(userAddReqVo,user);
        //设置用户的id
        user.setId(idWorker.nextId());
        //设置密码,并将密码加密
        user.setPassword(passwordEncoder.encode(userAddReqVo.getPassword()));
//        //设置邮箱
//        user.setEmail(userAddReqVo.getEmail());
//        //设置昵称
//        user.setNickName(userAddReqVo.getNickName());
//        //设置真实姓名
//        user.setRealName(userAddReqVo.getRealName());
//        //设置性别
//        user.setSex(userAddReqVo.getSex());
         //添加更新和创建时间
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //添加是否被删除
        user.setDeleted(1);

        //插入数据
        int row = sysUserMapper.insert(user);
        if (row != 1) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据用户id获取角色
     * @param userId 用户id
     * @return
     */
    @Override
    public R<Map<String, List>> getRoleByUserId(Long userId) {
        //获取角色id集合
       List<Long> roleIds = sysUserMapper.findRolesIdByUserId(userId);
        //用mapper查询所有角色的集合
       List<SysRole> roles = sysRoleMapper.findAllroles();
       //封装
        HashMap<String , List> map = new HashMap<>();
        map.put("allRole",roles);
        map.put("ownRoleIds",roleIds);
        return R.ok(map);
    }

    /**
     * 更新用户角色信息
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//需要对数据库进行频繁操作，所以加上事务管理回滚
    public R<String> updateUserRolesInfo(UserOneRoleReqVo vo) {
        //先判断用户id是否存在
        if (vo.getUserId() == null) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //先删除用户原来拥有的角色信息，再插入用户的新角色
        sysUserRoleMapper.deleteByUserId(vo.getUserId());
        //判断传递来的角色集合是否为空（判断是否删除干净）
        if (CollectionUtils.isEmpty(vo.getRoleIds())) {
            //如果为空，说明用户的角色信息全部清空了
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        //循环插入角色信息
            //封装用户的角色信息
        List<SysUserRole> list = new ArrayList<>();
        for (Long roleId : vo.getRoleIds()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(idWorker.nextId());
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreateTime(new Date());
            //装角色入集合
            list.add(sysUserRole);
        }
        //批量插入用户角色的信息
        int row = sysUserRoleMapper.insertUserRoleBatch(list);

        if (row == 0) {
            //如果插入失败，则返回错误信息
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 批量删除用户信息
     * @param userIds
     * @return
     */
    @Override
    public R<String> DeleteByUserid(List<Long> userIds) {
        //判断传入的集合是否为空
        if(CollectionUtils.isEmpty(userIds)){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //删除用户就是将用户deleted改为0 ，不完全删除
        int row = sysUserMapper.DeleteByUserid(userIds);
        //如果删除失败
        if (row == 0) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 获根据用户id获取用户信息，完成个人资料展示
     * @param id 用户id
     * @return
     */
    @Override
    public R<UserInfoRespVo> getUserInfo(Long id) {
        //判断id是否存在
        if (id == null) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //根据id查询用户信息
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        UserInfoRespVo userInfoRespVo = new UserInfoRespVo();
        //将信息复制到个人资料里面
        BeanUtils.copyProperties(sysUser,userInfoRespVo);
        //响应数据
        return R.ok(userInfoRespVo);
    }

    /**
     * 据id更新用户基本信息
     * @param vo
     * @return
     */
    @Override
    public R<String> updateUserInfo(UserUpdateInfoVo vo) {
       //判断传入的请求参数是否为空
        if (vo == null) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //将数据复制到SysUser类中
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo,sysUser);
        //修改的时间要更新
        sysUser.setUpdateTime(new Date());
        int row  = sysUserMapper.updateByPrimaryKey(sysUser);
        //响应数据
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

}
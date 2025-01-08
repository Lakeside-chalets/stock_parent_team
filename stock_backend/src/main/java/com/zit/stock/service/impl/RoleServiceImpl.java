package com.zit.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zit.stock.Exception.BusinessException;
import com.zit.stock.mapper.SysPermissionMapper;
import com.zit.stock.mapper.SysRoleMapper;
import com.zit.stock.mapper.SysRolePermissionMapper;
import com.zit.stock.pojo.entity.SysRole;
import com.zit.stock.pojo.entity.SysRolePermission;
import com.zit.stock.service.RoleService;
import com.zit.stock.utils.IdWorker;
import com.zit.stock.vo.req.RoleAddVo;
import com.zit.stock.vo.req.RolePageVo;
import com.zit.stock.vo.req.RoleUpdateVo;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import com.zit.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色管理相关接口实现类
 * @Author: mianbaoren
 * @Date: 2024/10/1 19:46
 */
@Service("roleService")
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 分页查询当前角色信息
     * @param vo
     * @return
     */
    @Override
    public R<PageResult> getRolesAllInfo(RolePageVo vo) {
        //获取分页参数
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        //mapper查询角色信息
        List<SysRole> allroles = sysRoleMapper.findAllroles();
        //返回分页查询结果,封装分页
//        PageInfo<SysRole> pageInfo = new PageInfo<>(allroles);
//        PageResult<SysRole> pageResult = new PageResult<>(pageInfo);
        PageResult<SysRole> pageResult = new PageResult<>(new PageInfo<>(allroles));
        //响应结果
        return R.ok(pageResult);
    }

    /**
     * 添加角色和角色关联权限
     * @param roleAddVo
     * @return
     */
    @Override
    public R<String> addRoleAndPermission(RoleAddVo roleAddVo) {
        //判断传入参数是否为空
        if (roleAddVo == null) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //添加角色的名字和说明
        String name = roleAddVo.getName();
        String description = roleAddVo.getDescription();
        //组装角色
        SysRole sysRole = new SysRole();
        sysRole.setId(idWorker.nextId());
        sysRole.setName(name);
        sysRole.setDescription(description);
        sysRole.setStatus(1);
        sysRole.setDeleted(1);
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());
        //调用mapper方法添加角色
        int row = sysRoleMapper.insert(sysRole);
        if (row != 1) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //添加角色对应的权限
        List<Long> permissionsIds = roleAddVo.getPermissionsIds();
        //获取父类权限的子集权限
        List<Long> list = new ArrayList<>();
        //判断权限集合是否为空
//        if (!CollectionUtils.isEmpty(permissionsIds)) {
        //遍历目录权限集合
        for (Long permissionsId : permissionsIds) {
            //根据目录父集合找到子权限集合
            List<Long> childPermissionId = sysPermissionMapper.findChildren(permissionsId);
            //遍历子权限集合
            for (Long child : childPermissionId) {
                //添加进集合
                list.add(child);
            }
            list.add(permissionsId);
        }

        List<SysRolePermission> listAll = new ArrayList<>();
        //判断权限集合是否为空
//        if (!CollectionUtils.isEmpty(permissionsIds)) {
            for (Long permissionsId : list) {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setId(idWorker.nextId());
                sysRolePermission.setRoleId(sysRole.getId());
                sysRolePermission.setPermissionId(permissionsId);
                sysRolePermission.setCreateTime(new Date());
                listAll.add(sysRolePermission);
            }
//        }
        if(listAll.size() != 0) {
            //批量添加
            int count = sysRolePermissionMapper.addRolePermissionInfo(listAll);
            if (count == 0) {
                throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
            }
        }
        //响应数据
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    @Override
    public R<Set<String>> getPermissionIdByRoleId(String roleId) {
        //根据角色id查询权限集合,（因为id是不能重复的所以用set集合）
      Set<String> set = sysRolePermissionMapper.getPermissionIdByRoleId(roleId);
        if (set == null) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }

        return R.ok(set);
    }

    /**
     * 添加角色和角色关联权限,编辑角色信息提交的数据
     * @param vo
     * @return
     */
    @Override
    public R<String> updateRoleAndPermission(RoleUpdateVo vo) {
        //判断传入的参数是否为空
        if (vo == null) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //1.先修改角色表中的信息
            //将数据封装到实体类中
        SysRole sysRole = new SysRole();
        sysRole.setId(vo.getId());
        sysRole.setName(vo.getName());
        sysRole.setDescription(vo.getDescription());
        sysRole.setUpdateTime(new Date());
         //修改角色表
        int row = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if (row != 1) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //2.再修改角色权限表
            //先删除原来的权限再添加新的权限
        int count = sysRolePermissionMapper.deleteByRoleId(vo.getId());
        //添加角色对应的权限
        List<Long> permissionsIds = vo.getPermissionsIds();
        //获取父类权限的子集权限
        List<Long> list = new ArrayList<>();
        //判断权限集合是否为空
//        if (!CollectionUtils.isEmpty(permissionsIds)) {
        //遍历目录权限集合
        for (Long permissionsId : permissionsIds) {
            //判断是否是目录
            Boolean checkIsMenu = sysPermissionMapper.checkIsMenu(permissionsId);
            //如果是则跳过遍历循环，否则会添加一些不需要的权限
            if(!checkIsMenu) {
                //根据目录父集合找到子权限集合
                List<Long> childPermissionId = sysPermissionMapper.findChildren(permissionsId);
                //遍历子权限集合
                for (Long child : childPermissionId) {
                    //添加进集合
                    list.add(child);
                }
            }
            list.add(permissionsId);
        }

        List<SysRolePermission> listAll = new ArrayList<>();
        //判断权限集合是否为空
//        if (!CollectionUtils.isEmpty(permissionsIds)) {
        for (Long permissionsId : list) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setId(idWorker.nextId());
            sysRolePermission.setRoleId(sysRole.getId());
            sysRolePermission.setPermissionId(permissionsId);
            sysRolePermission.setCreateTime(new Date());
            listAll.add(sysRolePermission);
        }
//        }
        if(listAll.size() != 0) {
            //批量添加
            int result = sysRolePermissionMapper.addRolePermissionInfo(listAll);
            if (result == 0) {
                throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
            }
        }
        //响应数据
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据角色id删除角色
     * @param roleId
     * @return
     */
    @Override
    public R<String> deleteRole(Long roleId) {
        //判断roleId是否为空
        if (roleId == null) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //逻辑删除，将deleted状态改为0即可
        SysRole sysRole = SysRole.builder()
                .id(roleId)
                .deleted(0)
                .updateTime(new Date())
                .build();
        //调用mapper执行修改操作
        int row = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if (row != 1){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 更新用户的状态信息
     * @param roleId 角色id
     * @param status 状态 1.正常 0：禁用
     * @return
     */
    @Override
    public R<String> updateRoleStatus(Long roleId, Integer status) {
//        //判断传入参数是否为空
//        if (roleId == null || status == null) {
//            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
//        }
//        //组装数据
//        SysRole sysRole = SysRole.builder()
//                .id(roleId)
//                .deleted(0)
//                .updateTime(new Date())
//                .build();
//        //修改状态
//        int row = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
//
//        if (row != 1){
//            throw new BusinessException(ResponseCode.ERROR.getMessage());
//        }
//        return R.ok(ResponseCode.SUCCESS.getMessage());
        //组装数据
        SysRole role = SysRole.builder().id(roleId).status(status).updateTime(new Date()).build();
        //修改状态
        int row = sysRoleMapper.updateByPrimaryKeySelective(role);

        if (row != 1){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }
}

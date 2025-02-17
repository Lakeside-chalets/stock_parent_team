package com.zit.stock.service.impl;

import com.google.common.collect.Lists;
import com.zit.stock.Exception.BusinessException;
import com.zit.stock.mapper.SysPermissionMapper;
import com.zit.stock.mapper.SysRolePermissionMapper;
import com.zit.stock.pojo.entity.SysPermission;
import com.zit.stock.service.PermissionService;
import com.zit.stock.utils.IdWorker;
import com.zit.stock.vo.req.PermissionAddVo;
import com.zit.stock.vo.req.PermissionUpdateVo;
import com.zit.stock.vo.resp.LoginRespPermission;
import com.zit.stock.vo.resp.PermissionTreeNodeVo;
import com.zit.stock.vo.resp.R;
import com.zit.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: PermissionServiceImpl
 * @Description: 有关权限的接口实现类
 * @Author: mianbaoren
 * @Date: 2024/9/25 0:07
 */
@Service("permissionService")
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    /**
     * 根据用户id查询用户的所有权限
     * @param id 用户id
     * @return
     */
    @Override
    public List<SysPermission> findPermissionsByUserId(Long id) {
        List<SysPermission> list = sysPermissionMapper.findPermissionsByUserId(id);
        return list;
    }

    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    @Override
    public List<LoginRespPermission> getTree(List<SysPermission> permissions, long pid, boolean isOnlyMenuType) {
        //创建一个集合，用于存放便利好的树状菜单
        ArrayList<LoginRespPermission> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(permissions)) {
            return list;
        }
        //便利查询到的所有用户权限
        for (SysPermission permission : permissions) {
            if (permission.getPid().equals(pid)) {
                //判断是否是按钮，如果左边是true，右边则不再运算直接返回true，只要有一个是真结果就是真
                //这里设置不能为3是因为这里3代表的是按钮，不是菜单栏里面的东西了所以就不需要将按钮添加到菜单栏当中，会额外的去获取按钮
                if (permission.getType().intValue()!=3 || !isOnlyMenuType) {
                    LoginRespPermission respNodeVo = new LoginRespPermission();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getTitle());
                    respNodeVo.setIcon(permission.getIcon());
                    respNodeVo.setPath(permission.getUrl());
                    respNodeVo.setName(permission.getName());
                    //通过递归的方式去找他的子集，这里也就是说将整个集合传递到里面去寻找，通过本次查找的id值去找，
                    //就比如说，当前的这个admin，如果是顶级目录，那么他就没有父级，所以他的pid就是0，如果是菜单，那么他的pid可能就是这个目录的id
                    //所以我们根据以目录的id，目录下面有很多菜单，如果归属与这个目录自然菜单的pid就等于目录的id，如果归属与这个菜单，那么按钮的pid就是菜单的id
                    //Children的类型是List<LoginRespPermission>，也就是这个方法返回的数据类型和这个属性的类型一致
                    respNodeVo.setChildren(getTree(permissions,permission.getId(),isOnlyMenuType));
                    list.add(respNodeVo);
                }
            }
        }
        return list;
    }



    /**
     * 树状结构回显权限集合,递归获取权限数据集合
     * @return
     */
    @Override
    public R<List<LoginRespPermission>> getPermissionTree() {
        //获取所有权限集合
       List<SysPermission> permissions = sysPermissionMapper.getAllPermission();
       //根据权限集合，遍历获取权限树
        List<LoginRespPermission> list = getTree(permissions, 0l, true);
        //响应数据
        return R.ok(list);
    }

    /**
     * 获取所有权限的全部信息
     * @return
     */
    @Override
    public R<List<SysPermission>> getAllPermission() {
        //调用mapper查询所有权限全部信息
       List<SysPermission> all = sysPermissionMapper.findAll();
       //判断集合是否为空
        if (CollectionUtils.isEmpty(all)) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //响应参数
        return R.ok(all);
    }

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    @Override
    public R<List<PermissionTreeNodeVo>> getPermissionsTreeOnlyMeuns() {
        //调用mapper获取所有权限的集合
        List<SysPermission> permissions = sysPermissionMapper.findAll();
        //构建权限树集合
        List<PermissionTreeNodeVo> permissionTree = Lists.newArrayList();
        //构架顶级菜单（默认选项）
        PermissionTreeNodeVo root = new PermissionTreeNodeVo();
        root.setId(0l);
        root.setTitle("顶级菜单");
        root.setLevel(0);
        permissionTree.add(root);
        permissionTree.addAll(getPermissionLevelTree(permissions,0l,1));
        //响应数据
        return R.ok(permissionTree);
    }

    /**
     * 权限添加按钮
     * @param vo
     * @return
     */
    @Override
    public R<String> addPermission(PermissionAddVo vo) {
        //判断是否为空
        if (vo==null){
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //将数据复制到pojo类中
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(vo,permission);
        //判断添加的权限类型是否合法
        this.checkPermissionForm(permission);
        //合法执行以下流程
        permission.setId(idWorker.nextId());
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        permission.setStatus(1);
        permission.setDeleted(1);
        //调用mapper添加到数据库
        int row = sysPermissionMapper.insert(permission);
        if (row!=1){
            throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR.getMessage());
        }
        //响应数据
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }


    /**
     * 修改权限按钮
     * @param vo
     * @return
     */
    @Override
    public R<String> updatePermission(PermissionUpdateVo vo) {
        //检查当前提交数据是否合法,如果不合法，则抛出异常
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(vo,permission);
        this.checkPermissionForm(permission);
        //TODO 如果再更新时，父级已被修改，则抛出异常
        permission.setStatus(1);
        permission.setUpdateTime(new Date());
        permission.setDeleted(1);
        int count = this.sysPermissionMapper.updateByPrimaryKeySelective(permission);
        if (count!=1) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }


    /**
     * 删除权限按钮菜单
     * @param permissionId
     * @return
     */
    @Override
    public R<String> deletePermission(Long permissionId) {
        //判断是否有子集，有就不能删除
      int row = sysPermissionMapper.findChildrenCountByParentId(permissionId);
        if (row > 0) {
            throw new BusinessException(ResponseCode.ROLE_PERMISSION_RELATION.getMessage());
        }
        //删除角色权限表中对应的数据
        sysRolePermissionMapper.deleteByPermissionId(permissionId);
        //逻辑删除，将deleted改为0即可
        SysPermission permission = SysPermission.builder()
                .id(permissionId)
                .deleted(0)
                .updateTime(new Date())
                .build();
        int updateCount = this.sysPermissionMapper.updateByPrimaryKeySelective(permission);
        if (updateCount!=1) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //响应数据
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }


    /**
     * 递归设置级别，用于权限列表 添加/编辑 所属菜单树结构数据
     * @param permissions 权限集合
     * @param parentId 父级id
     * @param lavel 级别
     * @return
     */
    private List<PermissionTreeNodeVo> getPermissionLevelTree(List<SysPermission> permissions, long parentId, int lavel) {
        List<PermissionTreeNodeVo> result=new ArrayList<>();
        for (SysPermission permission : permissions) {
            if (permission.getType().intValue()!=3 && permission.getPid().equals(parentId)) {
                PermissionTreeNodeVo nodeTreeVo = new PermissionTreeNodeVo();
                nodeTreeVo.setId(permission.getId());
                nodeTreeVo.setTitle(permission.getTitle());
                nodeTreeVo.setLevel(lavel);
                result.add(nodeTreeVo);
                result.addAll(getPermissionLevelTree(permissions,permission.getId(),lavel+1));
            }
        }
        return result;
    }

    private void checkPermissionForm(SysPermission vo) {
        if (vo!=null || vo.getType()!=null || vo.getPid()!=null){
            //获取权限类型 0：顶级目录 1.普通目录 2.菜单 3.按钮
            Integer type = vo.getType();
            //获取父级id
            Long pid = vo.getPid();
            //根据父级id查询父级信息
            SysPermission parentPermission = this.sysPermissionMapper.selectByPrimaryKey(pid);
            if (type==1){
                if(!(pid == 0l) || (parentPermission!=null && parentPermission.getType()> 1)){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR.getMessage());
                }
            }
            else if (type==2){
                if (parentPermission==null || parentPermission.getType() !=1 ){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR.getMessage());
                }
                if (StringUtils.isBlank(vo.getUrl())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_CODE_NULL.getMessage());
                }
            }
            else if (type==3){
                if (parentPermission==null || parentPermission.getType()!=2){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR.getMessage());
                }
                else if (vo.getUrl()==null || vo.getCode()==null || vo.getMethod()==null){
                    throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
                }
            }
            else {
                throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
            }
        }else {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
    }
}

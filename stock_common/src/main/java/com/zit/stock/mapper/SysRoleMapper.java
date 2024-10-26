package com.zit.stock.mapper;

import com.zit.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mianbaoren
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 查询所有角色
     * @return
     */
    List<SysRole> findAllroles();


    /**
     * 根据角色ID查询所有角色信息
     * @param id
     * @return
     */
    List<SysRole> getRoleByUserId(@Param("id") Long id);
}

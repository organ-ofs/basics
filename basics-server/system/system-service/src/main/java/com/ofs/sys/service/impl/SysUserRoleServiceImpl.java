package com.ofs.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ofs.sys.entity.SysUserRole;
import com.ofs.sys.mapper.SysUserRoleMapper;
import com.ofs.sys.service.SysUserRoleService;
import com.ofs.web.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Licoy
 * @version 2018/4/16/11:32
 */
@Service
@Transactional
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper,SysUserRole> implements SysUserRoleService {

}

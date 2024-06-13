package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        //md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //判断返回结果
        if (emp == null) {
            return R.error("登录失败！");
        }

        //比对密码
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败！");
        }

        //查看员工状态，是否已禁用
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用！");
        }

        //登陆成功，将员工id存入Session并返回登录成功结果
        httpServletRequest.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     *
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest) {
        //清除Session中保存的当前登录员工
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());

        //设置初始密码123456，并md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        /*
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的id
        Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        */

        employeeService.save(employee);

        return R.success("新增员工成功！");
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        //构造分页构造器
        Page<Employee> pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name)
                .orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        log.info(employee.toString());

        /*Long empId = (long) httpServletRequest.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);*/
        employeeService.updateById(employee);

        return R.success("员工信息修改成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息！");
    }
}

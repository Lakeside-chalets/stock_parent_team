package com.zit.stock;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zit.stock.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: TestEasyExcel
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/9/2 14:29
 */
public class TestEasyExcel {
    public List<User> init() {
        //组装数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAddress("上海" + i);
            user.setUserName("张三" + i);
            user.setBirthday(new Date());
            user.setAge(10 + i);
            users.add(user);
        }
        return users;
    }
    /**
     * 直接导出后，表头名称默认是实体类中的属性名称
     */
    @Test
    public void test02(){
        List<User> users = init();
        //不做任何注解处理时，表头名称与实体类属性名称一致
        EasyExcel.write("D:\\JAVA\\code\\1.xls",User.class).sheet("用户信息").doWrite(users);
    }

    /**
     * 读取excel文件
     */
    @Test
    public void test03(){
        List<User> users = new ArrayList<>();
        EasyExcel.read("D:\\JAVA\\code\\1.xls", User.class, new AnalysisEventListener<User>() {
            /**
             * 逐行读取excel内容，并封装（读取一行，就会调一次该方法）
             * @param data
             * @param analysisContext
             */
            @Override
            public void invoke(User data, AnalysisContext analysisContext) {
                users.add(data);

            }

            /**
             * 所有行读取完毕后，回调方法（读取完成的通知）
             * @param analysisContext
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("excel read finished");
            }
        }).sheet("用户信息").doRead();
    }

}

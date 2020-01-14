package com.ofs.commons.plugin.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成
 *
 * @author ly
 */
@SuppressWarnings("unchecked")
public class GeneratorTest extends BaseBizGenerator {
    /**
     * 测试 run 执行 注意：不生成service接口 注意：不生成service接口 注意：不生成service接口
     * <p>
     * 配置方法查看
     * </p>
     */
    public static void main(String[] args) {
        GeneratorTest generator = new GeneratorTest();
        String outputDir = "D:\\frame-gao\\web\\src\\main\\java\\";
        String[] tableArray = new String[]{
                "sys_group",
                "sys_group_role",
                "sys_log",
                "sys_menus",
                "sys_resource",
                "sys_role",
                "sys_role_resource",
                "sys_user",
                "sys_user_role",

        };
        String packageDir = "com.ofs.system";
        String xmlDir = "D:\\frame-gao\\web\\src\\main\\resources\\mapper\\";

        Map customConfig = new HashMap(6);
        customConfig.put("logModel", "FrameModelConstant.SYS");
        generator.autoGenerator(outputDir, false, tableArray, packageDir, xmlDir, "", customConfig);

    }
}

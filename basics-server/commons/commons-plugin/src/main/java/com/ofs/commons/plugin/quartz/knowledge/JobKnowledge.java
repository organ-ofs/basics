package com.ofs.commons.plugin.quartz.knowledge;

/**
 * @author gaoly
 * @version V1.0
 * @Title: JobKnowledge
 * @Description: job 涉及枚举类型集合
 * @date 2017/3/22 10:53
 */
public class JobKnowledge {
    /**
     * 手动执行
     */
    public static final String EXECUTE_BY_HAND_PARM = "executeByHand";


    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * JOB任务的状态
     *
     * @author gaoly
     */
    public enum TaskStatusEnum {

        /**
         * 运行中
         */
        PLAY("1"),

        /**
         * 暂停中
         */
        PAUSE("0");


        /**
         * 成员变量
         */
        private String value;

        /**
         * 构造方法
         */
        TaskStatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * JOB任务的执行状态
     *
     * @author gaoly
     */
    public enum ExecStatusEnum {

        /**
         * 成功
         */
        SUCCESS("1"),


        /**
         * 失败
         */
        FAILURE("0");


        /**
         * 成员变量
         */
        private String value;

        /**
         * 构造方法
         */
        ExecStatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }


}

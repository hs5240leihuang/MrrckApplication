package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：app启动图版本信息实体
 * 类名称：com.mrrck.ap.version.entity.StartDiagramVersionEntity     
 * 创建人：仲崇生
 * 创建时间：2016-5-23 下午04:03:32   
 * @version V1.0
 */
public class StartDiagramVersionEntity extends MkStartDiagramVersion {
   
    /**app启动图配置信息列表*/
    private List<MkStartDiagram> startDiagramList;

    public List<MkStartDiagram> getStartDiagramList() {
        return this.startDiagramList;
    }

    public void setStartDiagramList(List<MkStartDiagram> startDiagramList) {
        this.startDiagramList = startDiagramList;
    }
    
}

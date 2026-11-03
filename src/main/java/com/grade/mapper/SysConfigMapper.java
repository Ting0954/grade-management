package com.grade.mapper;

import com.grade.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysConfigMapper {
    List<SysConfig> selectAll();
    SysConfig selectByKey(String configKey);
    int insert(SysConfig c);
    int update(SysConfig c);
}

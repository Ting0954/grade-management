package com.grade.mapper;

import com.grade.entity.Term;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TermMapper {
    List<Term> selectAll();
    Term selectById(Integer id);
    int insert(Term t);
    int update(Term t);
    int deleteById(Integer id);
}

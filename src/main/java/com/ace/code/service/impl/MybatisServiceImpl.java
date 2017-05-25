package com.ace.code.service.impl;

import com.ace.code.mapper.SqlMapper;
import com.ace.code.service.IMybatisService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Diliang.Li
 * @date 2017/5/24.
 */
@Service
public class MybatisServiceImpl implements IMybatisService{
    @Autowired
    private SqlSessionFactory sessionFactory;


    public Map<String, Object> selectOne(String sql) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sessionFactory.openSession();
            SqlMapper sqlMapper = new SqlMapper(sqlSession);
            return sqlMapper.selectOne(sql);
        } finally {
            sqlSession.close();
        }
    }

    public List<Map<String, Object>> selectList(String sql) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sessionFactory.openSession();
            SqlMapper sqlMapper = new SqlMapper(sqlSession);
            return sqlMapper.selectList(sql);
        } finally {
            sqlSession.close();
        }
    }

}

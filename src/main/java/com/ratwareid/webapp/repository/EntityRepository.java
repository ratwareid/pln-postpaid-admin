package com.ratwareid.webapp.repository;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***********************************************************************
 * Module:  com.sisapp.ayodesajuaraapi.repository.EntityRepository
 * Author:  Ratwareid
 * Created: 31/10/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
@Repository
public class EntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("deprecation")
    public List<HashMap<String,Object>> getListFromQuery(String sql) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<HashMap<String,Object>>) q.getResultList();
    }

    @SuppressWarnings("deprecation")
    public List<HashMap<String,Object>> getListFromQuery(String sql,Object... params) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        if(params!=null){
            for(int i=0,len=params.length;i<len;i++){
                Object param=params[i];
                q.setParameter(i+1, param);
            }
        }
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<HashMap<String,Object>>) q.getResultList();
    }

    @SuppressWarnings("deprecation")
    public List<?> getListFromQuery(String sql, Class<?> dto, ArrayList paramArray) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        if (paramArray!= null) {
            Object[] params = paramArray.toArray();
            for (int x = 0; x < params.length; x++) {
                q.setParameter(x+1,  params[x]);

            }
        }
        q.setResultTransformer(Transformers.aliasToBean(dto));
        return (List<?>) q.getResultList();
    }

    @SuppressWarnings("deprecation")
    public <T> T getDTOFromQuery(String sql) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (T) q.uniqueResult();
    }

    public <T> Object getBD(String sql) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return ((Map.Entry)((HashMap) q.uniqueResult()).entrySet().toArray()[0]).getValue();
    }


    @SuppressWarnings("deprecation")
    public <T> T getDTOFromQuery(String sql,Object... params) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        if(params!=null){
            for(int i=0,len=params.length;i<len;i++){
                Object param=params[i];
                q.setParameter(i+1, param);
            }
        }
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (T) q.uniqueResult();
    }

    public <T> Object getBD(String sql, ArrayList paramArray) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        if (paramArray!= null) {
            Object[] params = paramArray.toArray();
            for (int x = 0; x < params.length; x++) {
                q.setParameter(x+1,  params[x]);

            }
        }
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return ((Map.Entry)((HashMap) q.uniqueResult()).entrySet().toArray()[0]).getValue();
    }


    @SuppressWarnings("deprecation")
    public <T> T getDTOFromQuery(String sql, Class<T> dto, Object... params) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createSQLQuery(sql);
        if(params!=null){
            for(int i=0,len=params.length;i<len;i++){
                Object param=params[i];
                q.setParameter(i+1, param);
            }
        }
        q.setResultTransformer(Transformers.aliasToBean(dto));
        return (T) q.uniqueResult();
    }

    @Transactional
    public void executeQuery(String sql){
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createNativeQuery(sql);
        q.executeUpdate();
    }

    @Transactional
    public void executeQuery(String sql,Object[] params){
        Session session = entityManager.unwrap(Session.class);
        NativeQuery q = session.createNativeQuery(sql);
        if(params!=null){
            for(int i=0,len=params.length;i<len;i++){
                Object param=params[i];
                q.setParameter(i+1, param);
            }
        }
        q.executeUpdate();
    }
}

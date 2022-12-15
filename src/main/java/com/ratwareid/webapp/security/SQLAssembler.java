package com.ratwareid.webapp.security;

import com.ratwareid.webapp.repository.EntityRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SQLAssembler {

    private StringBuffer szDataSource;
    private StringBuffer szSelect;
    private StringBuffer szQuery;
    private StringBuffer szClause;
    private StringBuffer szOrder;
    private StringBuffer szGroup;
    private ArrayList par;
    public String orderExpression;
    private String limit;

    public int pageSize,currentPage,offsitesize;
    public BigDecimal dataSize;
    private boolean enablePaging = false;

    public void addPar(Object x) {
        if (par==null) par = new ArrayList(10);
        par.add(x);
    }

    public ArrayList getPar() {
        return par;
    }

    public void setPar(ArrayList par) {
        this.par = par;
    }

    public void addDataSource(String stmt) {
        if (szDataSource == null)
            szDataSource = new StringBuffer(64);
        szDataSource.append(stmt);
    }

    public void addSelect(String stmt) {
        if (szSelect == null)
            szSelect = new StringBuffer(64);
        if (szSelect.length() > 0) szSelect.append(",");
        szSelect.append(stmt);
    }

    public void addQuery(String stmt) {
        if (szQuery == null)
            szQuery = new StringBuffer(64);
        szQuery.append(stmt);
    }

    public void addClause(String stmt) {
        if (szClause == null)
            szClause = new StringBuffer(64);
        if (szClause.length()>0) szClause.append(" and ");
        szClause.append(stmt);
    }

    public void addClauseOR(String stmt) {
        if (szClause == null)
            szClause = new StringBuffer(64);
        if (szClause.length()>0) szClause.append(" or ");
        szClause.append(stmt);
    }

    public String getSQLCount() {
        final StringBuffer sz = new StringBuffer();

        sz.append("select count(*)\n");
        sz.append(' ').append(szQuery.toString());

        if (szClause!=null && szClause.length()>0) sz.append(" where ").append(szClause);

        if (orderExpression != null) sz.append(' ').append(orderExpression);
        if (limit != null) sz.append(" limit ").append(limit.toString());

        return sz.toString();
    }

    public String getSQL() {
        final StringBuffer sz = new StringBuffer();

        sz.append("select ");
        sz.append(szSelect.toString());
        sz.append(' ').append(szQuery.toString());

        if (szClause!=null && szClause.length()>0) sz.append(" where ").append(szClause);

        if (orderExpression != null) sz.append(' ').append(orderExpression);
        if (szGroup != null) sz.append(" group by ").append(szGroup.toString());
        if (szOrder != null) sz.append(" order by ").append(szOrder.toString());
        if (limit != null) sz.append(" limit ").append(limit.toString());

        return sz.toString();
    }

    public String getSQL(int offsitesize,int pageSize) {
        final StringBuffer sz = new StringBuffer();

        sz.append("select ");
        sz.append(szSelect.toString());
        sz.append(' ').append(szQuery.toString());

        if (szClause!=null && szClause.length()>0) sz.append(" where ").append(szClause);

        if (orderExpression != null) sz.append(' ').append(orderExpression);
        if (szGroup != null) sz.append(" group by ").append(szGroup.toString());
        if (szOrder != null) sz.append(" order by ").append(szOrder.toString());
        sz.append(" offset ").append(offsitesize);
        sz.append(" limit ").append(pageSize);

        return sz.toString();
    }


    public void addOrder(String s) {
        if (szOrder==null)
            szOrder = new StringBuffer();

        if (szOrder.length()>0) szOrder.append(",");

        szOrder.append(s);
    }

    public List<?> getList(EntityRepository entityRepository,Class dtoClass) throws Exception {
        if (enablePaging){
            if (getPar() != null && getPar().size() > 0) {
                this.dataSize = new BigDecimal((BigInteger) entityRepository.getBD(getSQLCount(), getPar()));
            }else {
                this.dataSize = new BigDecimal((BigInteger) entityRepository.getBD(getSQLCount()));
            }
            return entityRepository.getListFromQuery(getSQL(offsitesize,pageSize),dtoClass,getPar());
        }else {
            return entityRepository.getListFromQuery(getSQL(), dtoClass,getPar());
        }
    }

    public boolean hasClause() {
        return szClause!=null && szClause.length()>0;
    }

    public void addParKeyword(Object o) {
        o=o==null?"":o;
        String p = ((String)o).toUpperCase();
        addPar("%"+p+"%");

    }

    public void addClauseIN(String fld, Object[] values) {

        StringBuffer sz = new StringBuffer();

        sz.append(fld).append(" in (");

        for (int i = 0; i < values.length; i++) {
            String s1 = (String) values[i];
            if (i>0) sz.append(',');
            sz.append("?");
            addPar(s1);
        }

        sz.append(")");

        addClause(sz.toString());
    }


    public void addClauseIN(String fld, String[] values) {

        StringBuffer sz = new StringBuffer();

        sz.append(fld).append(" in (");

        for (int i = 0; i < values.length; i++) {
            String s1 = values[i];
            if (i>0) sz.append(',');
            sz.append("?");
            addPar(s1);
        }

        sz.append(")");

        addClause(sz.toString());
    }

    public void setLimit(String n) {
        limit = n;
    }

    public void addParKeywordCS(String o) {
        o=o==null?"":o;
        addPar("%"+o+"%");
    }

    public void addGroup(String s) {
        if (szGroup==null)
            szGroup = new StringBuffer();

        if (szGroup.length()>0) szGroup.append(",");

        szGroup.append(s);
    }

    public String getClauses(String joiner) {
        if (szClause==null) return "";

        if (joiner==null) return szClause.toString()+" ";
        return joiner+" "+szClause.toString()+" ";
    }

    public void setPagination(int pageSize,int currentPage){
        this.enablePaging = true;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.offsitesize = (currentPage - 1) * pageSize;
    }

    public BigDecimal getDataSize() {
        return dataSize;
    }

    public void setDataSize(BigDecimal dataSize) {
        this.dataSize = dataSize;
    }
}

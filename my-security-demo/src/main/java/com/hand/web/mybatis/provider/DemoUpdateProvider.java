package com.hand.web.mybatis.provider;

import com.hand.annotation.UpdateIgnore;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.version.VersionException;

import java.util.Set;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-15
 * @description
 */
public class DemoUpdateProvider extends MapperTemplate {

    public DemoUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String updateWithoutIgnoreByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sql.append(this.updateSetColumns(entityClass, (String) null, false, false));
        sql.append(SqlHelper.wherePKColumns(entityClass, true));
        return sql.toString();
    }

    /**
     * update set列
     *
     * @param entityClass
     * @param entityName  实体映射名
     * @param notNull     是否判断!=null
     * @param notEmpty    是否判断String类型!=''
     * @return
     */
    public String updateSetColumns(Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<set>");
        // 获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        // 对乐观锁的支持
        EntityColumn versionColumn = null;
        // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnSet) {

            // 跳过被UpdateIgnore注解标记的字段
            if (column.getEntityField().isAnnotationPresent(UpdateIgnore.class)) {
               continue;
            }
            if (column.getEntityField().isAnnotationPresent(Version.class)) {
                if (versionColumn != null) {
                    throw new VersionException(entityClass.getCanonicalName() + " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
                }
                versionColumn = column;
            }
            if (!column.isId() && column.isUpdatable()) {
                if (column == versionColumn) {
                    Version version = versionColumn.getEntityField().getAnnotation(Version.class);
                    String versionClass = version.nextVersion().getCanonicalName();
                    //version = ${@tk.mybatis.mapper.version@nextVersionClass("versionClass", version)}
                    sql.append(column.getColumn())
                            .append(" = ${@tk.mybatis.mapper.version.VersionUtil@nextVersion(")
                            .append("@").append(versionClass).append("@class, ")
                            .append(column.getProperty()).append(")},");
                } else if (notNull) {
                    sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
                } else {
                    sql.append(column.getColumnEqualsHolder(entityName) + ",");
                }
            } else if (column.isId() && column.isUpdatable()) {
                //set id = id,
                sql.append(column.getColumn()).append(" = ").append(column.getColumn()).append(",");
            }
        }
        sql.append("</set>");
        return sql.toString();
    }

}

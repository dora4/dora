package dora.db.converter;

import dora.db.table.PropertyConverter;

import java.util.Arrays;
import java.util.List;

public class StringListConverter implements PropertyConverter<List<String>, String> {

    /**
     * 解析数据库的值赋值给实体类。
     *
     * @param databaseValue 数据库的值，如"a,b,c"
     * @return 如存放了a,b,c三个元素的List
     */
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        } else {
            return Arrays.asList(databaseValue.split(","));
        }
    }

    /**
     * 将复杂数据类型映射到数据库。
     *
     * @param entityProperty 如存放了a,b,c三个元素的List
     * @return 数据库的值，如"a,b,c"
     */
    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String link : entityProperty) {
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}

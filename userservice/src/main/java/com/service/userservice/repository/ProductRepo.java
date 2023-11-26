package com.service.userservice.repository;

import com.service.userservice.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ProductRepo {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public String saveData(Map<String, Object> payload) {
        String tableName = payload.get("table").toString();
        List<Map<String, String>> dataList = (List<Map<String, String>>) payload.get("records");
        if (!tableExists(tableName)) {
            createTable(tableName, dataList.get(0));
        }
        try {
            for (Map<String, String> data : dataList) {
                String sql = "INSERT INTO " + tableName + " (" +
                        String.join(", ", data.keySet()) +
                        ") VALUES (:" +
                        String.join(", :", data.keySet()) +
                        ")";
                namedParameterJdbcTemplate.update(sql, data);
            }
            return Map.of("status",true).toString();
        } catch (Exception e) {
            throw new CustomException("Something went wrong");
        }
    }

    private void createTable(String tableName, Map<String, String> dataList) {
        try {
            StringBuilder query = new StringBuilder("CREATE TABLE " + tableName + " (\n" +
                    "id INT AUTO_INCREMENT PRIMARY KEY");

            for (String column : dataList.keySet()) {
                query.append(" , " + column + " varchar(255)");
            }
            query.append(");");
            namedParameterJdbcTemplate.getJdbcTemplate().execute(query.toString());
        } catch (Exception e) {
            throw new CustomException("Something went wrong");

        }
    }

    private boolean tableExists(String tableName) {
        try {
            String query = "SELECT count(*) FROM " + tableName + ";";
            Integer count = namedParameterJdbcTemplate.queryForObject(query, Collections.emptyMap(), Integer.class);
            return count != 0 && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getAll() {
        try {
            String query = "Select * from products";
            return namedParameterJdbcTemplate.queryForList(query, Map.of());
        }
        catch (Exception e){
            throw new CustomException("Table not Exists");
        }
    }
}

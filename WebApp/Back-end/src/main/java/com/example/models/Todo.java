package com.example.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@DatabaseTable(tableName = "todos")
@Data
public class Todo {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String description;


    public Todo() {
        // ORMLite needs a no-arg constructor
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(description);
    }

}

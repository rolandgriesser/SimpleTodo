package io.griesser.simpletodo.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rolan on 26/07/2017.
 */

@Table(database = TodoItemDatabase.class)
public class TodoItem extends BaseModel implements Serializable {

    @Column(name = "_id")
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Date dueTo;

    @Column
    private int priority;

    @Column
    private int status;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public Date getDueTo() {
        return dueTo;
    }

    public void setDueTo(Date dueTo) {
        this.dueTo = dueTo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String getPriorityText(int priority) {
        switch(priority) {
            case 3: return "High";
            case 0: return "Normal";
            case -3: return "Low";
            default: return "Unknown";
        }
    }

    public static int getPriority(String priority) {
        switch(priority) {
            case "High": return 3;
            case "Low": return -3;
            default: return 0;
        }
    }
}

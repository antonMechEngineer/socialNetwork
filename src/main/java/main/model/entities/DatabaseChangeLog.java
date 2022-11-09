package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "databasechangelog") //todo м.б. поставить пробелы т.к. плохо читать
public class DatabaseChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String author;
    private String fileName;
    @Column(name = "dateexecuted") //// TODO: 08.11.2022 м.б записать их через пробелы
    private Timestamp dateExecuted;
    @Column(name = "orderexecuted")
    private int orderExecuted;
    @Column(name = "exectype", length = 10)
    private String execType;
    @Column(name = "md5sum", length = 35)
    private String md5Sum;
    private String description;
    private String comments;
    private String tag;
    @Column(length = 20)
    private String liquibase;
    private String contexts;
    private String labels;
    @Column (name = "deployment_id", length = 10)
    private String deploymentID;

}

package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "databaseChangeLogLoc")
public class DatabaseChangeLogLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean locked; // TODO: 08.11.2022 м.б. писать isLocked
    private Timestamp lockGranted;
    private String lockedby;

}

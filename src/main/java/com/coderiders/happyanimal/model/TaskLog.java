package com.coderiders.happyanimal.model;

import com.coderiders.happyanimal.enums.RepeatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_log")
public class TaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "type")
    private TaskType taskType;

    @Column(name = "date_expires")
    @JsonFormat(pattern= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiresDateTime;

    @Column(name = "date_completed")
    @JsonFormat(pattern= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime completedDateTime;

    @Column(name = "repeat_type")
    @Enumerated(EnumType.STRING)
    private RepeatType repeatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Animal animal;


    @Column(name = "note")
    private String note;//заметка, комментарий
}

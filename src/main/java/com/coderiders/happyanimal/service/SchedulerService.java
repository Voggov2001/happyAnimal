package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.TaskType;
import com.coderiders.happyanimal.model.MessageFromScheduler;
import com.coderiders.happyanimal.model.Task;
import com.coderiders.happyanimal.repository.TaskRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@NoArgsConstructor
public class SchedulerService {
    private TaskRepository taskRepository;
    private MessageFromScheduler message;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SchedulerService(TaskRepository taskRepository, MessageFromScheduler message, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskRepository = taskRepository;
        this.message = message;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Scheduled(cron = "0 35 3 * * ?")
    public void resetStateTask() {
        List<Task> result = taskRepository.findAll();
        for (var i : result) {
            if (i.getRepeatType().equals(TaskType.EVERY_DAY.getString())) {
                i.setState("in progress...");
                message.setContent("Статус задания " + i.getType() + " обновлен");
                this.simpMessagingTemplate.convertAndSend("/topic/public", message);
            }
        }
    }

    @Scheduled(cron = "0 0 0 ? * 1")
    public void resetStateTaskWeek() {
        List<Task> result = taskRepository.findAll();
        for (var i : result) {
            if (i.getRepeatType().equals(TaskType.EVERY_WEEK.getString())) {
                i.setState("in progress...");
                message.setContent("Статус задания " + i.getType() + " обновлен");
                this.simpMessagingTemplate.convertAndSend("/topic/public", message);
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetStateTaskMonth() {
        List<Task> result = taskRepository.findAll();
        for (var i : result) {
            if (i.getRepeatType().equals(TaskType.EVERY_MONTH.getString())) {
                i.setState("in progress...");
                message.setContent("Статус задания " + i.getType() + " обновлен");
                this.simpMessagingTemplate.convertAndSend("/topic/public", message);
            }
        }
    }
}

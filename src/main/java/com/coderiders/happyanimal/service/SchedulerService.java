package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.model.*;
import com.coderiders.happyanimal.model.dto.WeatherDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.TaskLogRepository;
import com.coderiders.happyanimal.repository.TaskRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@NoArgsConstructor
public class SchedulerService {
    private TaskRepository taskRepository;
    private SimpMessagingTemplate simpMessagingTemplate;
    private TaskLogRepository taskLogRepository;
    private AnimalRepository animalRepository;
    private WeatherService weatherService;

    boolean winterFlag = false;

    @Autowired
    public SchedulerService(TaskRepository taskRepository,
                            SimpMessagingTemplate simpMessagingTemplate,
                            TaskLogRepository taskLogRepository,
                            AnimalRepository animalRepository,
                            WeatherService weatherService) {
        this.taskRepository = taskRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.taskLogRepository = taskLogRepository;
        this.animalRepository = animalRepository;
        this.weatherService = weatherService;
    }

    @Transactional
    @Scheduled(cron = "0 */5 * ? * *")
    public void updateTasksEvery5min() {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(task -> {
            if (task.isCompleted()) {
                TaskLog taskLog = TaskLog.builder()
                        .taskId(task.getId())
                        .taskType(task.getTaskType())
                        .expiresDateTime(task.getExpiresDateTime())
                        .completedDateTime(LocalDateTime.now())
                        .repeatType(task.getRepeatType())
                        .animal(task.getAnimal())
                        .note(task.getNote())
                        .build();
                switch (task.getRepeatType()) {
                    case ONCE:
                        taskRepository.delete(task);
                        break;
                    case EVERY_DAY:
                        task.setExpiresDateTime(task.getExpiresDateTime().plusDays(1));
                        taskRepository.save(task);
                        break;
                    case EVERY_WEEK:
                        task.setExpiresDateTime(task.getExpiresDateTime().plusWeeks(1));
                        taskRepository.save(task);
                        break;
                    case EVERY_MONTH:
                        task.setExpiresDateTime(task.getExpiresDateTime().plusMonths(1));
                        taskRepository.save(task);
                        break;
                    case EVERY_YEAR:
                        task.setExpiresDateTime(task.getExpiresDateTime().plusYears(1));
                        taskRepository.save(task);
                        break;
                }
                taskLogRepository.save(taskLog);
            }
        });

    }

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void sendNotificationsEvery5min() {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(task -> {
            if (LocalDateTime.now().plusMinutes(10).plusSeconds(2).isAfter(task.getExpiresDateTime()) && !task.isCompleted()) {
                if (Optional.ofNullable(task.getAnimal().getUser()).isPresent()) {
                    this.simpMessagingTemplate.convertAndSend("/topic/" + task.getAnimal().getUser().getId(),
                            "Скоро завершится задача №" + task.getId());
                }
            }
        });
    }

    @Transactional
    @Scheduled(cron = "0 0 0 1 1 *")
    public void setAgeEveryYear() {
        List<Animal> animals = animalRepository.findAll();
        animals.forEach(animal -> {
            if (!Objects.equals(animal.getStatus(), AnimalStatus.NEWBORN)) {
                animal.setAge(animal.getAge() + 1);
                animalRepository.save(animal);
            }
        });
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * 11,12,1 *")
    public void setWinterFlagTrueEveryWinter() {
        WeatherDto weatherDto = weatherService.getWeatherForecast(1);
        if (Objects.requireNonNull(weatherDto).getTempC() <= -10 && !winterFlag) {
            setTaskToEveryAnimal("Перевести животное в зимний вольер");
            winterFlag = true;
        }

    }

    @Transactional
    @Scheduled(cron = "0 0 0 * 3,4,5 *")
    public void setWinterFlagFalseEverySpring() {
        WeatherDto weatherDto = weatherService.getWeatherForecast(1);
        if (Objects.requireNonNull(weatherDto).getTempC() > 5 && winterFlag) {
            setTaskToEveryAnimal("Перевести животное в летний вольер");
            winterFlag = false;
        }

    }

    @Transactional
    public void setTaskToEveryAnimal(String taskType) {
        List<Animal> animals = animalRepository.findAll();
        if (!animals.isEmpty()) {
            animals.forEach(animal -> {
                if (animal.getStatus() != AnimalStatus.DEAD || animal.getStatus() != AnimalStatus.SOLD) {
                    Task task = Task.builder()
                            .taskType(new TaskType(taskType))
                            .completed(false)
                            .expiresDateTime(LocalDateTime.now().plusDays(3))
                            .repeatType(RepeatType.ONCE)
                            .build();
                    taskRepository.save(task);
                }
            });
        }
    }
}

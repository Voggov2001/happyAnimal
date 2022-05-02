package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.model.*;
import com.coderiders.happyanimal.model.dto.WeatherFromJson;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.TaskLogRepository;
import com.coderiders.happyanimal.repository.TaskRepository;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private MessageFromScheduler message;
    private SimpMessagingTemplate simpMessagingTemplate;
    private TaskLogRepository taskLogRepository;
    private AnimalRepository animalRepository;
    private WeatherService weatherService;

    public Boolean winterFlag = false;

    public SchedulerService(TaskRepository taskRepository,
                            MessageFromScheduler message,
                            SimpMessagingTemplate simpMessagingTemplate,
                            TaskLogRepository taskLogRepository,
                            AnimalRepository animalRepository,
                            WeatherService weatherService) {
        this.taskRepository = taskRepository;
        this.message = message;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.taskLogRepository = taskLogRepository;
        this.animalRepository = animalRepository;
        this.weatherService = weatherService;
    }

    @Transactional
    @Scheduled(cron = "0 */10 * ? * *")
    public void updateTasksEvery10min() {
        List<Task> tasks = taskRepository.findAll();
        if (!tasks.isEmpty()) {
            tasks.forEach(task -> {
                if (task.isCompleted() && LocalDateTime.now().isAfter(task.getExpiresDateTime())) {
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

    }

    @Transactional
    @Scheduled(cron = "0 */10 * ? * *")
    public void sendNotificationsEvery10min() {
        List<Task> tasks = taskRepository.findAll();
        if (!tasks.isEmpty()) {
            tasks.forEach(task -> {
                if (LocalDateTime.now().plusMinutes(10).plusSeconds(2).isAfter(task.getExpiresDateTime())) {
                    message.setContent("Скоро завершится задача" + task.getId());
                    if (Optional.ofNullable(task.getAnimal().getUser()).isPresent()) {
                        this.simpMessagingTemplate.convertAndSend("/topic/" + task.getAnimal().getUser().getId(), message);
                    }
                }
            });
        }
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
        ResponseEntity<WeatherFromJson> weather = weatherService.getWeatherForecast(1);
        if (weather.hasBody()) {
            if (Objects.requireNonNull(weather.getBody()).getCurrent().getTempC() <= -10 && !winterFlag) {
                setTaskToEveryAnimal("Перевести животное в зимний вольер");
                winterFlag = true;
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * 3,4,5 *")
    public void setWinterFlagFalseEverySpring() {
        ResponseEntity<WeatherFromJson> weather = weatherService.getWeatherForecast(1);
        if (weather.hasBody()) {
            if (Objects.requireNonNull(weather.getBody()).getCurrent().getTempC() > 5 && winterFlag) {
                setTaskToEveryAnimal("Перевести животное в летний вольер");
                winterFlag = false;
            }
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
